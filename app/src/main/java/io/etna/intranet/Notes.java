package io.etna.intranet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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

        notes.add(new NoteModel("FDI-COBJ", "Programation C++", "Cardboard Pulley 2", "Très bon projet, bonus interessants", "16", "-42", "10", "22", true));

        return notes;
    }

}
