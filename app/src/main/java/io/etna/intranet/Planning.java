package io.etna.intranet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.etna.intranet.Curl.CheckConnection;
import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.CustomAdapterEvent;
import io.etna.intranet.Models.EventModel;
import io.etna.intranet.Parse.JSONParse;
import io.etna.intranet.Storage.TinyDB;

public class Planning extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_planning, container, false);
    }

    private ListView listView;
    private ArrayList<EventModel> list;
    private CustomAdapterEvent adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mon Planning");

        if (!CheckConnection.execute(getContext())) {
            Intent myIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(myIntent);
            Toast.makeText(getContext(), "Plus de connexion Internet, vérifiez vos reglages.", Toast.LENGTH_SHORT).show();
        }
        else {

            list = new ArrayList<>();
            /**
             * Binding that List to Adapter
             */
            adapter = new CustomAdapterEvent(getContext(), list);

            /**
             * Getting List and Setting List Adapter
             */
            listView = (ListView) getActivity().findViewById(R.id.flux);
            listView.setAdapter(adapter);
            new Planning.GetDataTask().execute();
        }
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

            List<EventModel> Events = new ArrayList<EventModel>();
            String json_string = null;
            //requette
            final JSONArray[] data = new JSONArray[1];
            try {
                data[0] = searchCall();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_string = JSONParse.parsePlanning(data[0]);
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
                        EventModel model = new EventModel(My_data.getString("name"), My_data.getString("location"), My_data.getString("start"), My_data.getString("end"));
                        list.add(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                EventModel model = new EventModel("Aucun.", "", "", "");
                list.add(model);
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
                ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                Log.d("fail", "fail");
            }
        }
    }


    private JSONArray searchCall() throws JSONException {
        /* Date */
        SimpleDateFormat formater = null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date startDate = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date endDate = cal.getTime();

        formater = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
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
