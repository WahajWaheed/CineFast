package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ComingSoonFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        movieList = new ArrayList<>();
        movieList.add(new Movie("Dune: Part Two", "Sci-Fi / 166 min", R.drawable.dune, "Way9Dexny3w", true));
        movieList.add(new Movie("Deadpool 3", "Action/Comedy / 130 min", R.drawable.deadpool, "73_1biulkYk", true));
        movieList.add(new Movie("Mickey 17", "Sci-Fi/Drama", R.drawable.mickey, "n6yYy0VmsH0", true));
        movieList.add(new Movie("Gladiator 2", "Action / 150 min", R.drawable.gladiator, "gladr2xxx", true));
        movieList.add(new Movie("Kung Fu Panda 4", "Animation / 94 min", R.drawable.kungfu, "kfp4xxx", true));

        movieAdapter = new MovieAdapter(movieList, this);
        recyclerView.setAdapter(movieAdapter);

        return view;
    }

    @Override
    public void onBookSeatsClick(Movie movie) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToSeatSelection(movie);
        }
    }

    @Override
    public void onTrailerClick(Movie movie) {
        watchTrailer(movie.getTrailerVideoId());
    }

    private void watchTrailer(String videoId) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
        try {
            startActivity(intent);
        } catch (Exception e) {
            startActivity(webIntent);
        }
    }
}
