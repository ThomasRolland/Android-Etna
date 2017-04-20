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
import io.etna.intranet.Models.CustomAdapterTicket;
import io.etna.intranet.Models.TicketModel;

public class Tickets extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_tickets, container, false);
    }


    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes Tickets");

          /* Liste d'activités */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        final List<TicketModel> Tickets = genererTickets();

        CustomAdapterTicket adapter = new CustomAdapterTicket(getActivity(), Tickets);
        mListView.setAdapter(adapter);

        /* Passer au fragment détail : voir un ticket */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Sauvegarde les values a passer*/
                Bundle bundle = new Bundle();
                String idPrincipal = Tickets.get(position).getTicketId();
                bundle.putString("idTicket",idPrincipal);

                /*Change de fragment*/
                TicketsDetails fragment2 = new TicketsDetails();
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

            List<TicketModel> Tickets = new ArrayList<TicketModel>();
            String json_string = null;
            //requette
            final JSONObject[] data = new JSONObject[1];
                        data[0] = searchCall();
                    return String.valueOf();

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
                    Tickets.add(new TicketModel(My_data.getString("id"), My_data.getString("title"), "Créé le : "+My_data.getString("created_at"), My_data.getString("updated_at"), My_data.getString("state"), My_data.getString("last_editor")));
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
            JSONArray hits = resobj.getJSONArray("data");
            for(int i = 0; i < hits.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = hits.getJSONObject(i);
                if (object3.getString("closed_at") == "null") {
                    Final_Object.put("state", "Ouvert");
                }
                else {
                    Final_Object.put("state", "Fermé");
                }
                    Final_Object.put("id", object3.getString("id"));
                    Final_Object.put("title", object3.getString("title"));
                    Final_Object.put("created_at", object3.getString("created_at"));
                    Final_Object.put("updated_at", object3.getString("updated_at"));
                    Final_Object.put("last_editor", object3.getJSONObject("last_edit").getString("login"));
                    Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    private JSONObject searchCall() throws JSONException {
        String[] path = {};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://tickets.etna-alternance.net/api/tasks.json", path);
        return new JSONObject(data);
    }

}

