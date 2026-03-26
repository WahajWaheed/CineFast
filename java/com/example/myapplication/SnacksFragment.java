package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class SnacksFragment extends Fragment implements SnackAdapter.OnSnackQuantityChangeListener {

    private TextView tvTotal;
    private ListView listViewSnacks;
    private List<Snack> snackList;
    private SnackAdapter snackAdapter;
    private Movie currentMovie;

    private String selectedSeats;
    private int seatCount;
    private int ticketTotal;

    public static SnacksFragment newInstance(Movie movie, String selectedSeats, int seatCount, int ticketTotal) {
        SnacksFragment fragment = new SnacksFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        args.putString("selectedSeats", selectedSeats);
        args.putInt("seatCount", seatCount);
        args.putInt("ticketTotal", ticketTotal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentMovie = (Movie) getArguments().getSerializable("movie");
            selectedSeats = getArguments().getString("selectedSeats");
            seatCount = getArguments().getInt("seatCount");
            ticketTotal = getArguments().getInt("ticketTotal");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        tvTotal = view.findViewById(R.id.tv_snacks_total);
        listViewSnacks = view.findViewById(R.id.list_view_snacks);
        
        ImageView btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).popBackStack();
            }
        });

        snackList = new ArrayList<>();
        snackList.add(new Snack(getString(R.string.popcorn), 5.0, R.drawable.popcorn));
        snackList.add(new Snack(getString(R.string.nachos), 6.0, R.drawable.nachos));
        snackList.add(new Snack(getString(R.string.softdrink), 3.0, R.drawable.softdrink));
        snackList.add(new Snack(getString(R.string.candymix), 4.0, R.drawable.candymix));

        snackAdapter = new SnackAdapter(getContext(), snackList, this);
        listViewSnacks.setAdapter(snackAdapter);

        // Calculate initially
        onQuantityChanged();

        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            int snacksTotal = 0;
            ArrayList<String> snackLines = new ArrayList<>();
            for (Snack snack : snackList) {
                if (snack.getQuantity() > 0) {
                    snacksTotal += snack.getPrice() * snack.getQuantity();
                    String line = "X" + snack.getQuantity() + " " + snack.getName() + "   $" + String.format("%.2f", snack.getPrice() * snack.getQuantity());
                    snackLines.add(line);
                }
            }

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTicketSummary(currentMovie, selectedSeats, seatCount, ticketTotal, snackLines, snacksTotal);
            }
        });

        return view;
    }

    @Override
    public void onQuantityChanged() {
        double total = 0;
        for (Snack snack : snackList) {
            total += snack.getPrice() * snack.getQuantity();
        }
        tvTotal.setText("Total: $" + String.format("%.2f", total));
    }
}
