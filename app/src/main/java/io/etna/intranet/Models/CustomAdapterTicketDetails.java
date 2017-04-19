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
public class CustomAdapterTicketDetails extends ArrayAdapter<TicketDetailsModel> {

    //TicketDetailsModels est la liste des models à afficher
    public CustomAdapterTicketDetails(Context context, List<TicketDetailsModel> TicketDetailsModels) {
        super(context, 0, TicketDetailsModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_tickets_details,parent, false);
        }

        TicketDetailsModelViewHolder viewHolder = (TicketDetailsModelViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TicketDetailsModelViewHolder();
            viewHolder.message = (TextView) convertView.findViewById(R.id.message);
            viewHolder.dateCreation = (TextView) convertView.findViewById(R.id.dateCreation);
            viewHolder.authorLogin = (TextView) convertView.findViewById(R.id.authorLogin);
            viewHolder.authorMail = (TextView) convertView.findViewById(R.id.authorMail);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<TicketDetailsModel> TicketDetailsModels
        TicketDetailsModel TicketDetailsModel = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        assert TicketDetailsModel != null;
        viewHolder.message.setText(TicketDetailsModel.getMessage());
        viewHolder.dateCreation.setText(TicketDetailsModel.getDateCreation());
        viewHolder.authorLogin.setText(TicketDetailsModel.getAuthorLogin());
        viewHolder.authorMail.setText(TicketDetailsModel.getAuthorMail());

        return convertView;
    }

    private class TicketDetailsModelViewHolder{
        public TextView message;
        public TextView dateCreation;
        public TextView authorLogin;
        public TextView authorMail;

    }
}