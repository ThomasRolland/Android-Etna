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
import io.etna.intranet.Models.CustomAdapterTicketDetails;
import io.etna.intranet.Models.TicketDetailsModel;
import io.etna.intranet.Parse.JSONParse;

public class TicketsDetails extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_tickets_details, container, false);
    }


    private ListView listView;
    private ArrayList<TicketDetailsModel> list;
    private CustomAdapterTicketDetails adapter;
    public String idTicket = new String();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes Tickets");

        /* Récupere les informations (id) de la vue précédente */
        idTicket = getArguments().getString("idTicket");
        TextView titre = (TextView) getActivity().findViewById(R.id.titreTicket);
        titre.setText(getArguments().getString("titreTicket"));

        list = new ArrayList<>();
        /**
         * Binding that List to Adapter
         */
        adapter = new CustomAdapterTicketDetails(getContext(), list);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) getActivity().findViewById(R.id.flux);
        listView.setAdapter(adapter);
        new TicketsDetails.GetDataTask().execute();


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

            List<TicketDetailsModel> Tickets = new ArrayList<TicketDetailsModel>();
            String json_string = null;
            //requette
            final JSONObject[] data = new JSONObject[1];
            try {
                data[0] = searchCall(idTicket);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            json_string = JSONParse.parseTicketsDetails(data[0]);
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
                    TicketDetailsModel model = new TicketDetailsModel(My_data.getString("message"), "Le : "+My_data.getString("created_at"), My_data.getString("author_login"), My_data.getString("author_mail"));
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

    private JSONObject searchCall(String idTicket) throws JSONException {
        String[] path = {};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://tickets.etna-alternance.net/api/tasks/"+idTicket+".json", path);
        Log.d("Ticket : ", data);
        return new JSONObject(data);
    }

}

