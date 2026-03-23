package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TicketSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_summary);

        // Get data from intent
        String movieName = getIntent().getStringExtra("movie_name");
        String selectedSeatsStr = getIntent().getStringExtra("selected_seats");
        int seatCount = getIntent().getIntExtra("seat_count", 0);
        int ticketTotal = getIntent().getIntExtra("ticket_total", 0);
        int snacksTotal = getIntent().getIntExtra("snacks_total", 0);
        ArrayList<String> snackItems = getIntent().getStringArrayListExtra("snack_items");

        // Fill movie details (hardcoded per movie)
        setMovieDetails(movieName);

        // Fill tickets
        LinearLayout ticketsContainer = findViewById(R.id.tickets_container);
        if (selectedSeatsStr != null && !selectedSeatsStr.isEmpty()) {
            String[] seats = selectedSeatsStr.split(",");
            int pricePerSeat = ticketTotal / seatCount; // assumes all same price
            for (String seat : seats) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);

                TextView seatDesc = new TextView(this);
                seatDesc.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                seatDesc.setText("Row " + seat.charAt(0) + ", Seat " + seat.substring(1));
                seatDesc.setTextColor(getResources().getColor(R.color.white));
                seatDesc.setTextSize(16);

                TextView seatPrice = new TextView(this);
                seatPrice.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                seatPrice.setText(pricePerSeat + " USD");
                seatPrice.setTextColor(getResources().getColor(R.color.white));
                seatPrice.setTextSize(16);

                row.addView(seatDesc);
                row.addView(seatPrice);
                ticketsContainer.addView(row);
            }
        } else {
            TextView noSeats = new TextView(this);
            noSeats.setText("No seats selected");
            noSeats.setTextColor(getResources().getColor(R.color.white));
            ticketsContainer.addView(noSeats);
        }

        // Fill snacks
        LinearLayout snacksContainer = findViewById(R.id.snacks_container);
        if (snackItems != null && !snackItems.isEmpty()) {
            for (String snack : snackItems) {
                TextView snackLine = new TextView(this);
                snackLine.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                snackLine.setText(snack);
                snackLine.setTextColor(getResources().getColor(R.color.white));
                snackLine.setTextSize(16);
                snacksContainer.addView(snackLine);
            }
        } else {
            TextView noSnacks = new TextView(this);
            noSnacks.setText("No snacks selected");
            noSnacks.setTextColor(getResources().getColor(R.color.white));
            snacksContainer.addView(noSnacks);
        }

        // Grand total
        TextView tvGrandTotal = findViewById(R.id.tv_grand_total);
        int grandTotal = ticketTotal + snacksTotal;
        tvGrandTotal.setText("TOTAL  " + grandTotal + " USD");

        // Send button
        Button btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            StringBuilder shareBody = new StringBuilder();
            shareBody.append("Movie: ").append(movieName).append("\n");
            shareBody.append("Seats: ").append(selectedSeatsStr).append("\n");
            shareBody.append("Ticket Total: ").append(ticketTotal).append(" USD\n");
            if (snackItems != null && !snackItems.isEmpty()) {
                shareBody.append("Snacks:\n");
                for (String snack : snackItems) {
                    shareBody.append(snack).append("\n");
                }
            }
            shareBody.append("Grand Total: ").append(grandTotal).append(" USD");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
    }

    private void setMovieDetails(String movieName) {
        TextView tvMovieTitle = findViewById(R.id.tv_movie_title);
        TextView tvDetails1 = findViewById(R.id.tv_movie_details1);
        TextView tvDetails2 = findViewById(R.id.tv_movie_details2);
        TextView tvTheater = findViewById(R.id.tv_theater);
        TextView tvHall = findViewById(R.id.tv_hall);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvTime = findViewById(R.id.tv_time);

        // Hardcode details for each movie (you can expand)
        if (movieName.equals("Inception")) {
            tvMovieTitle.setText("Inception");
            tvDetails1.setText("+13  EN");
            tvDetails2.setText("ScreenX  Dolby Atmos");
            tvTheater.setText("Stars (90°Mall)");
            tvHall.setText("1st");
            tvDate.setText("13.04.2025");
            tvTime.setText("22:15");
        } else if (movieName.equals("The Dark Knight")) {
            tvMovieTitle.setText("The Dark Knight");
            tvDetails1.setText("+16  EN");
            tvDetails2.setText("IMAX  Dolby Atmos");
            tvTheater.setText("City Center");
            tvHall.setText("2nd");
            tvDate.setText("14.04.2025");
            tvTime.setText("20:30");
        } else if (movieName.equals("Interstellar")) {
            tvMovieTitle.setText("Interstellar");
            tvDetails1.setText("+13  EN");
            tvDetails2.setText("ScreenX  Dolby Atmos");
            tvTheater.setText("Galaxy Mall");
            tvHall.setText("3rd");
            tvDate.setText("15.04.2025");
            tvTime.setText("19:00");
        } else {
            // Default fallback
            tvMovieTitle.setText(movieName);
            tvDetails1.setText("+13  EN");
            tvDetails2.setText("ScreenX  Dolby Atmos");
            tvTheater.setText("Stars (90°Mall)");
            tvHall.setText("1st");
            tvDate.setText("13.04.2025");
            tvTime.setText("22:15");
        }
    }
}