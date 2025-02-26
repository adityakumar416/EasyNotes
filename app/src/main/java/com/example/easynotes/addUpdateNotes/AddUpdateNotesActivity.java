package com.example.easynotes.addUpdateNotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easynotes.R;
import com.example.easynotes.dataClass.Notes;
import com.example.easynotes.databinding.ActivityAddNotesBinding;
import com.example.easynotes.sqlDB.SqlHelper;
import com.example.easynotes.utils.MyHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rejowan.cutetoast.CuteToast;

import java.util.Calendar;
import java.util.Objects;

public class AddUpdateNotesActivity extends AppCompatActivity {


    ActivityAddNotesBinding binding;
    SqlHelper sqlHelper;
    int noteId;
    boolean isFavoriteNote = false;
    MyHelper myHelper;
    String currentHour, date, month, year, currentMinute;
    Intent intent;
    Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize the MyHelper class to get method
        myHelper = new MyHelper(this);

        // initialize the SqlHelper class to get method to create and update the notes
        sqlHelper = new SqlHelper(this);

        // initialize the Calendar to time, date, year, and etc.
        Calendar calendar = Calendar.getInstance();

        currentHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        currentMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        //int second = calendar.get(Calendar.SECOND);
        date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        year = String.valueOf(calendar.get(Calendar.YEAR));


        // get intent for get the update note details
        intent = getIntent();

        // get boolean value for selected note isFavoriteNote or not
        isFavoriteNote = intent.getBooleanExtra("isFev", false);

        // if isFavoriteNote true show yellow star icon else show outline star icon
        if (isFavoriteNote) {
            binding.favoriteNoteIcon.setImageResource(R.drawable.star);
        } else {
            binding.favoriteNoteIcon.setImageResource(R.drawable.outline_star_icon);
        }


        // here we are check that notes key available or not in intent
        if (intent.hasExtra("notes")) {
            // update notes
            notes = intent.getParcelableExtra("notes");
            if (notes != null) {
                updateNotes(notes, currentHour, currentMinute, date, month, year);
            }
        } else {
            // new note
            saveNewNotes(month, date, year, currentHour, currentMinute);
        }

        // delete note on delete btn click
        binding.deleteNoteIcon.setOnClickListener(v -> {
            deleteNote();
        });

        // go back on backIcon click
        binding.backIcon.setOnClickListener(v -> {
            finish();
        });

    }

    // show delete dialog box for notify user
    private void deleteNote() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Delete Notes")
                .setMessage("Are you sure to delete this notes.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // delete note method from sqlHelper class
                        sqlHelper.deleteNote(noteId);
                        CuteToast.ct(AddUpdateNotesActivity.this, "Note Delete Successful.", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // when user click on cancel button
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // this code change the color for positive and negative button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            }
        });
        dialog.show();
    }

    private void updateNotes(Notes notes, String currentHour, String currentMinute, String date, String month, String year) {
        // button for favorite and unfavorite the note
        binding.favoriteNoteIcon.setOnClickListener(v -> {
            if (isFavoriteNote) {
                binding.favoriteNoteIcon.setImageResource(R.drawable.outline_star_icon);
                sqlHelper.setFavoriteNote("false", notes.getId());
                isFavoriteNote = false;
            } else {
                binding.favoriteNoteIcon.setImageResource(R.drawable.star);
                isFavoriteNote = true;
                sqlHelper.setFavoriteNote("true", notes.getId());
            }

        });

        // show deleteNoteIcon only for update user
        binding.deleteNoteIcon.setVisibility(View.VISIBLE);
        noteId = notes.getId();
        binding.note.setText(notes.getNote());
        binding.title.setText(notes.getTitle());
        binding.date.setText(myHelper.getMonths(notes.getMonth()) + " " + notes.getDate() + ", " + notes.getYear());

        // save note
        binding.saveNotes.setOnClickListener(v -> {
            String title = binding.title.getText().toString().trim();
            String note = binding.note.getText().toString().trim();
            // check title is empty or not
            if (title.isEmpty()) {
                binding.title.requestFocus();
                CuteToast.ct(this, "Write Something in Title.", CuteToast.LENGTH_SHORT, CuteToast.INFO, true).show();
            }
            // check note is empty or not
            else if (note.isEmpty()) {
                binding.note.requestFocus();
                CuteToast.ct(this, "Write Something in Note.", CuteToast.LENGTH_SHORT, CuteToast.INFO, true).show();
            } else {
                // update note
                sqlHelper.updateNotes(notes.getId(), title, note, currentHour, currentMinute, date, month, year);
                CuteToast.ct(this, "Note Update Successfully", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                finish();
            }
        });
    }


    // save new note
    private void saveNewNotes(String month, String date, String year, String currentHour, String currentMinute) {
        binding.date.setText(myHelper.getMonths(month) + " " + date + ", " + year);
        binding.deleteNoteIcon.setVisibility(View.GONE);
        binding.favoriteNoteIcon.setVisibility(View.GONE);
        binding.saveNotes.setOnClickListener(v -> {
            String title = binding.title.getText().toString().trim();
            String note = binding.note.getText().toString().trim();
            if (title.isEmpty()) {
                binding.title.requestFocus();
                CuteToast.ct(this, "Write Something in Title.", CuteToast.LENGTH_SHORT, CuteToast.INFO, true).show();
            } else if (note.isEmpty()) {
                binding.note.requestFocus();
                CuteToast.ct(this, "Write Something in Note.", CuteToast.LENGTH_SHORT, CuteToast.INFO, true).show();
            } else {
                sqlHelper.addNotes(title, note, currentHour, currentMinute, date, month, year);
                CuteToast.ct(this, "Note Save Successfully.", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                finish();
            }

        });
    }


    // save note on back press
    @Override
    public void onBackPressed() {
        String title = binding.title.getText().toString().trim();
        String note = binding.note.getText().toString().trim();

        // Check if the title and note are both empty, in this case just finish the activity
        if (title.isEmpty() && note.isEmpty()) {
            finish();
        } else if (intent.hasExtra("notes")) {
            if (Objects.equals(notes.getTitle(), title) && Objects.equals(notes.getNote(), note)) {
                // No changes, just finish the activity
              //  finish();
                AddUpdateNotesActivity.super.onBackPressed();

            } else {
                // There are new changes, ask the user if they want to save
                showSaveChangesDialog();
            }
        } else {
            // No existing note data, just check if both fields are filled and save
            showSaveChangesDialog();
        }
    }

    private void showSaveChangesDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Save Notes")
                .setMessage("Do you want to save the changes to your notes?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // If the title or note is empty, show a message and return
                        String title = binding.title.getText().toString().trim();
                        String note = binding.note.getText().toString().trim();

                        if (title.isEmpty()) {
                            binding.title.requestFocus();
                            CuteToast.ct(AddUpdateNotesActivity.this, "Write Something in Title.", CuteToast.LENGTH_SHORT, CuteToast.INFO, true).show();
                        } else if (note.isEmpty()) {
                            binding.note.requestFocus();
                            CuteToast.ct(AddUpdateNotesActivity.this, "Write Something in Note.", CuteToast.LENGTH_SHORT, CuteToast.INFO, true).show();
                        } else {
                            // Save or update the note
                            if (intent.hasExtra("notes")) {
                                // If the note is being updated
                                sqlHelper.updateNotes(notes.getId(), title, note, currentHour, currentMinute, date, month, year);
                            } else {
                                // If it's a new note
                                sqlHelper.addNotes(title, note, currentHour, currentMinute, date, month, year);
                            }
                            dialogInterface.dismiss();
                            AddUpdateNotesActivity.super.onBackPressed();
                            //finish(); // Finish the activity after saving the note
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // when user click on cancel button
                        dialogInterface.dismiss();
                        AddUpdateNotesActivity.super.onBackPressed();
                        // finish(); // Finish the activity if cancel is pressed
                    }
                });

        AlertDialog dialog = builder.create();
        // this code change the color for positive and negative button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            }
        });
        dialog.show();
    }


}