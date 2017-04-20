package io.etna.intranet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.ActiviteModel;
import io.etna.intranet.Models.CustomAdapterActivite;
import io.etna.intranet.Storage.TinyDB;

public class Activites extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_activites, container, false);
    }

    private ListView listView;
    private ArrayList<ActiviteModel> list;
    private CustomAdapterActivite adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Activités");

        list = new ArrayList<>();
        /**
         * Binding that List to Adapter
         */
        adapter = new CustomAdapterActivite(getContext(), list);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) getActivity().findViewById(R.id.flux);
        listView.setAdapter(adapter);
        new GetDataTask().execute();

    }


    class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            String json_string = null;
            final JSONObject[] data = new JSONObject[1];
            try {
                data[0] = searchCall();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_string = parse(data[0]);
            try {
                JSONArray get_data = new JSONArray(json_string);
                for (int i = 0; i < get_data.length(); i++) {

                    ArrayList<String> cours = new ArrayList<String>();
                    JSONObject My_data = get_data.getJSONObject(i);
                    String key = "";
                    if (!My_data.isNull("key")) {
                        key = My_data.getString("key");
                    }
                    String name = "";
                    if (!My_data.isNull("name")) {
                        name = My_data.getString("name");
                    }
                    String date = "";
                    if (!My_data.isNull("date")) {
                        date = "fin le " + My_data.getString("date");
                    }
                    if (!My_data.isNull("cour")) {
                        JSONArray cours_array = My_data.getJSONArray("cour");
                        for (int j = 0; j < cours_array.length(); j++) {
                            JSONObject cours_object = cours_array.getJSONObject(j);
                            cours.add(cours_object.getString("name"));
                        }
                    }
                    ActiviteModel model = new ActiviteModel(key, name, date, cours);
                    list.add(model);
                    //activites.add(new ActiviteModel(key, name, date, cours));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /**
             * Checking if List size if more than zero then
             * Update ListView
             */
            if(list.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Log.d("fail", "fail");
            }
        }
    }

    private String parse(JSONObject resobj)
    {
        Iterator<?> keys = resobj.keys();
        JSONArray Final_Array = new JSONArray();
        while(keys.hasNext())
        {
            JSONArray Cour_Array = new JSONArray();
            JSONObject Final_Object = new JSONObject();
            String key = (String)keys.next();
            try
            {
                if (resobj.get(key) instanceof JSONObject)
                {
                    Final_Object.put("key", key);
                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                    JSONArray  project = xx.getJSONArray("project");
                    for(int i = 0; i < project .length(); i++)
                    {
                        JSONObject object3 = project.getJSONObject(i);
                        Final_Object.put("name", object3.getString("name"));
                        Final_Object.put("date", object3.getString("date_end"));
                    }
                    JSONArray cours = xx.getJSONArray("cours");
                    if (cours.length() != 0)
                    {
                        for(int i = 0; i < cours.length(); i++)
                        {
                            JSONObject cour_object = new JSONObject();
                            JSONObject object4 = cours.getJSONObject(i);
                            cour_object.put("name", object4.getString("name"));
                            cour_object.put("id", object4.getString("activity_id"));
                            Cour_Array.put(cour_object);
                        }
                        Final_Object.put("cour", Cour_Array);
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Final_Array.put(Final_Object);
        }
        return String.valueOf(Final_Array);
    }

    private JSONObject searchCall() throws JSONException {
        TinyDB tinydb = new TinyDB(getContext());
        String[] path = {"students", tinydb.getString("userName"), "currentactivities"};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data, "https://modules-api.etna-alternance.net/", path);
        return new JSONObject(data);
    }
}

