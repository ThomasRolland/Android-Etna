package io.etna.intranet.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.etna.intranet.R;


/**
 * Created by nextjoey on 12/04/2017.
 */
public class CustomAdapterEvent extends ArrayAdapter<EventModel> {

    //EventModels est la liste des models à afficher
    public CustomAdapterEvent(Context context, List<EventModel> EventModels) {
        super(context, 0, EventModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_planning,parent, false);
        }

        EventModelViewHolder viewHolder = (EventModelViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new EventModelViewHolder();
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.location = (TextView) convertView.findViewById(R.id.location);
            viewHolder.dateDebut = (TextView) convertView.findViewById(R.id.dateDebut);
            viewHolder.dateFin = (TextView) convertView.findViewById(R.id.dateFin);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<EventModel> EventModels
        EventModel EventModel = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        assert EventModel != null;
        viewHolder.titre.setText(EventModel.getTitre());
        viewHolder.location.setText(EventModel.getLocation());
        viewHolder.dateDebut.setText(EventModel.getDateDebut());
        viewHolder.dateFin.setText(EventModel.getDateFin());

        return convertView;
    }

    private class EventModelViewHolder{
        public TextView titre;
        public TextView location;
        public TextView dateDebut;
        public TextView dateFin;
    }
}