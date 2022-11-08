package com.fakhrirasyids.utsppb.ui.main;

import static com.fakhrirasyids.utsppb.database.DatabaseContract.TABLE_NAME_MOTOR;
import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_MOTOR;
import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_POSITION;
import static com.fakhrirasyids.utsppb.utils.Constants.RESULT_ADD;
import static com.fakhrirasyids.utsppb.utils.Constants.RESULT_DELETE;
import static com.fakhrirasyids.utsppb.utils.Constants.RESULT_UPDATE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.fakhrirasyids.utsppb.R;
import com.fakhrirasyids.utsppb.callback.LoadMotorCallback;
import com.fakhrirasyids.utsppb.database.MotorHelper;
import com.fakhrirasyids.utsppb.databinding.ActivityMainBinding;
import com.fakhrirasyids.utsppb.entity.Motor;
import com.fakhrirasyids.utsppb.helper.MappingHelper;
import com.fakhrirasyids.utsppb.callback.OnItemClickCallback;
import com.fakhrirasyids.utsppb.ui.adapter.MotorAdapter;
import com.fakhrirasyids.utsppb.ui.add.AddActivity;
import com.fakhrirasyids.utsppb.ui.cart.CartActivity;
import com.fakhrirasyids.utsppb.ui.detail.DetailActivity;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements LoadMotorCallback {
    ActivityMainBinding binding;

    MotorAdapter adapter;

    final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    if (result.getResultCode() == RESULT_ADD) {
                        Motor motor = result.getData().getParcelableExtra(EXTRA_MOTOR);

                        adapter.addItem(motor);
                        binding.rvList.smoothScrollToPosition(adapter.getItemCount() - 1);

                        showSnackbarMessage("Item berhasil ditambahkan!");
                    } else if (result.getResultCode() == RESULT_UPDATE) {
                        Motor motor = result.getData().getParcelableExtra(EXTRA_MOTOR);
                        int position = result.getData().getIntExtra(EXTRA_POSITION, 0);

                        adapter.updateItem(position, motor);
                        binding.rvList.smoothScrollToPosition(position);

                        showSnackbarMessage("Item berhasil diupdate!");
                    } else if (result.getResultCode() == RESULT_DELETE) {
                        int position = result.getData().getIntExtra(EXTRA_POSITION, 0);

                        adapter.removeItem(position);

                        showSnackbarMessage("Item berhasil dihapus!");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new MotorAdapter(new OnItemClickCallback() {
            @Override
            public void onMotorClicked(Motor selectedMotor, int position) {
                Intent iDetail = new Intent(MainActivity.this, DetailActivity.class);
                iDetail.putParcelableArrayListExtra(EXTRA_MOTOR, selectedMotor);
                iDetail.putExtra(EXTRA_POSITION, position);
                resultLauncher.launch(iDetail);
            }
        });

        binding.rvList.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvList.setAdapter(adapter);

        binding.btnAddMotor.setOnClickListener(v -> {
            Intent iAdd = new Intent(MainActivity.this, AddActivity.class);
            resultLauncher.launch(iAdd);
        });

        new LoadMotorAsync(getApplicationContext(), this).execute();
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(binding.rvList, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void preExecute() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<Motor> motor) {
        binding.progressBar.setVisibility(View.GONE);
        if (motor.size() > 0) {
            adapter.setMotorList(motor);
        } else {
            adapter.setMotorList(new ArrayList<Motor>());
            showSnackbarMessage("Tidak ada data saat ini!");
        }
    }

    public static class LoadMotorAsync {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMotorCallback> weakCallback;

        private LoadMotorAsync(Context context, LoadMotorCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        void execute() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            weakCallback.get().preExecute();
            executor.execute(() -> {
                Context context = weakContext.get();

                MotorHelper motorHelper = MotorHelper.getInstance(context, TABLE_NAME_MOTOR);
                motorHelper.open();
                Cursor dataCursor = motorHelper.queryAll(TABLE_NAME_MOTOR);
                ArrayList<Motor> motorList = MappingHelper.mapCursorToArrayList(dataCursor);
                motorHelper.close();

                handler.post(() -> weakCallback.get().postExecute(motorList));
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.list_mode) {
            binding.rvList.setAdapter(adapter);
            adapter.toggleViewMode(false);
            binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else if (item.getItemId() == R.id.grid_mode) {
            binding.rvList.setAdapter(adapter);
            adapter.toggleViewMode(true);
            binding.rvList.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (item.getItemId() == R.id.btn_cart) {
            Intent iCart = new Intent(MainActivity.this, CartActivity.class);
            startActivity(iCart);
        }
        return super.onOptionsItemSelected(item);
    }
}