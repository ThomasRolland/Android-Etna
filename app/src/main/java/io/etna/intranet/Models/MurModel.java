package io.etna.intranet.Models;

import android.graphics.Bitmap;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class MurModel {
    String id;
    String createur;
    String titre;
    String dateCreation;
    String message;
    Bitmap photo;

    public MurModel(String id, String createur, String titre, String dateCreation, String message, Bitmap photo) {
        this.id = id;
        this.createur = createur;
        this.titre = titre;
        this.dateCreation = dateCreation;
        this.message = message;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getCreateur() {
        return createur;
    }

    public String getTitre() {
        return titre;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getMessage() {
        return message;
    }
    public Bitmap getPhoto() {
        return photo;
    }

}
