package io.etna.intranet.Models;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class EventModel {

    String titre;
    String location;
    String dateDebut;
    String dateFin;


    public EventModel(String titre, String location, String dateDebut, String dateFin) {
        this.titre = titre;
        this.location = location;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getTitre() {
        return titre;
    }
    public String getLocation() {
        return location;
    }
    public String getDateDebut() {
        return dateDebut;
    }
    public String getDateFin() {
        return dateFin;
    }
}
