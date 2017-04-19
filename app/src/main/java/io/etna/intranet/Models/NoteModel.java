package io.etna.intranet.Models;

import com.google.zxing.common.StringUtils;

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
        String temp = new String();

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

    public String roundNote(String note) {
        if (note.length() > 4) {
            String temp = noteMax.substring(0, 4);
            return temp;
        }
        else {
            return note;
        }
    }
}
