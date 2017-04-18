package io.etna.intranet.Models;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class TicketModel {

    String ticketId;
    String titre;
    String dateCreation;
    String dateModification;
    String dateFermeture;
    String dernierEditeur;


    public TicketModel(String ticketId, String titre, String dateCreation, String dateModification, String dateFermeture, String dernierEditeur) {
        this.ticketId = ticketId;
        this.titre = titre;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.dateFermeture = dateFermeture;
        this.dernierEditeur = dernierEditeur;
    }

    public String getTicketId() {
        return ticketId;
    }
    public String getTitre() {
        return titre;
    }
    public String getDateCreation() {
        return dateCreation;
    }
    public String getDateModification() {
        return dateModification;
    }
    public String getDateFermeture() {
        return dateFermeture;
    }
    public String getDernierEditeur() {
        return dernierEditeur;
    }
}
