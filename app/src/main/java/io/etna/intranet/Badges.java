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
import java.util.List;
import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.BadgeModel;
import io.etna.intranet.Models.CustomAdapterBadge;
import io.etna.intranet.Parse.JSONParse;
import io.etna.intranet.Storage.TinyDB;

public class Badges extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_badges, container, false);
    }


    private ListView listView;
    private ArrayList<BadgeModel> list;
    private CustomAdapterBadge adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes Badges");
        list = new ArrayList<>();
        /**
         * Binding that List to Adapter
         */
        adapter = new CustomAdapterBadge(getContext(), list);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) getActivity().findViewById(R.id.flux);
        listView.setAdapter(adapter);
        new Badges.GetDataTask().execute();
    }

    class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            List<BadgeModel> Badges = new ArrayList<BadgeModel>();
            String json_string = null;
            //requette
            final JSONArray[] data = new JSONArray[1];
            try {
                data[0] = searchCall();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            json_string = JSONParse.parseBadges(data[0]);
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
                    BadgeModel model = new BadgeModel(My_data.getString("name"), My_data.getString("image"));
                    list.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private JSONArray searchCall() throws JSONException {
        TinyDB tinydb = new TinyDB(getContext());
        String[] path = {"api", "users", tinydb.getString("userName"), "achievements"};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://achievements.etna-alternance.net/", path);
        return new JSONArray(data);
    }
}

