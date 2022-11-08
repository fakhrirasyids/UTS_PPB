package com.fakhrirasyids.utsppb.ui.cart;

import static com.fakhrirasyids.utsppb.database.DatabaseContract.TABLE_NAME_CART;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.TABLE_NAME_MOTOR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.fakhrirasyids.utsppb.callback.LoadMotorCallback;
import com.fakhrirasyids.utsppb.callback.LoadMotorCartCallback;
import com.fakhrirasyids.utsppb.database.MotorHelper;
import com.fakhrirasyids.utsppb.databinding.ActivityCartBinding;
import com.fakhrirasyids.utsppb.entity.Motor;
import com.fakhrirasyids.utsppb.entity.MotorCart;
import com.fakhrirasyids.utsppb.helper.MappingHelper;
import com.fakhrirasyids.utsppb.ui.adapter.CartAdapter;
import com.fakhrirasyids.utsppb.ui.main.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity implements LoadMotorCartCallback {
    ActivityCartBinding binding;

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new CartAdapter();
        binding.rvCart.setAdapter(adapter);
        binding.rvCart.setLayoutManager(new LinearLayoutManager(this));

        new LoadMotorAsync(getApplicationContext(), this).execute();
    }

    @Override
    public void preExecute() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<MotorCart> motorCart) {
        binding.progressBar.setVisibility(View.GONE);
        if (motorCart.size() > 0) {
            adapter.setMotorList(motorCart);
        } else {
            adapter.setMotorList(new ArrayList<MotorCart>());
            showSnackbarMessage("Tidak ada data di keranjang saat ini!");
        }
    }

    public static class LoadMotorAsync {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMotorCartCallback> weakCallback;

        private LoadMotorAsync(Context context, LoadMotorCartCallback callback) {
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
                Cursor dataCursor = motorHelper.queryAll(TABLE_NAME_CART);
                ArrayList<MotorCart> motorList = MappingHelper.mapCursorToArrayListCart(dataCursor);
                motorHelper.close();

                handler.post(() -> weakCallback.get().postExecute(motorList));
            });
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(binding.rvCart, message, Snackbar.LENGTH_SHORT).show();
    }
}