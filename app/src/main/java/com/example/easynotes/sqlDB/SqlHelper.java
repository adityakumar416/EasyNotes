package com.example.easynotes.sqlDB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.easynotes.dataClass.Notes;

import java.util.ArrayList;

public class SqlHelper extends SQLiteOpenHelper {

    // database name
    private static final String DATABASE_NAME = "NOTE";
    // table name
    private static final String TABLE_NAME = "my_notes";
    // current version
    private static final int DATABASE_VERSION = 1;

    private static final String HOUR = "hour";
    private static final String MINUTES = "minutes";
    private static final String DATE = "date";
    private static final String MONTHS = "months";
    private static final String YEAR = "year";
    private static final String ID = "id";
    private static final String NOTES = "note";
    private static final String NOTES_TITLE = "title";
    private static final String IS_FAVORITE_NOTE = "isFavoriteNote";

    public SqlHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // query for create the table
        String SQL_QUERY = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY," + NOTES_TITLE + " TEXT," + NOTES + " TEXT," + IS_FAVORITE_NOTE + " TEXT, " + HOUR + " TEXT," + MINUTES + " TEXT," + DATE + " TEXT," + MONTHS + " TEXT," + YEAR + " TEXT )";
        db.execSQL(SQL_QUERY);
    }

    // Called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // save note in sql db
    public void addNotes(String title, String note, String currentHour, String currentMinute, String date, String month, String year) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_FAVORITE_NOTE, "false");
        contentValues.put(NOTES_TITLE, title);
        contentValues.put(NOTES, note);
        contentValues.put(HOUR, currentHour);
        contentValues.put(MINUTES, currentMinute);
        contentValues.put(DATE, date);
        contentValues.put(MONTHS, month);
        contentValues.put(YEAR, year);

        database.insert(TABLE_NAME, null, contentValues);
    }

    // get data from sql db
    public ArrayList<Notes> getNotes() {

        ArrayList<Notes> list = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();

        // cursor is a interface. cursor is act as a pointer that allow you to iterate over the row of data
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // move to the first note
        if (cursor.moveToFirst()) {
            // get all note using moveToNext()
            do {
                String hour = cursor.getString(cursor.getColumnIndexOrThrow(HOUR));
                String minutes = cursor.getString(cursor.getColumnIndexOrThrow(MINUTES));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                String months = cursor.getString(cursor.getColumnIndexOrThrow(MONTHS));
                String year = cursor.getString(cursor.getColumnIndexOrThrow(YEAR));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow(NOTES));
                String notes_title = cursor.getString(cursor.getColumnIndexOrThrow(NOTES_TITLE));
                String isFavoriteNote = cursor.getString(cursor.getColumnIndexOrThrow(IS_FAVORITE_NOTE));

                list.add(new Notes(id, notes, notes_title, isFavoriteNote(isFavoriteNote), hour, minutes, date, months, year));
            } while (cursor.moveToNext());

        }
        // after fetch the note add in this list that will return
        return list;
    }

    // here we are convert the isFavoriteNote string to boolean value
    boolean isFavoriteNote(String str) {
        if (str.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    // update  note
    public void updateNotes(int id, String title, String note, String currentHour, String currentMinute, String date, String month, String year) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_TITLE, title);
        contentValues.put(NOTES, note);
        contentValues.put(HOUR, currentHour);
        contentValues.put(MINUTES, currentMinute);
        contentValues.put(DATE, date);
        contentValues.put(MONTHS, month);
        contentValues.put(YEAR, year);
        database.update(TABLE_NAME, contentValues, ID + " = ?", new String[]{String.valueOf(id)});
    }

    // delete note
    public void deleteNote(int notesId) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, ID + "=?", new String[]{String.valueOf(notesId)});
    }

    // change favoriteNote preference according to the user and change in the database
    public void setFavoriteNote(String b, int id) {
        SQLiteDatabase database = getWritableDatabase();
        // query for update only the IS_FAVORITE_NOTE
        database.execSQL("UPDATE " + TABLE_NAME + " SET " + IS_FAVORITE_NOTE + "=? " + " WHERE " + ID + "=?", new String[]{String.valueOf(b), String.valueOf(id)});
    }


}
