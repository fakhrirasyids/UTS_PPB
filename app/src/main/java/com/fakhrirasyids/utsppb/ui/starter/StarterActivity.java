package com.fakhrirasyids.utsppb.ui.starter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fakhrirasyids.utsppb.databinding.ActivityStarterBinding;
import com.fakhrirasyids.utsppb.ui.main.MainActivity;

public class StarterActivity extends AppCompatActivity {
    ActivityStarterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStarterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iMain = new Intent(StarterActivity.this, MainActivity.class);
                startActivity(iMain);
            }
        });
    }
}