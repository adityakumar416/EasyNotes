package com.example.easynotes.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Notes implements Parcelable {

    int id;
    String note;
    String title;
    boolean favorite;
    String currentHour;
    String currentMinute;
    String date;
    String month;
    String year;

    public Notes(int id, String note, String title, boolean favorite, String currentHour, String currentMinute, String date, String month, String year) {
        this.id = id;
        this.note = note;
        this.title = title;
        this.favorite = favorite;
        this.currentHour = currentHour;
        this.currentMinute = currentMinute;
        this.date = date;
        this.month = month;
        this.year = year;
    }


    protected Notes(Parcel in) {
        id = in.readInt();
        note = in.readString();
        title = in.readString();
        favorite = in.readByte() != 0;
        currentHour = in.readString();
        currentMinute = in.readString();
        date = in.readString();
        month = in.readString();
        year = in.readString();
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(note);
        dest.writeString(title);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeString(currentHour);
        dest.writeString(currentMinute);
        dest.writeString(date);
        dest.writeString(month);
        dest.writeString(year);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public boolean isFavorite() {
        return favorite;
    }

}
