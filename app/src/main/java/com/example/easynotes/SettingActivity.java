package com.example.easynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.easynotes.databinding.ActivitySettingBinding;
import com.example.easynotes.utils.MyHelper;
import com.example.easynotes.utils.RatingDialogFragment;
import com.example.easynotes.viewModel.NotesViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    NotesViewModel notesViewModel;
    boolean isDarkModeOn;
    MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize the MyHelper
        myHelper = new MyHelper(this);

        // initialize the notesViewModel
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        // sharedPreferences for current theme
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();

        // get day and night mode theme
        isDarkModeOn = sharedPreferences.getBoolean("isDayModeOn", true);

        // select switch button theme mode
        binding.appTheme.setChecked(!isDarkModeOn);

        // if isDarkModeOn is true but observe in realtime on btn click
        notesViewModel.getTheme().observe(this, aBoolean -> {
            if (aBoolean) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        // theme change on switch btn click
        binding.appTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // save day and night mode theme
                editor.putBoolean("isDayModeOn", false);
                notesViewModel.selectTheme(false);
                editor.apply();

            } else {
                // save day and night mode theme
                editor.putBoolean("isDayModeOn", true);
                notesViewModel.selectTheme(true);
                editor.apply();

            }
        });

        // go back on btn click
        binding.backIc.setOnClickListener(v -> {
            finish();
        });

        // clearAll shared preference data on btn click
        binding.clearAllData.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                    .setTitle("Reset Settings")
                    .setMessage("Only App setting reset. Your Notes will Safe.")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences sharedPreferences1 = getSharedPreferences("NOTES", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.clear();
                            editor1.apply();
                            editor.clear();
                            editor.apply();
                            // restart the app
                            restart();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // dismiss the dialog
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
                }
            });
            dialog.show();
        });

        // contact us
        binding.contacts.setOnClickListener(v -> {
            myHelper.dialContactPhone("8979116063");
        });

        // rate us the app
        binding.rateUs.setOnClickListener(v -> {
            RatingDialogFragment dialogFragment = new RatingDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "My Dialog");
        });

        // followUs on instagram
        binding.followUs.setOnClickListener(v -> {
            myHelper.goToInstagram();
        });

        // share the app
        binding.shareApp.setOnClickListener(v -> {
            myHelper.shareApp();
        });

        // app version
        try {
            appVersion();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    // get current app version
    private void appVersion() throws PackageManager.NameNotFoundException {
        PackageManager manager = getPackageManager();
        PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
        String version = info.versionName;
        binding.appVersion.setText("Version : " + version);
    }

    // restart the app
    public void restart() {
        Intent intent = new Intent(this, OnBoardingScreenActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }

}

