package io.etna.intranet.Models;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class TicketDetailsModel {

    String message;
    String dateCreation;
    String authorLogin;
    String authorMail;


    public TicketDetailsModel(String message, String dateCreation, String authorLogin, String authorMail) {
        this.message = message;
        this.dateCreation = dateCreation;
        this.authorLogin = authorLogin;
        this.authorMail = authorMail;
    }

    public String getMessage() {
        return message;
    }
    public String getDateCreation() {
        return dateCreation;
    }
    public String getAuthorLogin() {
        return authorLogin;
    }
    public String getAuthorMail() {
        return authorMail;
    }

}
