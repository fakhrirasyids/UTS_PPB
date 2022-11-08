package com.fakhrirasyids.utsppb.ui.order;

import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_MOTOR;
import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_QUANTITY;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fakhrirasyids.utsppb.databinding.ActivityOrderBinding;
import com.fakhrirasyids.utsppb.entity.Motor;

import java.text.DecimalFormat;

public class OrderActivity extends AppCompatActivity {
    ActivityOrderBinding binding;

    Motor motor;
    int ppn;
    int qty, harga, bayar, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DecimalFormat formatter = new DecimalFormat("#,###,###");

        motor = getIntent().getParcelableExtra(EXTRA_MOTOR);
        qty = getIntent().getIntExtra(EXTRA_QUANTITY, 0);
        harga = Integer.parseInt(motor.harga);
        total = qty * harga;
        ppn = (int) (total * 0.11);
        bayar = total + ppn;

        binding.tvMotorName.setText(motor.nama);
        binding.tvHarga.setText(new StringBuilder("Rp " + formatter.format(harga)));
        binding.tvQuantity.setText(new StringBuilder(qty + " " + motor.satuan));
        binding.tvTotal.setText(new StringBuilder("Rp " + formatter.format(total)));
        binding.tvPpn.setText(new StringBuilder("Rp " + formatter.format(ppn)));
        binding.tvBayar.setText(new StringBuilder("Rp " + formatter.format(bayar)));
    }
}