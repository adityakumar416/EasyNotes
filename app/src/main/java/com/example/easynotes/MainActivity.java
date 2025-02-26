package com.example.easynotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.easynotes.databinding.ActivityMainBinding;
import com.example.easynotes.favoriteNotes.FavoriteNoteFragment;
import com.example.easynotes.showAllNotes.NotesFragment;
import com.example.easynotes.viewModel.NotesViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public int selectedTabNumber = 1;

    NotesViewModel notesViewModel;
    boolean isDayModeOn;
    TextView selectedTextView;
    TextView nonSelectedTab1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize the notesViewModel
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        // sharedPreferences for current theme
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        // get day and night mode theme
        isDayModeOn = sharedPreferences.getBoolean("isDayModeOn", true);

        // if isDayModeOn is true
        if (isDayModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // if isDayModeOn is false
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // open setting on btn click
        binding.setting.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        });

        // first show the NotesFragment
        replaceFragment(new NotesFragment());

        // when user click on note tab
        binding.noteTab.setOnClickListener(v -> {
            selectedTab(1);
        });

        // when user click on favoriteNotesTab
        binding.favoriteNotesTab.setOnClickListener(v -> {
            selectedTab(2);
        });


        // Add Text Change Listener to EditText
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesViewModel.setSearchNoteText(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    // show fragment according user select
    private void selectedTab(int tabNumber) {

        Typeface font = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            font = getResources().getFont(R.font.jua_regular);
        }

        if (tabNumber == 1) {
            selectedTextView = binding.noteTab;
            nonSelectedTab1 = binding.favoriteNotesTab;

            replaceFragment(new NotesFragment());
        } else {

            selectedTextView = binding.favoriteNotesTab;
            nonSelectedTab1 = binding.noteTab;

            replaceFragment(new FavoriteNoteFragment());

        }


        float slideTo = (tabNumber - selectedTabNumber) * selectedTextView.getWidth();
        TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
        translateAnimation.setDuration(100);

        if (selectedTabNumber == 1) {
            binding.noteTab.startAnimation(translateAnimation);
        } else {
            binding.favoriteNotesTab.startAnimation(translateAnimation);
        }

        Typeface finalFont = font;
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Apply styles to selected tab
                selectedTextView.setBackgroundResource(R.drawable.tabs_item_selector);
                selectedTextView.setTypeface(finalFont, Typeface.BOLD);
                selectedTextView.setTextColor(getResources().getColor(R.color.white));

                // Reset styles for non-selected tab
                nonSelectedTab1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                nonSelectedTab1.setTextColor(getResources().getColor(R.color.black));
                nonSelectedTab1.setTypeface(finalFont, Typeface.NORMAL);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        selectedTabNumber = tabNumber;
    }

    // replace the fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


}
