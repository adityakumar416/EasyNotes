package com.example.easynotes.showAllNotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.easynotes.R;
import com.example.easynotes.addUpdateNotes.AddUpdateNotesActivity;
import com.example.easynotes.dataClass.Notes;
import com.example.easynotes.databinding.FragmentNotesBinding;
import com.example.easynotes.interfaces.NotesClickListener;
import com.example.easynotes.sqlDB.SqlHelper;
import com.example.easynotes.utils.MyHelper;
import com.example.easynotes.utils.RatingDialogFragment;
import com.example.easynotes.viewModel.NotesViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rejowan.cutetoast.CuteToast;

import java.util.ArrayList;


public class NotesFragment extends Fragment implements NotesClickListener {


    FragmentNotesBinding binding;
    NotesAdapter notesAdapter;
    ArrayList<Notes> notesList;
    SqlHelper sqlHelper;
    NotesViewModel notesViewModel;
    int count = 0;
    MyHelper myHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotesBinding.inflate(inflater, container, false);

        // initialize the SqlHelper class
        sqlHelper = new SqlHelper(getContext());

        // initialize the notesList
        notesList = new ArrayList<>();

        // initialize the notesViewModel
        notesViewModel = new ViewModelProvider(requireActivity()).get(NotesViewModel.class);

        // fetch notes to update ui
        notesList = sqlHelper.getNotes();

        myHelper = new MyHelper(getContext());

        // initialize the notesAdapter
        notesAdapter = new NotesAdapter(getContext(),myHelper.reverseListOrder(notesList), this,myHelper);
        // set recyclerview layout example - linear or grid
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        // set data in adapter
        binding.recyclerView.setAdapter(notesAdapter);

        return binding.getRoot();

    }

    // fetch notes to update ui
    private void fetchUser() {
        // get all notes
        notesList = sqlHelper.getNotes();
        notesAdapter.updateData(myHelper.reverseListOrder(notesList));
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // fetch notes for refresh note on update notes
        fetchUser();

        // filter note using search
        // this observe the current search text
        notesViewModel.getSearchNoteText().observe(requireActivity(), c -> {
            notesAdapter.getSearchFilter(c.toString());
            notesAdapter.notifyDataSetChanged();
        });

        // show RatingDialogFragment after 3 notes added
        if (count == 3) {
            RatingDialogFragment dialogFragment = new RatingDialogFragment();
            dialogFragment.show(requireActivity().getSupportFragmentManager(), "My Dialog");
        }

    }

    // update the note from the favorite fragment
    @Override
    public void updateNote(Notes notes) {
        Intent intent = new Intent(getActivity(), AddUpdateNotesActivity.class);
        intent.putExtra("isFev", notes.isFavorite());
        intent.putExtra("notes", notes);
        startActivity(intent);
    }

    // delete note on long press
    @Override
    public void longPressClickForDeleteNote(int notesId) {
        deleteNotes(notesId);
    }

    private void deleteNotes(int notesId) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Delete Notes")
                .setMessage("Are you sure to delete this notes.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // delete note
                        sqlHelper.deleteNote(notesId);
                        CuteToast.ct(getContext(), "Note Delete Successful.", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                        // refresh the notes after the delete notes
                        fetchUser();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss the dialog on cancel Button click
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // for change button color
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                //dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
            }
        });
        dialog.show();
    }

    // add note item click
    @Override
    public void addNoteClick() {
        Intent intent = new Intent(getContext(), AddUpdateNotesActivity.class);
        startActivity(intent);
        count++;
    }

}