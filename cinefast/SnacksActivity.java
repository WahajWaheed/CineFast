package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SnacksActivity extends AppCompatActivity {

    private TextView tvTotal;
    private int[] quantities = new int[4];          // 4 items
    private int[] prices;
    private TextView[] quantityViews = new TextView[4];
    private Button[] minusButtons = new Button[4];
    private Button[] plusButtons = new Button[4];

    private String movieName;
    private String selectedSeats;
    private int seatCount;
    private int ticketTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        // Get data from intent
        movieName = getIntent().getStringExtra("movie_name");
        selectedSeats = getIntent().getStringExtra("selected_seats");
        seatCount = getIntent().getIntExtra("seat_count", 0);
        ticketTotal = getIntent().getIntExtra("ticket_total", 0);

        // Prices for 4 items (must match strings.xml)
        prices = new int[]{
                Integer.parseInt(getString(R.string.price_popcorn)),   // 5
                Integer.parseInt(getString(R.string.price_nachos)),    // 6
                Integer.parseInt(getString(R.string.price_softdrink)), // 3
                Integer.parseInt(getString(R.string.price_candymix))   // 4
        };

        tvTotal = findViewById(R.id.tv_snacks_total);

        // Setup each snack item (IDs: snack1 to snack4)
        setupSnackItem(R.id.snack1, R.drawable.popcorn, R.string.popcorn,
                R.string.popcorn_desc, prices[0], 0);
        setupSnackItem(R.id.snack2, R.drawable.nachos, R.string.nachos,
                R.string.nachos_desc, prices[1], 1);
        setupSnackItem(R.id.snack3, R.drawable.softdrink, R.string.softdrink,
                R.string.softdrink_desc, prices[2], 2);
        setupSnackItem(R.id.snack4, R.drawable.candymix, R.string.candymix,
                R.string.candymix_desc, prices[3], 3);

        Button btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            int snacksTotal = 0;
            ArrayList<String> snackLines = new ArrayList<>();
            String[] snackNames = {
                    getString(R.string.popcorn),
                    getString(R.string.nachos),
                    getString(R.string.softdrink),
                    getString(R.string.candymix)
            };

            for (int i = 0; i < 4; i++) {
                snacksTotal += quantities[i] * prices[i];
                if (quantities[i] > 0) {
                    String line = "X" + quantities[i] + " " + snackNames[i] +
                            "   " + (quantities[i] * prices[i]) + " USD";
                    snackLines.add(line);
                }
            }

            Intent intent = new Intent(SnacksActivity.this, TicketSummaryActivity.class);
            intent.putExtra("movie_name", movieName);
            intent.putExtra("selected_seats", selectedSeats);
            intent.putExtra("seat_count", seatCount);
            intent.putExtra("ticket_total", ticketTotal);
            intent.putStringArrayListExtra("snack_items", snackLines);
            intent.putExtra("snacks_total", snacksTotal);
            startActivity(intent);
        });
    }

    private void setupSnackItem(int includeId, int imageRes, int nameRes,
                                int descRes, int priceValue, int index) {
        View item = findViewById(includeId);
        ImageView image = item.findViewById(R.id.snack_image);
        TextView name = item.findViewById(R.id.snack_name);
        TextView description = item.findViewById(R.id.snack_description);
        TextView price = item.findViewById(R.id.snack_price);
        quantityViews[index] = item.findViewById(R.id.tv_quantity);
        minusButtons[index] = item.findViewById(R.id.btn_minus);
        plusButtons[index] = item.findViewById(R.id.btn_plus);

        image.setImageResource(imageRes);
        name.setText(nameRes);
        description.setText(descRes);
        price.setText(getString(R.string.price_format, priceValue));

        quantities[index] = 0;
        quantityViews[index].setText("0");
        minusButtons[index].setEnabled(false);

        plusButtons[index].setOnClickListener(v -> {
            quantities[index]++;
            quantityViews[index].setText(String.valueOf(quantities[index]));
            quantityViews[index].setTextColor(getResources().getColor(R.color.white));
            minusButtons[index].setEnabled(true);
            updateTotal();
        });

        minusButtons[index].setOnClickListener(v -> {
            if (quantities[index] > 0) {
                quantities[index]--;
                quantityViews[index].setText(String.valueOf(quantities[index]));
                if (quantities[index] == 0) {
                    minusButtons[index].setEnabled(false);
                }
                updateTotal();
            }
        });
    }

    private void updateTotal() {
        int total = 0;
        for (int i = 0; i < 4; i++) {
            total += quantities[i] * prices[i];
        }
        tvTotal.setText(getString(R.string.snacks_total, total));
    }
}