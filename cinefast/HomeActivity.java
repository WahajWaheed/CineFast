package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private final String[] movieNames = {"","",""};
    private final String[] videoIds = {"abc123", "def456", "ghi789"}; // Replace with actual YouTube IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        movieNames[0] = getString(R.string.movie1_name);
        movieNames[1] = getString(R.string.movie2_name);
        movieNames[2] = getString(R.string.movie3_name);

        // Movie 1
        Button book1 = findViewById(R.id.btn_book1);
        ImageButton trailer1 = findViewById(R.id.btn_trailer1);
        book1.setOnClickListener(v -> openSeatSelection(movieNames[0]));
        trailer1.setOnClickListener(v -> watchTrailer(videoIds[0]));

        // Movie 2
        Button book2 = findViewById(R.id.btn_book2);
        ImageButton trailer2 = findViewById(R.id.btn_trailer2);
        book2.setOnClickListener(v -> openSeatSelection(movieNames[1]));
        trailer2.setOnClickListener(v -> watchTrailer(videoIds[1]));

        // Movie 3
        Button book3 = findViewById(R.id.btn_book3);
        ImageButton trailer3 = findViewById(R.id.btn_trailer3);
        book3.setOnClickListener(v -> openSeatSelection(movieNames[2]));
        trailer3.setOnClickListener(v -> watchTrailer(videoIds[2]));
    }

    private void openSeatSelection(String movieName) {
        Intent intent = new Intent(HomeActivity.this, SeatSelectionActivity.class);
        intent.putExtra("movie_name", movieName);
        startActivity(intent);
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