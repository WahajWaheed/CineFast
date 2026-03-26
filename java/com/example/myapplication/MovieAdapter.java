package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onBookSeatsClick(Movie movie);
        void onTrailerClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.poster.setImageResource(movie.getPosterResId());
        holder.movieName.setText(movie.getName());
        holder.movieGenre.setText(movie.getGenreDuration());

        if (movie.isComingSoon()) {
            holder.btnBook.setText("Coming Soon");
            holder.btnBook.setEnabled(false);
            holder.btnBook.setBackgroundTintList(holder.itemView.getContext().getResources().getColorStateList(android.R.color.darker_gray, null));
        } else {
            holder.btnBook.setText("Book Seats");
            holder.btnBook.setEnabled(true);
            holder.btnBook.setBackgroundTintList(holder.itemView.getContext().getResources().getColorStateList(com.google.android.material.R.color.design_default_color_error, null));
        }

        holder.btnBook.setOnClickListener(v -> listener.onBookSeatsClick(movie));
        holder.btnTrailer.setOnClickListener(v -> listener.onTrailerClick(movie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView movieName, movieGenre;
        View btnTrailer;
        MaterialButton btnBook;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            movieName = itemView.findViewById(R.id.movie_name);
            movieGenre = itemView.findViewById(R.id.movie_genre);
            btnTrailer = itemView.findViewById(R.id.btn_trailer);
            btnBook = itemView.findViewById(R.id.btn_book);
        }
    }
}
