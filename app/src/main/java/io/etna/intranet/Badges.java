package io.etna.intranet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.BadgeModel;
import io.etna.intranet.Models.CustomAdapterBadge;

public class Badges extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_badges, container, false);
    }

    ListView mListView;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes Badges");

          /* Liste d'activités */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        List<BadgeModel> Badges = genererBadges();
        Log.d("Badges : ", String.valueOf(Badges.size()));
        CustomAdapterBadge adapter = new CustomAdapterBadge(getActivity(), Badges);
        mListView.setAdapter(adapter);
    }


    private List<BadgeModel> genererBadges(){
        List<BadgeModel> Badges = new ArrayList<BadgeModel>();
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
        for(int i = 0; i < get_data.length(); i++) {
            JSONObject My_data = null;
            try {
                My_data = get_data.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Badges.add(new BadgeModel(My_data.getString("name"), My_data.getString("image")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return Badges;
    }

    private String parse(JSONArray resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            for(int i = 0; i < resobj.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = resobj.getJSONObject(i);
                Final_Object.put("name", object3.getString("name"));
                Final_Object.put("image", object3.getString("image"));
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
        String[] path = {"api", "users", "bedmin_j", "achievements"};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://achievements.etna-alternance.net/", path);
        return new JSONArray(data);
    }

}

