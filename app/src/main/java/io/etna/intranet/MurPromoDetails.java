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
import android.widget.TextView;

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
import io.etna.intranet.Models.CustomAdapterMur;
import io.etna.intranet.Models.CustomAdapterMurDetails;
import io.etna.intranet.Models.MurDetailsModel;
import io.etna.intranet.Models.MurModel;

public class MurPromoDetails extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_mur_details, container, false);
    }

    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Discussion");

        /* Récupere les informations (id) de la vue précédente */
        String idPrincipal = getArguments().getString("idPrincipal");
        String titrePrincipal = getArguments().getString("titrePrincipal");
        String createurPrincipal= getArguments().getString("createurPrincipal");
        String dateCreationPrincipal = getArguments().getString("dateCreationPrincipal");
        String messagePrincipal = getArguments().getString("messagePrincipal");

        /* Modifie le contenu du message principal */
        TextView titreDiscussOnView = (TextView) getActivity().findViewById(R.id.titre_discuss);
        TextView titrePrincipalOnView = (TextView) getActivity().findViewById(R.id.titrePrincipal);
        TextView createurPrincipalOnView = (TextView) getActivity().findViewById(R.id.createurPrincipal);
        TextView dateCreationPrincipalOnView = (TextView) getActivity().findViewById(R.id.dateCreationPrincipal);
        TextView messagePrincipalOnView = (TextView) getActivity().findViewById(R.id.messagePrincipal);
        titreDiscussOnView.setText(titrePrincipal);
        titrePrincipalOnView.setText(titrePrincipal);
        createurPrincipalOnView.setText(createurPrincipal);
        dateCreationPrincipalOnView.setText(dateCreationPrincipal);
        messagePrincipalOnView.setText(messagePrincipal);

        /* Liste des messages du mur */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        List<MurDetailsModel> messages = genererMur(idPrincipal);

        CustomAdapterMurDetails adapter = new CustomAdapterMurDetails(getActivity() , messages);
        mListView.setAdapter(adapter);
    }


    private List<MurDetailsModel> genererMur(final String idPrincipal){
        List<MurDetailsModel> messages = new ArrayList<MurDetailsModel>();
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
                return String.valueOf(parse(data[0], idPrincipal));
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
                messages.add(new MurDetailsModel("", My_data.getString("createur"), My_data.getString("date"), My_data.getString("message")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return messages;
    }

    private String parse(JSONObject resobj, String idPrincipal)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            JSONArray  hits = resobj.getJSONArray("hits");

            for(int i = 0; i < hits.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = hits.getJSONObject(i);
                if (object3.getString("id").equals(idPrincipal)) {
                    JSONArray messages = object3.getJSONArray("messages");
                    for(int j = 0; j < messages.length(); j++)
                    {
                        JSONObject onemessage = messages.getJSONObject(j);
                        Log.d("Message : ", onemessage.toString());
                        Final_Object.put("createur", onemessage.getString("user"));
                        Final_Object.put("date", onemessage.getString("created_at"));
                        Final_Object.put("message", onemessage.getString("content"));
                    }
                    Final_Array.put(Final_Object);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    private JSONObject searchCall() throws JSONException {
        String[] path = {"terms", "Prep'ETNA2 - 2020", "conversations"};
        String[] get = {"0" , "50"};
        String[] get_data = {"from" , "size"};
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://prepintra-api.etna-alternance.net/", path);
        return new JSONObject(data);
    }

}
