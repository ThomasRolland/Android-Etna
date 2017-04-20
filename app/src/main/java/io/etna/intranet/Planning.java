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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.ActiviteModel;
import io.etna.intranet.Models.CustomAdapterEvent;
import io.etna.intranet.Models.EventModel;
import io.etna.intranet.Storage.TinyDB;

import static android.R.id.list;

public class Planning extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_planning, container, false);
    }

    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mon Planning");
       /* Liste d'activités */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        List<EventModel> Events = genererEvents();

        CustomAdapterEvent adapter = new CustomAdapterEvent(getActivity(), Events);
        mListView.setAdapter(adapter);

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
            JSONArray data = new JSONArray();
            try {
                data = searchCall();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_string = parse(data);
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

    private String parse(JSONArray resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
                for (int i = 0; i < resobj.length(); i++) {
                    JSONObject Final_Object = new JSONObject();
                    JSONObject object3 = resobj.getJSONObject(i);
                    Final_Object.put("name", object3.getString("name"));
                    Final_Object.put("location", object3.getString("location"));
                    Final_Object.put("start", object3.getString("start"));
                    Final_Object.put("end", object3.getString("end"));
                    Final_Array.put(Final_Object);
                }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    private JSONArray searchCall() throws JSONException {
        /* Date */
        SimpleDateFormat formater = null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date startDate = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date endDate = cal.getTime();

        formater = new SimpleDateFormat("yyyy-MM-dd");
        String startDatestr = formater.format(startDate);
        String endDatestr = formater.format(endDate);

        /* Appel*/
        TinyDB tinydb = new TinyDB(getContext());
        String[] path = {"students", tinydb.getString("userName"), "events"};
        String[] get = {endDatestr , startDatestr};
        String[] get_data = {"end" , "start"};
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://prepintra-api.etna-alternance.net/", path);
        return new JSONArray(data);
    }

}
