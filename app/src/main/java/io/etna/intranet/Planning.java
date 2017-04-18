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

import io.etna.intranet.Models.CustomAdapterEvent;
import io.etna.intranet.Models.EventModel;

public class Planning extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_planning, container, false);
    }

    ListView mListView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mon Planning");
       /* Liste d'activités */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        List<EventModel> Events = genererEvents();

        CustomAdapterEvent adapter = new CustomAdapterEvent(getActivity(), Events);
        mListView.setAdapter(adapter);

    }

    private List<EventModel> genererEvents() {
        List<EventModel> Events = new ArrayList<EventModel>();

        Events.add(new EventModel("Soutenance - Suivi PRO-PRIN", "Colabs", "18/04/17 19:00", "18/04/17 19:30"));
        return Events;
    }
}
