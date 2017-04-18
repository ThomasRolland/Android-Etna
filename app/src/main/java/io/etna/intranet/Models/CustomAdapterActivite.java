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
public class CustomAdapterActivite extends ArrayAdapter<ActiviteModel> {

    //ActiviteModels est la liste des models à afficher
    public CustomAdapterActivite(Context context, List<ActiviteModel> ActiviteModels) {
        super(context, 0, ActiviteModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_activite,parent, false);
        }

        ActiviteModelViewHolder viewHolder = (ActiviteModelViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ActiviteModelViewHolder();
            viewHolder.code = (TextView) convertView.findViewById(R.id.code);
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.dateFin = (TextView) convertView.findViewById(R.id.dateFin);
            viewHolder.coursStr = (TextView) convertView.findViewById(R.id.coursStr);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<ActiviteModel> ActiviteModels
        ActiviteModel ActiviteModel = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.code.setText(ActiviteModel.getCode());
        viewHolder.titre.setText(ActiviteModel.getTitre());
        viewHolder.dateFin.setText(ActiviteModel.getDateFin());
        viewHolder.coursStr.setText(ActiviteModel.getCours().toString());

        return convertView;
    }

    private class ActiviteModelViewHolder{
        public TextView code;
        public TextView titre;
        public TextView dateFin;
        public TextView coursStr;
    }
}