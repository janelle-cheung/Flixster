package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String KEY_POSTER_PATH = "poster_path";
    public static final String KEY_TITLE = "title";
    public static final String KEY_YEAR = "year";
    public static final String KEY_RATING = "rating";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_ID = "id";

    String categoryTitle;
    String categoryPath;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "created main activity");
        super.onCreate(savedInstanceState);
        categoryTitle = getIntent().getStringExtra(CategoryActivity.KEY_CATEGORY_TITLE);
        categoryPath = getIntent().getStringExtra(CategoryActivity.KEY_CATEGORY_PATH);
        getSupportActionBar().setTitle(categoryTitle);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        RecyclerView rvMovies = binding.rvMovies;

        movies = new ArrayList<>();

        MovieAdapter.OnClickListener onClickListener = new MovieAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent i = new Intent(MainActivity.this, MovieDetailsActivity.class);
                i.putExtra(KEY_POSTER_PATH, movies.get(position).getPosterPath());
                i.putExtra(KEY_BACKDROP_PATH, movies.get(position).getBackdropPath());
                i.putExtra(KEY_TITLE, movies.get(position).getTitle());
                i.putExtra(KEY_YEAR, movies.get(position).getYear());
                i.putExtra(KEY_RATING, movies.get(position).getRating());
                i.putExtra(KEY_OVERVIEW, movies.get(position).getOverview());
                i.putExtra(KEY_ID, movies.get(position).getId());
                startActivity(i);
            }
        };

        // Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies, onClickListener);

        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        rvMovies.addItemDecoration(new DividerItemDecoration(rvMovies.getContext(), DividerItemDecoration.VERTICAL));

        AsyncHttpClient client = new AsyncHttpClient();

        String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/" + categoryPath + "?api_key=d54409dd2729bc6f105a0c0e4b7c2ebc";
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.d(TAG, String.valueOf(movies.size()));
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
}