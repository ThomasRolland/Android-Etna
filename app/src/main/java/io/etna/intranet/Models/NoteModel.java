package io.etna.intranet.Models;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class NoteModel {

    String UVnom;
    String UVdescription;
    String projet;
    String commentaire;
    String note;
    String noteMin;
    String noteMax;
    String noteMoy;
    Boolean validation;

    public NoteModel(String UVnom, String UVdescription, String projet, String commentaire, String note, String noteMin, String noteMax, String noteMoy, Boolean validation) {
        this.UVnom = UVnom;
        this.UVdescription = UVdescription;
        this.projet = projet;
        this.commentaire = commentaire;
        this.note = note + "/20";
        this.noteMin = noteMin;
        this.noteMax = noteMax;
        this.noteMoy = noteMoy;
        this.validation = validation;
    }

    public String getUVNom() {
        return UVnom;
    }
    public String getUVDescription() {
        return UVdescription;
    }
    public String getProjet() {
        return projet;
    }
    public String getCommentaire() {
        return commentaire;
    }
    public String getNote() {
        return note;
    }
    public String getNoteMin() {
        return noteMin;
    }
    public String getNoteMax() {
        return noteMax;
    }
    public String getNoteMoy() {
        return noteMoy;
    }
    public Boolean getValidation() { return validation; }
}
