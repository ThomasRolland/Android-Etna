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
public class CustomAdapterMur extends ArrayAdapter<MurModel> {

    //MurModels est la liste des models à afficher
    public CustomAdapterMur(Context context, List<MurModel> MurModels) {
        super(context, 0, MurModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_mur_message,parent, false);
        }

        MurModelViewHolder viewHolder = (MurModelViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MurModelViewHolder();
            viewHolder.createur = (TextView) convertView.findViewById(R.id.createur);
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.dateCreation = (TextView) convertView.findViewById(R.id.dateCreation);
            viewHolder.message = (TextView) convertView.findViewById(R.id.message);


            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<MurModel> MurModels
        MurModel MurModel = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.createur.setText(MurModel.getCreateur());
        viewHolder.titre.setText(MurModel.getTitre());
        viewHolder.dateCreation.setText(MurModel.getDateCreation());
        viewHolder.message.setText(MurModel.getMessage());

        return convertView;
    }

    private class MurModelViewHolder{
        public TextView createur;
        public TextView titre;
        public TextView dateCreation;
        public TextView message;
    }
}