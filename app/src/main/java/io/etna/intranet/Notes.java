package io.etna.intranet;

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
import io.etna.intranet.Models.CustomAdapterNote;
import io.etna.intranet.Models.NoteModel;

public class Notes extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_notes, container, false);
    }

    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes Notes");

      /* Liste les notes */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        List<NoteModel> notes = genererNotes();

        CustomAdapterNote adapter = new CustomAdapterNote(getActivity() ,notes);
        mListView.setAdapter(adapter);

    }


    private List<NoteModel> genererNotes(){
        List<NoteModel> notes = new ArrayList<NoteModel>();
        String json_string = null;
        //requette
        final JSONArray[] data = new JSONArray[1];
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
                notes.add(new NoteModel(My_data.getString("UVNom"), My_data.getString("UVDescription"), My_data.getString("projet"), My_data.getString("commentaire"), My_data.getString("note"), My_data.getString("noteMin"), My_data.getString("noteMoy"), My_data.getString("noteMax"), true));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return notes;
    }

    private String parse(JSONArray resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            for(int i = 0; i < resobj.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = resobj.getJSONObject(i);
                Final_Object.put("UVNom", object3.getString("uv_name"));
                Final_Object.put("UVDescription", object3.getString("uv_long_name"));
                Final_Object.put("projet", object3.getString("activity_name"));
                Final_Object.put("commentaire", object3.getJSONObject("checklist").getJSONArray("comments").getJSONObject(0).getString("comment"));
                Final_Object.put("note", object3.getString("student_mark"));
                Final_Object.put("noteMin", object3.getString("minimal"));
                Final_Object.put("noteMax", object3.getString("maximal"));
                Final_Object.put("noteMoy", object3.getString("average"));
                Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    private JSONArray searchCall() throws JSONException {
        String[] path = {"terms", "205", "students", "bedmin_j", "marks"};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data,"\n" + "https://prepintra-api.etna-alternance.net/", path);
        Log.d("Tickets", data);
        return new JSONArray(data);
    }

}
