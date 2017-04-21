package io.etna.intranet.Models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import io.etna.intranet.R;

import static io.etna.intranet.R.id.b1;


/**
 * Created by nextjoey on 12/04/2017.
 */
public class CustomAdapterNote extends ArrayAdapter<NoteModel> {

    Button button;

    //NoteModels est la liste des models à afficher
    public CustomAdapterNote(Context context, List<NoteModel> NoteModels) {
        super(context, 0, NoteModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_notes,parent, false);
        }

        NoteModelViewHolder viewHolder = (NoteModelViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new NoteModelViewHolder();
            viewHolder.UVnom = (TextView) convertView.findViewById(R.id.UVnom);
            viewHolder.UVdescription = (TextView) convertView.findViewById(R.id.UVdescription);
            viewHolder.projet = (TextView) convertView.findViewById(R.id.projet);
            viewHolder.commentaire = (TextView) convertView.findViewById(R.id.commentaire);
            viewHolder.note = (TextView) convertView.findViewById(R.id.note);
            viewHolder.noteMin = (TextView) convertView.findViewById(R.id.noteMin);
            viewHolder.noteMax = (TextView) convertView.findViewById(R.id.noteMax);
            viewHolder.noteMoy = (TextView) convertView.findViewById(R.id.noteMoy);
            viewHolder.mButton = (Button) convertView.findViewById(R.id.share);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<NoteModel> NoteModels
        NoteModel NoteModel = getItem(position);

        viewHolder.UVnom.setText(NoteModel.getUVNom());
        viewHolder.UVdescription.setText(NoteModel.getUVDescription());
        viewHolder.projet.setText(NoteModel.getProjet());
        viewHolder.commentaire.setText(NoteModel.getCommentaire());
        viewHolder.note.setText(NoteModel.getNote());
        viewHolder.noteMin.setText(NoteModel.getNoteMin());
        viewHolder.noteMax.setText(NoteModel.getNoteMax());
        viewHolder.noteMoy.setText(NoteModel.getNoteMoy());
        /*Partager note*/
        viewHolder.mButton.setOnClickListener(share);
        viewHolder.note.setOnClickListener(share);

        return convertView;
    }

    View.OnClickListener share = new View.OnClickListener() {
        public void onClick(View v) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Voici ma note du projet " + NoteModel.getProjet() + " ("+NoteModel.getUVNom() + ") : " + NoteModel.getNote() + ". @ETNA";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Partager ma note #ETNA");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            getContext().startActivity(Intent.createChooser(sharingIntent, "Partager via"));
        }
    };

    private class NoteModelViewHolder{
        public TextView UVnom;
        public TextView UVdescription;
        public TextView projet;
        public TextView note;
        public TextView noteMin;
        public TextView noteMax;
        public TextView noteMoy;
        public TextView commentaire;
        public Button mButton;
    }
}