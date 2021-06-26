package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CategoryActivity extends AppCompatActivity {

    public static final String KEY_CATEGORY_TITLE = "category";
    public static final String KEY_CATEGORY_PATH = "category_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }

    public void launchMoviesActivity (String categoryTitle, String categoryPath) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(KEY_CATEGORY_TITLE, categoryTitle);
        i.putExtra(KEY_CATEGORY_PATH, categoryPath);
        startActivity(i);
    }

    public void launchNowPlaying(View view) {
        launchMoviesActivity("Now Playing", "now_playing");
    }

    public void launchTopRated(View view) {
        launchMoviesActivity("Top Rated", "top_rated");
    }

    public void launchUpcoming(View view) {
        launchMoviesActivity("Upcoming", "upcoming");
    }
}