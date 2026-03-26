package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionFragment extends Fragment {

    private TextView tvMovieTitle, tvTotalSelected;
    private GridLayout seatGrid;
    private Button btnProceedSnacks, btnBookSeats;
    private ImageView btnBack;

    private Movie currentMovie;
    private int selectedCount = 0;
    private final int PRICE_PER_SEAT = 10;
    private List<Seat> seatList;
    private final int ROWS = 8, COLS = 9;

    public static SeatSelectionFragment newInstance(Movie movie) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentMovie = (Movie) getArguments().getSerializable("movie");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);

        tvMovieTitle = view.findViewById(R.id.tv_movie_title);
        tvTotalSelected = view.findViewById(R.id.tv_total_selected);
        seatGrid = view.findViewById(R.id.seat_grid);
        btnProceedSnacks = view.findViewById(R.id.btn_proceed_snacks);
        btnBookSeats = view.findViewById(R.id.btn_book_seats);
        btnBack = view.findViewById(R.id.btn_back);

        if (currentMovie != null) {
            tvMovieTitle.setText(currentMovie.getName());
        }

        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).popBackStack();
            }
        });

        initializeSeats();
        createSeatButtons();

        if (currentMovie != null && currentMovie.isComingSoon()) {
            setupComingSoonMode();
        } else {
            setupNowShowingMode();
        }

        updateSelectedStatus();
        return view;
    }

    private void setupNowShowingMode() {
        btnProceedSnacks.setOnClickListener(v -> {
            if (selectedCount == 0) {
                Toast.makeText(getContext(), R.string.select_seat_warning, Toast.LENGTH_SHORT).show();
                return;
            }
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToSnacks(currentMovie, getSelectedSeatsString(), selectedCount, selectedCount * PRICE_PER_SEAT);
            }
        });

        btnBookSeats.setOnClickListener(v -> {
            if (selectedCount == 0) {
                Toast.makeText(getContext(), R.string.select_seat_warning, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTicketSummary(currentMovie, getSelectedSeatsString(), selectedCount, selectedCount * PRICE_PER_SEAT, new ArrayList<>(), 0);
            }
        });
    }

    private void setupComingSoonMode() {
        btnBookSeats.setText("Coming Soon");
        btnBookSeats.setEnabled(false);
        btnBookSeats.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray, null));

        btnProceedSnacks.setText("Watch Trailer");
        btnProceedSnacks.setTextColor(getResources().getColor(android.R.color.white, null));
        btnProceedSnacks.setBackgroundTintList(getResources().getColorStateList(com.google.android.material.R.color.design_default_color_error, null));
        btnProceedSnacks.setOnClickListener(v -> {
            if (currentMovie != null) {
                watchTrailer(currentMovie.getTrailerVideoId());
            }
        });
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

    private void initializeSeats() {
        seatList = new ArrayList<>();
        int[][] layout = {
                {0, 1, 1, 1, 0, 1, 1, 1, 0},
                {2, 1, 1, 1, 0, 1, 1, 2, 1},
                {1, 1, 1, 1, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 1, 1, 1},
                {1, 1, 2, 2, 0, 1, 1, 1, 1},
                {1, 2, 1, 1, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 1, 2, 2},
                {0, 1, 1, 1, 0, 1, 1, 1, 0}
        };

        selectedCount = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Seat seat = new Seat(r * COLS + c);
                int type = layout[r][c];
                if (type == 0) {
                    seat.setState(Seat.STATE_EMPTY);
                } else if (type == 1) {
                    seat.setState(Seat.STATE_AVAILABLE);
                } else if (type == 2) {
                    seat.setState(Seat.STATE_BOOKED);
                }
                seatList.add(seat);
            }
        }
    }

    private void createSeatButtons() {
        seatGrid.removeAllViews();
        
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (getActivity() != null) {
             getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        int screenWidth = displayMetrics.widthPixels;
        int padding = (int) (32 * displayMetrics.density);
        int availableWidth = screenWidth - padding;
        int seatWidth = availableWidth / COLS - (int)(4 * displayMetrics.density);

        for (int i = 0; i < seatList.size(); i++) {
            Seat seat = seatList.get(i);
            View view;
            if (seat.getState() == Seat.STATE_EMPTY) {
                view = new View(getContext());
            } else {
                Button btn = new Button(getContext());
                updateButtonAppearance(btn, seat.getState());
                final int index = i;
                if (currentMovie == null || !currentMovie.isComingSoon()) {
                    btn.setOnClickListener(v -> onSeatClick(index));
                }
                view = btn;
            }

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = seatWidth;
            params.height = seatWidth;
            params.setMargins(2, 2, 2, 2);
            view.setLayoutParams(params);
            seatGrid.addView(view);
        }
    }

    private void onSeatClick(int index) {
        Seat seat = seatList.get(index);
        if (seat.getState() == Seat.STATE_BOOKED) {
            Toast.makeText(getContext(), R.string.seat_booked, Toast.LENGTH_SHORT).show();
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
        updateSelectedStatus();
    }

    private void updateSelectedStatus() {
        if (tvTotalSelected != null) {
            tvTotalSelected.setText(getString(R.string.selected_seats, selectedCount));
        }
        if (currentMovie != null && !currentMovie.isComingSoon()) {
            if (btnProceedSnacks != null) {
                btnProceedSnacks.setEnabled(selectedCount > 0);
                btnProceedSnacks.setAlpha(selectedCount > 0 ? 1.0f : 0.6f);
            }
        }
    }

    private void updateButtonAppearance(Button btn, int state) {
        btn.setText(""); 
        switch (state) {
            case Seat.STATE_AVAILABLE:
                btn.setBackgroundResource(R.drawable.available);
                break;
            case Seat.STATE_SELECTED:
                btn.setBackgroundResource(R.drawable.yourseat);
                break;
            case Seat.STATE_BOOKED:
                btn.setBackgroundResource(R.drawable.booked);
                break;
        }
    }

    private String getSelectedSeatsString() {
        StringBuilder sb = new StringBuilder();
        char[] rowLabels = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for (int i = 0; i < seatList.size(); i++) {
            if (seatList.get(i).getState() == Seat.STATE_SELECTED) {
                int r = i / COLS;
                int c = i % COLS;
                int seatNum = (c < 4) ? (c + 1) : c;
                if (sb.length() > 0) sb.append(",");
                sb.append(rowLabels[r]).append(seatNum);
            }
        }
        return sb.toString();
    }

    private static class Seat {
        static final int STATE_EMPTY = -1;
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
