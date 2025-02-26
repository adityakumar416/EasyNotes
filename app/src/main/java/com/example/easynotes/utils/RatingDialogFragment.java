package com.example.easynotes.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.easynotes.R;
import com.example.easynotes.databinding.FragmentRatingDialogBinding;
import com.example.easynotes.viewModel.NotesViewModel;
import com.rejowan.cutetoast.CuteToast;


public class RatingDialogFragment extends DialogFragment {
    FragmentRatingDialogBinding binding;
    NotesViewModel notesViewModel;
    MyHelper myHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRatingDialogBinding.inflate(inflater, container, false);

        // initialize the notesViewModel
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        // initialize the MyHelper
        myHelper = new MyHelper(getContext());

        // for do more corner radius in RatingDialog
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        // sharedPreferences for current theme
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        // get day and night mode theme
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", true);

        // if isDarkModeOn is true
        if (isDarkModeOn) {
            binding.image.setImageResource(R.drawable.smite_rating_day_icon);
        }
        // if isDarkModeOn is false
        else {
            binding.image.setImageResource(R.drawable.night_rating_image);
        }

        // if isDarkModeOn is true but observe in realtime on btn click
        notesViewModel.getTheme().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.image.setImageResource(R.drawable.night_rating_image);
            } else {
                binding.image.setImageResource(R.drawable.smite_rating_day_icon);
            }
        });

        // submitRating button click
        binding.submitRating.setOnClickListener(v -> {
            CuteToast.ct(getContext(), "Thank You For Rate Us..", CuteToast.LENGTH_SHORT, CuteToast.HAPPY, true).show();
            myHelper.rateUsOurApp();
            // dismiss dialog box
            dismiss();
        });

        // noThanks button click
        binding.noThanks.setOnClickListener(v -> {
            // dismiss dialog box
            dismiss();
        });

        return binding.getRoot();
    }


}