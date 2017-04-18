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

import java.util.ArrayList;
import java.util.List;

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

        List<MurDetailsModel> messages = genererMur();

        CustomAdapterMurDetails adapter = new CustomAdapterMurDetails(getActivity() , messages);
        mListView.setAdapter(adapter);
    }

    private List<MurDetailsModel> genererMur(){
        List<MurDetailsModel> messages = new ArrayList<MurDetailsModel>();

        messages.add(new MurDetailsModel("87265", "bedmin_j", "10/04/2017", "Merci."));

        return messages;
    }

}
