package io.etna.intranet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.CustomAdapterMur;
import io.etna.intranet.Models.MurModel;
import io.etna.intranet.Parse.JSONParse;
import io.etna.intranet.Storage.TinyDB;

public class MurPromo extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_mur, container, false);
    }

    private ListView listView;
    private ArrayList<MurModel> list;
    private CustomAdapterMur adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mur Promotion");


        list = new ArrayList<>();
        /**
         * Binding that List to Adapter
         */
        adapter = new CustomAdapterMur(getContext(), list);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) getActivity().findViewById(R.id.flux);
        listView.setAdapter(adapter);
        new MurPromo.GetDataTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Sauvegarde les values a passer*/
                Bundle bundle = new Bundle();
                String idPrincipal = list.get(position).getId();
                String titrePrincipal = list.get(position).getTitre();
                String createurPrincipal= list.get(position).getCreateur();
                String dateCreationPrincipal = list.get(position).getDateCreation();
                String messagePrincipal = list.get(position).getMessage();
                bundle.putString("idPrincipal",idPrincipal);
                bundle.putString("titrePrincipal",titrePrincipal);
                bundle.putString("createurPrincipal",createurPrincipal);
                bundle.putString("dateCreationPrincipal",dateCreationPrincipal);
                bundle.putString("messagePrincipal",messagePrincipal);

                /*Change de fragment*/
                MurPromoDetails fragment2 = new MurPromoDetails();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment2.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment2);
                fragmentTransaction.commit();
            }
        });
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

            List<MurModel> messages = new ArrayList<MurModel>();
            String json_string = null;
            //requette
            final JSONObject[] data = new JSONObject[1];
            try {
                data[0] = searchCall();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_string = JSONParse.parseMurPromo(data[0]);
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
                    MurModel model = new MurModel(My_data.getString("id"), My_data.getString("id_user"), My_data.getString("title"), My_data.getString("date"), My_data.getString("message"), null);
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

}
