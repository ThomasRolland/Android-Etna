package io.etna.intranet.Models;

import com.google.zxing.common.StringUtils;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class NoteModel {

    static String UVnom;
    String UVdescription;
    static String projet;
    String commentaire;
    static String note;
    String noteMin;
    String noteMax;
    String noteMoy;
    Boolean validation;

    public NoteModel(String UVnom, String UVdescription, String projet, String commentaire, String note, String noteMin, String noteMax, String noteMoy, Boolean validation) {
        this.UVnom = UVnom;
        this.UVdescription = UVdescription;
        this.projet = projet;
        this.commentaire = commentaire;
        this.note = note;
        this.noteMin = noteMin;
        this.noteMax = noteMax;
        this.noteMoy = noteMoy;
        this.validation = validation;
    }

    public static String getUVNom() {
        return UVnom;
    }
    public String getUVDescription() {
        return UVdescription;
    }
    public static String getProjet() {
        return projet;
    }
    public String getCommentaire() {
        return commentaire;
    }
    public static String getNote() {
        return roundNote(note);

    }
    public String getNoteMin() {
        return "Min: "+roundNote(noteMin);
    }
    public String getNoteMax() {
        return "Max: "+roundNote(noteMax);

    }
    public String getNoteMoy() {
        return "Moy: "+roundNote(noteMoy);

    }
    public Boolean getValidation() { return validation; }

    public static String roundNote(String note) {
        if (note.length() > 4) {
            String temp = note.substring(0, 4);
            return temp + "/20";
        }
        else {
            return note + "/20";
        }
    }
}
