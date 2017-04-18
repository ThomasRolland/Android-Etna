package io.etna.intranet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

public class Activites extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_activites, container, false);
    }

    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Activités");

          /* Liste d'activités */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        List<ActiviteModel> activites = genererActivites();

        CustomAdapterActivite adapter = new CustomAdapterActivite(getActivity(), activites);
        mListView.setAdapter(adapter);

    }
    private List<ActiviteModel> genererActivites(){
        List<ActiviteModel> activites = new ArrayList<ActiviteModel>();
        String json_string = null;
        //requette
        final JSONObject[] data = new JSONObject[1];
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                try {
                    data[0] = searchCall();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return String.valueOf(parse(data[0]));
            }
        };
        Future<String> future = executor.submit(callable);
        try {
            json_string = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        try {
            JSONArray get_data = new JSONArray(json_string);
            for(int i = 0; i < get_data.length(); i++) {
                ArrayList<String> cours = new ArrayList<String>();
                JSONObject My_data = get_data.getJSONObject(i);
                String key = "";
                if (!My_data.isNull("key")){
                    key = My_data.getString("key");
                }
                String name = "";
                if(!My_data.isNull("name"))
                {
                    name = My_data.getString("name");
                }
                String date = "";
                if (!My_data.isNull("date"))
                {
                    date = "fin le "+My_data.getString("date");
                }
                if (!My_data.isNull("cour"))
                {
                    JSONArray cours_array = My_data.getJSONArray("cour");
                    for(int j = 0; j < cours_array.length(); j++) {
                        JSONObject cours_object = cours_array.getJSONObject(j);
                        cours.add(cours_object.getString("name"));
                        /*cours.add(cours_object.getString("id"));*/
                    }
                }
                activites.add(new ActiviteModel(key, name, date, cours));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return activites;
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
        String[] path = {"students", "rollan_t", "currentactivities"};
        final String data = NetworkService.INSTANCE.search("test", "https://modules-api.etna-alternance.net/", path);
        return new JSONObject(data);
    }
}

