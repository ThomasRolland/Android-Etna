package io.etna.intranet;

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
import io.etna.intranet.Models.CustomAdapterEvent;
import io.etna.intranet.Models.EventModel;
import io.etna.intranet.Storage.TinyDB;

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



    private List<EventModel> genererEvents() {
        List<EventModel> Events = new ArrayList<EventModel>();
        String json_string = null;
        //requette
        final JSONArray[] data = new JSONArray[1];
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
        JSONArray get_data = null;
        try {
            get_data = new JSONArray(json_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (get_data.length() > 0) {
        for(int i = 0; i < get_data.length(); i++) {
            JSONObject My_data = null;
            try {
                My_data = get_data.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Events.add(new EventModel(My_data.getString("name"), My_data.getString("location"), My_data.getString("start"), My_data.getString("end")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        }
        else {
            Log.d("Events : ", "Aucun");
            Events.add(new EventModel("Aucun.", "", "", ""));
        }

        return Events;
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
