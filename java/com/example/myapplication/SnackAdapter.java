package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class SnackAdapter extends BaseAdapter {

    private Context context;
    private List<Snack> snackList;
    private OnSnackQuantityChangeListener listener;

    public interface OnSnackQuantityChangeListener {
        void onQuantityChanged();
    }

    public SnackAdapter(Context context, List<Snack> snackList, OnSnackQuantityChangeListener listener) {
        this.context = context;
        this.snackList = snackList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return snackList.size();
    }

    @Override
    public Object getItem(int position) {
        return snackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_snack, parent, false);
        }

        Snack snack = snackList.get(position);

        ImageView snackImage = convertView.findViewById(R.id.snack_image);
        TextView snackName = convertView.findViewById(R.id.snack_name);
        TextView snackPrice = convertView.findViewById(R.id.snack_price);
        TextView tvQuantity = convertView.findViewById(R.id.tv_quantity);
        Button btnMinus = convertView.findViewById(R.id.btn_minus);
        Button btnPlus = convertView.findViewById(R.id.btn_plus);

        snackImage.setImageResource(snack.getImageResId());
        snackName.setText(snack.getName());
        snackPrice.setText("$" + String.format("%.2f", snack.getPrice()));
        tvQuantity.setText(String.valueOf(snack.getQuantity()));

        btnMinus.setOnClickListener(v -> {
            if (snack.getQuantity() > 0) {
                snack.setQuantity(snack.getQuantity() - 1);
                tvQuantity.setText(String.valueOf(snack.getQuantity()));
                if (listener != null) {
                    listener.onQuantityChanged();
                }
            }
        });

        btnPlus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() + 1);
            tvQuantity.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) {
                listener.onQuantityChanged();
            }
        });

        return convertView;
    }
}
