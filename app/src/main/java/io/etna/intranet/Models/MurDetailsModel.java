package io.etna.intranet.Models;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class MurDetailsModel {
    String id;
    String createur;
    String dateCreation;
    String message;

    public MurDetailsModel(String id, String createur, String dateCreation, String message) {
        this.id = id;
        this.createur = createur;
        this.dateCreation = dateCreation;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getCreateur() {
        return createur;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getMessage() {
        return message;
    }

}
