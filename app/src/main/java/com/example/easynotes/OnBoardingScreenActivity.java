package com.example.easynotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.easynotes.databinding.ActivityOnBoardingScreenBinding;

public class OnBoardingScreenActivity extends AppCompatActivity {

    ActivityOnBoardingScreenBinding binding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // sharedPreferences for onboarding screen
        sharedPreferences = getSharedPreferences("NOTES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Glide.with(SplashScreenActivity.this).load(R.drawable.note_splash_screen).into(binding.splashIcon);

        // show note gif in splash onboarding
        Glide.with(this)
                .asGif()
                .load(R.drawable.note_splash_screen)
               // .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1);
                        return false;
                    }
                }).into(binding.splashIcon);

        // go to main activity
        binding.getStart.setOnClickListener(v -> {
            Intent intent = new Intent(OnBoardingScreenActivity.this, MainActivity.class);
            startActivity(intent);
            // save status in sharedprefrence
            editor.putBoolean("USER_ONBOARD", true);
            editor.apply();
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // skip this activity if true
        if (sharedPreferences.getBoolean("USER_ONBOARD", false)) {
            Intent intent = new Intent(OnBoardingScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}