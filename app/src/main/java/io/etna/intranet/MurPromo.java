package io.etna.intranet;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.etna.intranet.Curl.NetworkService;
import io.etna.intranet.Models.ActiviteModel;
import io.etna.intranet.Models.CustomAdapterActivite;
import io.etna.intranet.Models.CustomAdapterMur;
import io.etna.intranet.Models.MurModel;

public class MurPromo extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_mur, container, false);
    }

    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mur Promotion");


      /* Liste des messages du mur */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        final List<MurModel> messages = genererMur();

        final CustomAdapterMur adapter = new CustomAdapterMur(getActivity() , messages);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Sauvegarde les values a passer*/
                Bundle bundle = new Bundle();
                String idPrincipal = messages.get(position).getId();
                String titrePrincipal = messages.get(position).getTitre();
                String createurPrincipal= messages.get(position).getCreateur();
                String dateCreationPrincipal = messages.get(position).getDateCreation();
                String messagePrincipal = messages.get(position).getMessage();
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

    private List<MurModel> genererMur(){
        List<MurModel> messages = new ArrayList<MurModel>();
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
                messages.add(new MurModel(My_data.getString("id"), My_data.getString("id_user"), My_data.getString("title"), My_data.getString("date"), My_data.getString("message")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return messages;
    }

    private String parse(JSONObject resobj)
    {
        JSONArray Final_Array = new JSONArray();
            try
            {
                    JSONArray  hits = resobj.getJSONArray("hits");
                    for(int i = 0; i < hits.length(); i++)
                    {
                        JSONObject Final_Object = new JSONObject();
                        JSONObject object3 = hits.getJSONObject(i);
                        Final_Object.put("id", object3.getString("id"));
                        Final_Object.put("title", object3.getString("title"));
                        Final_Object.put("date", object3.getString("created_at"));
                        Final_Object.put("message", object3.getJSONArray("messages").getJSONObject(0).getString("content"));
                        Final_Object.put("id_user", object3.getJSONObject("last_message").getString("user"));
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
        String[] path = {"terms", "Prep'ETNA2 - 2020", "conversations"};
        String[] get = {"0" , "8"};
        String[] get_data = {"from" , "size"};
        final String data = NetworkService.INSTANCE.search(get, get_data,"https://prepintra-api.etna-alternance.net/", path);
        return new JSONObject(data);
    }

}
