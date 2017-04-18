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

        List<TicketModel> Tickets = genererTickets();

        CustomAdapterTicket adapter = new CustomAdapterTicket(getActivity(), Tickets);
        mListView.setAdapter(adapter);

    }

    private List<TicketModel> genererTickets(){
        List<TicketModel> Tickets = new ArrayList<TicketModel>();

        Tickets.add(new TicketModel("50042", "Absence du 13 mars 2018", "Créé le : 23/03/17", "14/02/17", "", "reyjal_a"));

        return Tickets;
    }

}

