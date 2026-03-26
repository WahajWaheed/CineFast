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

public class NowShowingFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

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
        movieList.add(new Movie("The Dark Knight", "Action / 152 min", R.drawable.darkknight, "abc123", false));
        movieList.add(new Movie("Inception", "Sci-Fi / 148 min", R.drawable.inception, "def456", false));
        movieList.add(new Movie("Interstellar", "Sci-Fi / 169 min", R.drawable.interstellar, "ghi789", false));
        movieList.add(new Movie("The Shawshank Redemption", "Drama / 142 min", R.drawable.redemption, "jkl012", false));
        movieList.add(new Movie("Oppenheimer", "Biography / 180 min", R.drawable.oppenheimer, "mno345", false));

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
