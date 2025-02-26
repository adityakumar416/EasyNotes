package com.example.easynotes.favoriteNotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easynotes.R;
import com.example.easynotes.dataClass.Notes;
import com.example.easynotes.interfaces.NotesClickListener;
import com.example.easynotes.utils.MyHelper;

import java.util.ArrayList;

public class FavoriteNotesAdapter extends RecyclerView.Adapter<FavoriteNotesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Notes> notesList;
    private ArrayList<Notes> notesFilterList;
    private NotesClickListener clickListener;
    private MyHelper myHelper;

    public FavoriteNotesAdapter(Context context, ArrayList<Notes> notesList, NotesClickListener clickListener, MyHelper myHelper) {
        this.context = context;
        this.notesList = notesList;
        this.notesFilterList = notesList;
        this.clickListener = clickListener;
        this.myHelper = myHelper;
    }

    // update the list after change
    void updateData(ArrayList<Notes> newNotesList){
        notesList.clear();
        notesList.addAll(newNotesList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView add_image;
        CardView add_note_card;
        CardView note_card;
        TextView title;
        TextView note;
        TextView date;

        // find id for perform action on views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            add_image = itemView.findViewById(R.id.add_note_icon);
            add_note_card = itemView.findViewById(R.id.add_note_card);
            note_card = itemView.findViewById(R.id.note_card);
            title = itemView.findViewById(R.id.title);
            note = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }
    }

    // show a item in ui
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notes_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get all notes and display in ui
        Notes notes = notesFilterList.get(position);
        holder.title.setText(notes.getTitle());
        holder.note.setText(notes.getNote());
        holder.date.setText(myHelper.getMonthsWithShortName(notes.getMonth()) + " " + notes.getDate() + ", " + notes.getYear());

        // note click for update
        holder.note_card.setOnClickListener(v -> {
            clickListener.updateNote(notes);
        });

        // long press for delete note
        holder.note_card.setOnLongClickListener(v -> {
            clickListener.longPressClickForDeleteNote(notes.getId());
            return true;
        });

    }

    // filter the notes according search text
    ArrayList<Notes> getFilter(String string){
        notesFilterList = new ArrayList<>();
        for(Notes notes: notesList){
            if (notes.getTitle().toLowerCase().contains(string.toLowerCase())
                    || notes.getNote().toLowerCase().contains(string.toLowerCase())
                    || notes.getDate().toLowerCase().contains(string.toLowerCase())
                    || notes.getMonth().toLowerCase().contains(string.toLowerCase())
                    || notes.getYear().toLowerCase().contains(string.toLowerCase())) {
                notesFilterList.add(notes);
            }
        }
        return notesFilterList;
    }


    // size of list
    @Override
    public int getItemCount() {
        return notesFilterList.size();
    }


}
