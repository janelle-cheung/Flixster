package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.service.autofill.TextValueSanitizer;
import android.telecom.Conference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    Context context;
    List<Movie> movies;
    OnClickListener onClickListener;

    public MovieAdapter(Context context, List<Movie> movies, OnClickListener onClickListener) {
        this.context = context;
        this.movies = movies;
        this.onClickListener = onClickListener;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

        }

        public void bind(Movie movie) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            String imageUrl;
            int placeholderOrientation;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                placeholderOrientation = R.mipmap.backdrop_placeholder_foreground;
            } else {
                imageUrl = movie.getPosterPath();
                placeholderOrientation = R.mipmap.placeholder_foreground;
            }

            int radius = 30;
            int margin = 10;
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(placeholderOrientation)
                    .centerCrop() // scale image to fill the entire ImageView
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);
        }
    }
}
