package io.etna.intranet.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.etna.intranet.R;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class BadgeModel {

    String nom;
    String imageURL;

    public BadgeModel(String nom, String imageURL) {
        this.nom = nom;
        this.imageURL = imageURL;
    }

    public String getNom() {
        return nom;
    }

    public String getImageURL() {
        String replacedStr = imageURL.replace("\\", "");
        //Log.d("url : ", "https://achievements.etna-alternance.net"+replacedStr) ;
        return "https://achievements.etna-alternance.net"+replacedStr;
    }


}
