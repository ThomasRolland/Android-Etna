package io.etna.intranet.Models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nextjoey on 12/04/2017.
 */

public class ActiviteModel {

    String code;
    String titre;
    String dateFin;
    ArrayList<String> cours;

    public ActiviteModel(String code, String titre, String dateFin, ArrayList<String> cours) {
        this.code = code;
        this.titre = titre;
        this.dateFin = dateFin;
        this.cours = cours;
    }


    public String getCode() {
        return code;
    }

    public String getTitre() {
        return titre;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getCours() {
        String res = new String();
        for (int i = 0; i < cours.size(); i++) {
            res += cours.get(i) + System.getProperty("line.separator");
        }
        return res;
    }
}
