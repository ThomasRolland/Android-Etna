package io.etna.intranet.Models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.etna.intranet.Badges;
import io.etna.intranet.ImageDownloader.CookieImageDownloader;
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
        BadgeModel badge = getItem(position);

        TextView text = (TextView) convertView.findViewById(R.id.nom);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        text.setText(badge.getNom());


            Picasso picasso = new Picasso.Builder(getContext()).downloader(new CookieImageDownloader(getContext())).build();
            Picasso.with(getContext()).load(badge.getImageURL()).into(image);
        {
            Log.d("Impossible de récuperer l'image : ", badge.getImageURL());
        }


        return convertView;
    }
}