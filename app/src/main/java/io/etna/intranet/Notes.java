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
import java.util.List;

import io.etna.intranet.Curl.CheckConnection;
import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.CustomAdapterNote;
import io.etna.intranet.Models.NoteModel;
import io.etna.intranet.Parse.JSONParse;
import io.etna.intranet.Storage.TinyDB;

public class Notes extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_notes, container, false);
    }

    private ListView listView;
    private ArrayList<NoteModel> list;
    private CustomAdapterNote adapter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes Notes");

        if (CheckConnection.execute(getContext()) == false) {
            Intent myIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(myIntent);
            Toast.makeText(getContext(), "Plus de connexion Internet, vérifiez vos reglages.", Toast.LENGTH_SHORT).show();
        }
        else {

            list = new ArrayList<>();
            /**
             * Binding that List to Adapter
             */
            adapter = new CustomAdapterNote(getContext(), list);

            /**
             * Getting List and Setting List Adapter
             */
            listView = (ListView) getActivity().findViewById(R.id.flux);
            listView.setAdapter(adapter);
            new Notes.GetDataTask().execute();
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

            List<NoteModel> notes = new ArrayList<NoteModel>();
            String json_string = null;
            //requette
            final JSONArray[] data = new JSONArray[1];
            try {
                data[0] = searchCall();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_string = JSONParse.parseNotes(data[0]);
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
                    NoteModel model = new NoteModel(My_data.getString("UVNom"), My_data.getString("UVDescription"), My_data.getString("projet"), My_data.getString("commentaire"), My_data.getString("note"), My_data.getString("noteMin"), My_data.getString("noteMax"), My_data.getString("noteMoy"), true);
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
                ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                Log.d("fail", "fail");
            }
        }
    }

    private JSONArray searchCall() throws JSONException {
        TinyDB tinydb = new TinyDB(getContext());
        String[] path = {"terms", tinydb.getString("userIdPromo"), "students", tinydb.getString("userName"), "marks"};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data,"\n" + "https://prepintra-api.etna-alternance.net/", path);
        return new JSONArray(data);
    }

}
