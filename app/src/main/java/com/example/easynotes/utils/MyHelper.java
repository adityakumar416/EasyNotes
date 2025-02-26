package com.example.easynotes.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.easynotes.dataClass.Notes;

import java.util.ArrayList;


public class MyHelper {

    Context context;

    public MyHelper(Context context) {
        this.context = context;
    }

    // filter all Favorite notes
    public ArrayList<Notes> filterAllFavoriteNote(ArrayList<Notes> notesList) {
        for (int i = notesList.size() - 1; i >= 0; i--) {
            if (!notesList.get(i).isFavorite()) {
                notesList.remove(i);  // Remove the non-favorite note
            }
        }
        return notesList;
    }

    // reverse string for show last added value to show in first
    public ArrayList<Notes> reverseListOrder(ArrayList<Notes> notes) {
        ArrayList<Notes> notesArrayList = new ArrayList<>();

        int size = notes.size() - 1;
        int i = 0;

        while (size >= i) {
            notesArrayList.add(notes.get(size));
            size--;
        }
        return notesArrayList;
    }

    // rate us app
    void rateUsOurApp() {
        try {
            Uri marketUri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            context.startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            context.startActivity(marketIntent);
        }
    }

    // share app
    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "\nEasy Notes\nLet me recommend you this application for write the notes easily.\n\n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "Choose an app to share with"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    // follow on instagram
    public void goToInstagram() {
        Uri uri = Uri.parse("http://instagram.com/_u/aditya_kumar4");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");
        try {
            context.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/aditya_kumar4")));
        }
    }

    // contact us
    public void dialContactPhone(String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    // get months
    public String getMonths(String month) {
        switch (month) {
            case "1":
                return "January";
            case "2":
                return "February";
            case "3":
                return "March";
            case "4":
                return "April";
            case "5":
                return "May";
            case "6":
                return "June";
            case "7":
                return "July";
            case "8":
                return "August";
            case "9":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return null;
        }
    }

    // get months from months number
    public String getMonthsWithShortName(String month) {
        switch (month) {
            case "1":
                return "Jan";
            case "2":
                return "Fev";
            case "3":
                return "Mar";
            case "4":
                return "Apr";
            case "5":
                return "May";
            case "6":
                return "Jun";
            case "7":
                return "Jul";
            case "8":
                return "Aug";
            case "9":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return null;
        }
    }


}
