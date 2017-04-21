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
import java.util.ArrayList;

import io.etna.intranet.Curl.CheckConnection;
import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.ActiviteModel;
import io.etna.intranet.Models.CustomAdapterActivite;
import io.etna.intranet.Parse.JSONParse;
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
            adapter = new CustomAdapterActivite(getContext(), list);

            /**
             * Getting List and Setting List Adapter
             */
            listView = (ListView) getActivity().findViewById(R.id.flux);
            listView.setAdapter(adapter);
            new GetDataTask().execute();
        }
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
            String json_string = null;
            final JSONObject[] data = new JSONObject[1];
            try {
                data[0] = searchCall();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_string = JSONParse.parseActivites(data[0]);
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
                ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                Log.d("fail", "fail");
            }
        }
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

