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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.ActiviteModel;
import io.etna.intranet.Models.CustomAdapterTicketDetails;
import io.etna.intranet.Models.TicketDetailsModel;

public class TicketsDetails extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_tickets_details, container, false);
    }


    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes Tickets");

        /* Récupere les informations (id) de la vue précédente */
        String idTicket = getArguments().getString("idTicket");
        Log.d("Ticket de la vue chargé : id ", idTicket);

        /* Liste d'activités */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        final List<TicketDetailsModel> Tickets = genererTickets(idTicket);

        CustomAdapterTicketDetails adapter = new CustomAdapterTicketDetails(getActivity(), Tickets);
        mListView.setAdapter(adapter);


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
                        data[0] = searchCall(idTicket);

                json_string = parse(data[0]);
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
                    Tickets.add(new TicketDetailsModel(My_data.getString("message"), "Le : "+My_data.getString("created_at"), My_data.getString("author_login"), My_data.getString("author_mail")));
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


    private String parse(JSONObject resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            JSONArray hits = resobj.getJSONObject("data").getJSONArray("messages");
            for(int i = 0; i < hits.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = hits.getJSONObject(i);
                Final_Object.put("message", object3.getString("content"));
                Final_Object.put("created_at", object3.getString("created_at"));
                Final_Object.put("author_login", object3.getJSONObject("author").getString("login"));
                Final_Object.put("author_mail", object3.getJSONObject("author").getString("email"));
                Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
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

