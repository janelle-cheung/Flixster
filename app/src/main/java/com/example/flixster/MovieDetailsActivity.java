package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String TAG = "MovieDetailsActivity";
    public static final String KEY_YOUTUBE_KEY = "youtube_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String posterPath;
        int placeholder;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            posterPath = getIntent().getStringExtra(MainActivity.KEY_POSTER_PATH);
            placeholder = R.mipmap.placeholder_foreground;
        } else {
            posterPath = getIntent().getStringExtra(MainActivity.KEY_BACKDROP_PATH);
            placeholder = R.mipmap.backdrop_placeholder_foreground;
        }

        int radius = 30;
        int margin = 10;
        String titleInfo = getIntent().getStringExtra(MainActivity.KEY_TITLE) + " (" + getIntent().getIntExtra(MainActivity.KEY_YEAR, 1970) + ")";
        Glide.with(getApplicationContext())
                .load(posterPath)
                .transform(new RoundedCornersTransformation(radius, margin))
                .placeholder(placeholder)
                .into(binding.ivPoster);
        binding.tvTitle.setText(titleInfo);
        binding.rbRating.setRating((float) ((getIntent().getDoubleExtra(MainActivity.KEY_RATING, 0.0)) / 2.0));
        binding.tvOverview.setText(getIntent().getStringExtra(MainActivity.KEY_OVERVIEW));

        String videos_url = "https://api.themoviedb.org/3/movie/" + getIntent().getIntExtra(MainActivity.KEY_ID, 0) + "/videos?api_key=d54409dd2729bc6f105a0c0e4b7c2ebc";
        binding.btnYouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(videos_url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            String youtube_key = results.getJSONObject(0).getString("key");
                            Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                            i.putExtra(KEY_YOUTUBE_KEY, youtube_key);
                            Log.d(TAG, youtube_key);
                            startActivity(i);
                        } catch (JSONException e) {
                            Log.d(TAG, "Hit JSON exception", e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });
            }
        });
    }
}