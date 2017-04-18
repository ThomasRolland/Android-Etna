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

import java.util.ArrayList;
import java.util.List;

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

        messages.add(new MurModel("87656", "Aurelie Reyjal", "Jour férié 17 Avril", "10/04/2017", "Vous serez en repos le 17 Avril 2017. Dormez bien."));

        return messages;
    }

}
