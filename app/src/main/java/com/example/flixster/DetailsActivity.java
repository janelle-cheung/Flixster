package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.databinding.ActivityDetailsBinding;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {

    /*ImageView ivPoster;
    TextView tvTitle;
    RatingBar rbRating;
    TextView tvOverview;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // setContentView(R.layout.activity_details);
        /*ivPoster = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        rbRating = findViewById(R.id.rbRating);
        tvOverview = findViewById(R.id.tvOverview);*/

        String posterPath;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            posterPath = getIntent().getStringExtra(MainActivity.KEY_POSTER_PATH);
        } else {
            posterPath = getIntent().getStringExtra(MainActivity.KEY_BACKDROP_PATH);
        }

        int radius = 30;
        int margin = 10;
        String titleInfo = getIntent().getStringExtra(MainActivity.KEY_TITLE) + " (" + getIntent().getIntExtra(MainActivity.KEY_YEAR, 1970) + ")";
        Glide.with(getApplicationContext())
                .load(posterPath)
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(binding.ivPoster);
        binding.tvTitle.setText(titleInfo);
        binding.rbRating.setRating((float) ((getIntent().getDoubleExtra(MainActivity.KEY_RATING, 0.0)) / 2.0));
        binding.tvOverview.setText(getIntent().getStringExtra(MainActivity.KEY_OVERVIEW));
    }
}