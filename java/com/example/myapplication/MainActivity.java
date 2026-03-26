package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    public void navigateToSeatSelection(Movie movie) {
        SeatSelectionFragment fragment = SeatSelectionFragment.newInstance(movie);
        navigateToFragment(fragment, true);
    }

    public void navigateToSnacks(Movie movie, String selectedSeats, int seatCount, int ticketTotal) {
        SnacksFragment fragment = SnacksFragment.newInstance(movie, selectedSeats, seatCount, ticketTotal);
        navigateToFragment(fragment, true);
    }

    public void navigateToTicketSummary(Movie movie, String selectedSeats, int seatCount, int ticketTotal, java.util.ArrayList<String> snackItems, int snacksTotal) {
        TicketSummaryFragment fragment = TicketSummaryFragment.newInstance(movie, selectedSeats, seatCount, ticketTotal, snackItems, snacksTotal);
        navigateToFragment(fragment, true);
    }
    
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }
    
    public void popToHome() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void navigateToFragment(Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
