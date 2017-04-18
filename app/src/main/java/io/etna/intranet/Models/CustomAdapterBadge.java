package io.etna.intranet.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import io.etna.intranet.R;


/**
 * Created by nextjoey on 12/04/2017.
 */
public class CustomAdapterBadge extends ArrayAdapter<BadgeModel> {

    //BadgeModels est la liste des models à afficher
    public CustomAdapterBadge(Context context, List<BadgeModel> BadgeModels) {
        super(context, 0, BadgeModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_badges,parent, false);
        }

        BadgeModelViewHolder viewHolder = (BadgeModelViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new BadgeModelViewHolder();
            viewHolder.nom = (TextView) convertView.findViewById(R.id.nom);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<BadgeModel> BadgeModels
        BadgeModel BadgeModel = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        assert BadgeModel != null;
        viewHolder.nom.setText(BadgeModel.getNom());
        Picasso.with(getContext()).load(BadgeModel.getImageURL()).into(viewHolder.imageView);
        {
            Log.d("A", "Erreur");
        }
        return convertView;
    }

    private class BadgeModelViewHolder{
        public TextView nom;
        public ImageView imageView;
    }


}