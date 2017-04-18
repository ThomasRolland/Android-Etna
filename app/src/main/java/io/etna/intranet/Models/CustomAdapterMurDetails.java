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
public class CustomAdapterMurDetails extends ArrayAdapter<MurDetailsModel> {

    //MurDetailsModels est la liste des models à afficher
    public CustomAdapterMurDetails(Context context, List<MurDetailsModel> MurDetailsModels) {
        super(context, 0, MurDetailsModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_mur_message_details,parent, false);
        }

        MurDetailsModelViewHolder viewHolder = (MurDetailsModelViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MurDetailsModelViewHolder();
            viewHolder.createur = (TextView) convertView.findViewById(R.id.createur);
            viewHolder.dateCreation = (TextView) convertView.findViewById(R.id.dateCreation);
            viewHolder.message = (TextView) convertView.findViewById(R.id.message);


            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<MurDetailsModel> MurDetailsModels
        MurDetailsModel MurDetailsModel = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.createur.setText(MurDetailsModel.getCreateur());
        viewHolder.dateCreation.setText(MurDetailsModel.getDateCreation());
        viewHolder.message.setText(MurDetailsModel.getMessage());

        return convertView;
    }

    private class MurDetailsModelViewHolder{
        public TextView createur;
        public TextView dateCreation;
        public TextView message;
    }
}