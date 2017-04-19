package io.etna.intranet;

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
import io.etna.intranet.Models.CustomAdapterTicket;
import io.etna.intranet.Models.CustomAdapterTicketDetails;
import io.etna.intranet.Models.TicketDetailsModel;

public class TicketsDetails extends Fragment {
    public String idTicket = getArguments().getString("idTicket");

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
        Log.d("Ticket de la vue chargé : id ", idTicket);

        /* Liste d'activités */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        final List<TicketDetailsModel> Tickets = genererTickets();

        CustomAdapterTicketDetails adapter = new CustomAdapterTicketDetails(getActivity(), Tickets);
        mListView.setAdapter(adapter);


    }


    private List<TicketDetailsModel> genererTickets(){
        List<TicketDetailsModel> Tickets = new ArrayList<TicketDetailsModel>();
        String json_string = null;
        //requette
        final JSONObject[] data = new JSONObject[1];
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
                Tickets.add(new TicketDetailsModel(My_data.getString("message"), "Le : "+My_data.getString("created_at"), My_data.getString("author_login"), My_data.getString("author_mail")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return Tickets;
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
                Final_Object.put("message", object3.getJSONObject("messages").getString("content"));
                Final_Object.put("created_at", object3.getJSONObject("messages").getString("created_at"));
                Final_Object.put("author_login", object3.getJSONObject("messages").getJSONObject("author").getString("login"));
                Final_Object.put("author_mail", object3.getJSONObject("messages").getJSONObject("author").getString("email"));
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
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://tickets.etna-alternance.net/api/tasks/"+idTicket+".json", path);
        Log.d("Ticket : ", data);
        return new JSONObject(data);
    }

}

