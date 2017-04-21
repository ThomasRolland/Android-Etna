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
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.CustomAdapterMurDetails;
import io.etna.intranet.Models.MurDetailsModel;
import io.etna.intranet.Parse.JSONParse;
import io.etna.intranet.Storage.TinyDB;

public class MurPromoDetails extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_mur_details, container, false);
    }

    private ListView listView;
    private ArrayList<MurDetailsModel> list;
    private CustomAdapterMurDetails adapter;
    public String idPrincipal = new String();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Discussion");

        /* Récupere les informations (id) de la vue précédente */
        idPrincipal = getArguments().getString("idPrincipal");
        String titrePrincipal = getArguments().getString("titrePrincipal");


        /* Modifie le contenu du message principal */
        TextView titreDiscussOnView = (TextView) getActivity().findViewById(R.id.titre_discuss);
        titreDiscussOnView.setText(titrePrincipal);


        list = new ArrayList<>();
        /**
         * Binding that List to Adapter
         */
        adapter = new CustomAdapterMurDetails(getContext(), list);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) getActivity().findViewById(R.id.flux);
        listView.setAdapter(adapter);
        new MurPromoDetails.GetDataTask().execute();
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

            List<MurDetailsModel> messages = new ArrayList<MurDetailsModel>();
            String json_string = null;
            //requette
            final JSONObject[] data = new JSONObject[1];
            try {
                data[0] = searchCall();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            json_string = JSONParse.parseMurPromoDetails(data[0], idPrincipal);
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
                    //ici
                    MurDetailsModel model = new MurDetailsModel("", My_data.getString("id_user"), My_data.getString("date"), My_data.getString("message"));
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


    private JSONObject searchCall() throws JSONException {
        TinyDB tinydb = new TinyDB(getContext());
        String[] path = {"terms", tinydb.getString("userPromoName"),"conversations"};
        String[] get = {"0" , "8"};
        String[] get_data = {"from" , "size"};
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://prepintra-api.etna-alternance.net/", path);
        return new JSONObject(data);
    }
    private JSONObject searchCall_user(String id) throws JSONException {
        String[] path = {"api", "users", id};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data, "https://auth.etna-alternance.net/", path);
        return new JSONObject(data);
    }
}
