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
public class CustomAdapterTicket extends ArrayAdapter<TicketModel> {

    //TicketModels est la liste des models à afficher
    public CustomAdapterTicket(Context context, List<TicketModel> TicketModels) {
        super(context, 0, TicketModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_tickets,parent, false);
        }

        TicketModelViewHolder viewHolder = (TicketModelViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TicketModelViewHolder();
            viewHolder.ticketId = (TextView) convertView.findViewById(R.id.ticketId);
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.dateCreation = (TextView) convertView.findViewById(R.id.dateCreation);
            viewHolder.dateModification = (TextView) convertView.findViewById(R.id.dateModification);
            /*viewHolder.dateFermeture = (TextView) convertView.findViewById(R.id.dateFermeture);*/
            viewHolder.dernierEditeur = (TextView) convertView.findViewById(R.id.dernierEditeur);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<TicketModel> TicketModels
        TicketModel TicketModel = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        assert TicketModel != null;
        viewHolder.ticketId.setText(TicketModel.getTicketId());
        viewHolder.titre.setText(TicketModel.getTitre());
        viewHolder.dateCreation.setText(TicketModel.getDateCreation());
        viewHolder.dateModification.setText(TicketModel.getDateModification());
        /*viewHolder.dateFermeture.setText(TicketModel.getDateFermeture());*/
        viewHolder.dernierEditeur.setText(TicketModel.getDernierEditeur());

        return convertView;
    }

    private class TicketModelViewHolder{
        public TextView ticketId;
        public TextView titre;
        public TextView dateCreation;
        public TextView dateModification;
        public TextView dateFermeture;
        public TextView dernierEditeur;
    }
}