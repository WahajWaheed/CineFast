package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    private TextView tvMovieTitle, tvRatingLang, tvScreen, tvTheater, tvHall, tvDate, tvTime, tvTotalSelected;
    private GridLayout seatGrid;
    private Button btnProceedSnacks, btnBookSeats;

    private String movieName;
    private int selectedCount = 0;
    private final int PRICE_PER_SEAT = 10;
    private List<Seat> seatList;
    private final int ROWS = 5, COLS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        movieName = getIntent().getStringExtra("movie_name");

        // Initialize views
        tvMovieTitle = findViewById(R.id.tv_movie_title);
        tvRatingLang = findViewById(R.id.tv_movie_rating_lang);
        tvScreen = findViewById(R.id.tv_movie_screen);
        tvTheater = findViewById(R.id.tv_theater);
        tvHall = findViewById(R.id.tv_hall);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvTotalSelected = findViewById(R.id.tv_total_selected);
        seatGrid = findViewById(R.id.seat_grid);
        btnProceedSnacks = findViewById(R.id.btn_proceed_snacks);
        btnBookSeats = findViewById(R.id.btn_book_seats);

        // Set movie details based on movie name
        setMovieDetails(movieName);

        initializeSeats();
        createSeatButtons();

        btnProceedSnacks.setOnClickListener(v -> {
            Intent intent = new Intent(SeatSelectionActivity.this, SnacksActivity.class);
            intent.putExtra("movie_name", movieName);
            intent.putExtra("selected_seats", getSelectedSeatsString());
            intent.putExtra("seat_count", selectedCount);
            intent.putExtra("ticket_total", selectedCount * PRICE_PER_SEAT);
            startActivity(intent);
        });

        btnBookSeats.setOnClickListener(v -> {
            if (selectedCount == 0) {
                Toast.makeText(this, R.string.select_seat_warning, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(SeatSelectionActivity.this, TicketSummaryActivity.class);
            intent.putExtra("movie_name", movieName);
            intent.putExtra("selected_seats", getSelectedSeatsString());
            intent.putExtra("seat_count", selectedCount);
            intent.putExtra("ticket_total", selectedCount * PRICE_PER_SEAT);
            intent.putStringArrayListExtra("snack_items", new ArrayList<>());
            intent.putExtra("snacks_total", 0);
            startActivity(intent);
        });
    }

    private void setMovieDetails(String movieName) {
        // Hardcode details for each movie
        if (movieName.equals("Inception")) {
            tvMovieTitle.setText("Inception");
            tvRatingLang.setText("+13  EN");
            tvScreen.setText("ScreenX");
            tvTheater.setText("Stars (90°Mall)");
            tvHall.setText("1st");
            tvDate.setText("13.04.2025");
            tvTime.setText("22:15");
        } else if (movieName.equals("The Dark Knight")) {
            tvMovieTitle.setText("The Dark Knight");
            tvRatingLang.setText("+16  EN");
            tvScreen.setText("IMAX");
            tvTheater.setText("City Center");
            tvHall.setText("2nd");
            tvDate.setText("14.04.2025");
            tvTime.setText("20:30");
        } else if (movieName.equals("Interstellar")) {
            tvMovieTitle.setText("Interstellar");
            tvRatingLang.setText("+13  EN");
            tvScreen.setText("ScreenX");
            tvTheater.setText("Galaxy Mall");
            tvHall.setText("3rd");
            tvDate.setText("15.04.2025");
            tvTime.setText("19:00");
        } else {
            // Default fallback
            tvMovieTitle.setText(movieName);
            tvRatingLang.setText("+13  EN");
            tvScreen.setText("ScreenX");
            tvTheater.setText("Stars (90°Mall)");
            tvHall.setText("1st");
            tvDate.setText("13.04.2025");
            tvTime.setText("22:15");
        }
    }

    private void initializeSeats() {
        seatList = new ArrayList<>();
        for (int i = 0; i < ROWS * COLS; i++) {
            Seat seat = new Seat(i);
            // Hardcode some booked seats (e.g., seats 2, 7, 12)
            if (i == 2 || i == 7 || i == 12) {
                seat.setState(Seat.STATE_BOOKED);
            } else {
                seat.setState(Seat.STATE_AVAILABLE);
            }
            seatList.add(seat);
        }
    }

    private void createSeatButtons() {
        seatGrid.removeAllViews();
        int seatSize = getResources().getDimensionPixelSize(R.dimen.seat_button_width);
        for (int i = 0; i < seatList.size(); i++) {
            Button btn = new Button(this);
            btn.setText(String.valueOf(i + 1));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = seatSize;
            params.height = seatSize;
            params.setMargins(4, 4, 4, 4);
            btn.setLayoutParams(params);
            updateButtonAppearance(btn, seatList.get(i).getState());
            final int index = i;
            btn.setOnClickListener(v -> onSeatClick(index));
            seatGrid.addView(btn);
        }
    }

    private void onSeatClick(int index) {
        Seat seat = seatList.get(index);
        if (seat.getState() == Seat.STATE_BOOKED) {
            Toast.makeText(this, R.string.seat_booked, Toast.LENGTH_SHORT).show();
            return;
        }
        if (seat.getState() == Seat.STATE_AVAILABLE) {
            seat.setState(Seat.STATE_SELECTED);
            selectedCount++;
        } else if (seat.getState() == Seat.STATE_SELECTED) {
            seat.setState(Seat.STATE_AVAILABLE);
            selectedCount--;
        }
        Button btn = (Button) seatGrid.getChildAt(index);
        updateButtonAppearance(btn, seat.getState());
        tvTotalSelected.setText(getString(R.string.selected_seats, selectedCount));
        btnProceedSnacks.setEnabled(selectedCount > 0);
    }

    private void updateButtonAppearance(Button btn, int state) {
        switch (state) {
            case Seat.STATE_AVAILABLE:
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.seat_available));
                btn.setEnabled(true);
                break;
            case Seat.STATE_SELECTED:
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.seat_selected));
                btn.setEnabled(true);
                break;
            case Seat.STATE_BOOKED:
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.seat_booked));
                btn.setEnabled(false);
                break;
        }
    }

    private String getSelectedSeatsString() {
        StringBuilder sb = new StringBuilder();
        char[] rows = {'A', 'B', 'C', 'D', 'E'};
        for (int i = 0; i < seatList.size(); i++) {
            if (seatList.get(i).getState() == Seat.STATE_SELECTED) {
                int row = i / 5;
                int seatInRow = (i % 5) + 1;
                if (sb.length() > 0) sb.append(",");
                sb.append(rows[row]).append(seatInRow);
            }
        }
        return sb.toString();
    }

    private static class Seat {
        static final int STATE_AVAILABLE = 0;
        static final int STATE_SELECTED = 1;
        static final int STATE_BOOKED = 2;
        private int id;
        private int state;

        Seat(int id) { this.id = id; }
        int getState() { return state; }
        void setState(int state) { this.state = state; }
    }
}