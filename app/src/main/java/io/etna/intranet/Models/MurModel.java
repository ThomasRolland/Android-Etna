package io.etna.intranet.Models;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class MurModel {
    String id;
    String createur;
    String titre;
    String dateCreation;
    String message;

    public MurModel(String id, String createur, String titre, String dateCreation, String message) {
        this.id = id;
        this.createur = createur;
        this.titre = titre;
        this.dateCreation = dateCreation;
        this.message = message;
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

}
