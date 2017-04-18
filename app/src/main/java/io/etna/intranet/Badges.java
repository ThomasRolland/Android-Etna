package io.etna.intranet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.etna.intranet.Models.BadgeModel;
import io.etna.intranet.Models.CustomAdapterBadge;

public class Badges extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_badges, container, false);
    }

    ListView mListView;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes Badges");

          /* Liste d'activités */
        mListView = (ListView) getActivity().findViewById(R.id.flux);

        List<BadgeModel> Badges = genererBadges();

        CustomAdapterBadge adapter = new CustomAdapterBadge(getActivity(), Badges);
        mListView.setAdapter(adapter);
    }

    private List<BadgeModel> genererBadges(){
        List<BadgeModel> Badges = new ArrayList<BadgeModel>();

        Badges.add(new BadgeModel("Badge vainqueur Web-Camp 2016", "http://commons.studyrama.com/data/bc2e/15029/logo_300x175.png"));

        return Badges;
    }

}

