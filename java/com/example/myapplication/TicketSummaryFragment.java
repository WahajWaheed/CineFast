package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class TicketSummaryFragment extends Fragment {

    private Movie currentMovie;
    private String selectedSeatsStr;
    private int seatCount;
    private int ticketTotal;
    private int snacksTotal;
    private ArrayList<String> snackItems;

    public static TicketSummaryFragment newInstance(Movie movie, String selectedSeats, int seatCount, int ticketTotal, ArrayList<String> snackItems, int snacksTotal) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        args.putString("selectedSeats", selectedSeats);
        args.putInt("seatCount", seatCount);
        args.putInt("ticketTotal", ticketTotal);
        args.putStringArrayList("snackItems", snackItems);
        args.putInt("snacksTotal", snacksTotal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentMovie = (Movie) getArguments().getSerializable("movie");
            selectedSeatsStr = getArguments().getString("selectedSeats");
            seatCount = getArguments().getInt("seatCount");
            ticketTotal = getArguments().getInt("ticketTotal");
            snackItems = getArguments().getStringArrayList("snackItems");
            snacksTotal = getArguments().getInt("snacksTotal");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        ImageView btnBack = view.findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).popBackStack();
                }
            });
        }

        if (currentMovie != null) {
            setMovieDetails(view, currentMovie.getName());
        }

        LinearLayout ticketsContainer = view.findViewById(R.id.tickets_container);
        if (selectedSeatsStr != null && !selectedSeatsStr.isEmpty() && seatCount > 0) {
            String[] seats = selectedSeatsStr.split(",");
            int pricePerSeat = ticketTotal / seatCount;
            for (String seat : seats) {
                if (seat.length() < 2) continue;
                LinearLayout row = new LinearLayout(getContext());
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);

                TextView seatDesc = new TextView(getContext());
                seatDesc.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                seatDesc.setText("Row " + seat.charAt(0) + ", Seat " + seat.substring(1));
                seatDesc.setTextColor(getResources().getColor(android.R.color.white, null));
                seatDesc.setTextSize(16);

                TextView seatPrice = new TextView(getContext());
                seatPrice.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                seatPrice.setText(pricePerSeat + " USD");
                seatPrice.setTextColor(getResources().getColor(android.R.color.white, null));
                seatPrice.setTextSize(16);

                row.addView(seatDesc);
                row.addView(seatPrice);
                ticketsContainer.addView(row);
            }
        } else {
            TextView noSeats = new TextView(getContext());
            noSeats.setText("No seats selected");
            noSeats.setTextColor(getResources().getColor(android.R.color.white, null));
            ticketsContainer.addView(noSeats);
        }

        LinearLayout snacksContainer = view.findViewById(R.id.snacks_container);
        if (snackItems != null && !snackItems.isEmpty()) {
            for (String snack : snackItems) {
                TextView snackLine = new TextView(getContext());
                snackLine.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                snackLine.setText(snack);
                snackLine.setTextColor(getResources().getColor(android.R.color.white, null));
                snackLine.setTextSize(16);
                snacksContainer.addView(snackLine);
            }
        } else {
            TextView noSnacks = new TextView(getContext());
            noSnacks.setText("No snacks selected");
            noSnacks.setTextColor(getResources().getColor(android.R.color.white, null));
            snacksContainer.addView(noSnacks);
        }

        TextView tvGrandTotal = view.findViewById(R.id.tv_grand_total);
        double grandTotal = ticketTotal + snacksTotal;
        tvGrandTotal.setText("TOTAL  $" + String.format("%.2f", grandTotal));

        // Save to SharedPreferences as last successful booking
        saveLastBooking(grandTotal);

        Button btnHome = view.findViewById(R.id.btn_home);
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).popToHome();
                }
            });
        }

        Button btnShare = view.findViewById(R.id.btn_share);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                StringBuilder shareBody = new StringBuilder();
                shareBody.append("Movie: ").append(currentMovie != null ? currentMovie.getName() : "Unknown").append("\n");
                shareBody.append("Seats: ").append(selectedSeatsStr).append("\n");
                shareBody.append("TOTAL: $").append(String.format("%.2f", grandTotal));
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());
                startActivity(Intent.createChooser(shareIntent, "Share Ticket via"));
            });
        }

        return view;
    }

    private void saveLastBooking(double totalTicketPrice) {
        if (getContext() != null && currentMovie != null) {
            SharedPreferences prefs = getContext().getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("movieName", currentMovie.getName());
            editor.putInt("seats", seatCount);
            // using float instead of double because putDouble doesnt exist natively in basic sharedprefs
            editor.putFloat("totalPrice", (float) totalTicketPrice);
            editor.apply();
        }
    }

    private void setMovieDetails(View view, String movieName) {
        TextView tvMovieTitle = view.findViewById(R.id.tv_movie_title);
        TextView tvDetails1 = view.findViewById(R.id.tv_movie_details1);
        TextView tvDetails2 = view.findViewById(R.id.tv_movie_details2);
        TextView tvTheater = view.findViewById(R.id.tv_theater);
        TextView tvHall = view.findViewById(R.id.tv_hall);
        TextView tvDate = view.findViewById(R.id.tv_date);
        TextView tvTime = view.findViewById(R.id.tv_time);

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
