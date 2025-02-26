package com.example.easynotes.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotesViewModel extends ViewModel {

    private final MutableLiveData<CharSequence> searchNoteText = new MutableLiveData<>();

    public void setSearchNoteText(CharSequence item) {
        searchNoteText.setValue(item);
    }

    public LiveData<CharSequence> getSearchNoteText() {
        return searchNoteText;
    }


    private final MutableLiveData<Boolean> theme = new MutableLiveData<>();

    public void selectTheme(Boolean item) {
        theme.setValue(item);
    }

    public LiveData<Boolean> getTheme() {
        return theme;
    }


}
