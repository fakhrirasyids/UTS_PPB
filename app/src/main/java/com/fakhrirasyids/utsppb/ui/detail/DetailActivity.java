package com.fakhrirasyids.utsppb.ui.detail;

import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.GAMBAR;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.HARGA;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.JUMLAH;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.KODE;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.NAMA;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.SATUAN;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.TABLE_NAME_CART;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.TABLE_NAME_MOTOR;
import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_MOTOR;
import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_POSITION;
import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_QUANTITY;
import static com.fakhrirasyids.utsppb.utils.Constants.RESULT_DELETE;
import static com.fakhrirasyids.utsppb.utils.Constants.RESULT_UPDATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fakhrirasyids.utsppb.R;
import com.fakhrirasyids.utsppb.database.DatabaseContract;
import com.fakhrirasyids.utsppb.database.MotorHelper;
import com.fakhrirasyids.utsppb.databinding.ActivityDetailBinding;
import com.fakhrirasyids.utsppb.entity.Motor;
import com.fakhrirasyids.utsppb.entity.MotorCart;
import com.fakhrirasyids.utsppb.helper.MappingHelper;
import com.fakhrirasyids.utsppb.ui.order.OrderActivity;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;

    private Motor motor;
    private int position;
    private MotorHelper motorHelper;
    private int qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        motorHelper = MotorHelper.getInstance(getApplicationContext(), DatabaseContract.TABLE_NAME_MOTOR);
        motorHelper.open();

        motor = getIntent().getParcelableExtra(EXTRA_MOTOR);
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        DecimalFormat formatter = new DecimalFormat("#,###,###");

        if (Integer.parseInt(motor.jumlah) > 0) {
            qty = 1;
            binding.plusBtn.setEnabled(true);
            binding.removeBtn.setEnabled(true);
            binding.btnBuy.setEnabled(true);
        } else {
            qty = 0;
            binding.plusBtn.setEnabled(false);
            binding.removeBtn.setEnabled(false);
            binding.btnBuy.setEnabled(false);
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(Base64.decode(motor.gambar, Base64.DEFAULT), 0, Base64.decode(motor.gambar, Base64.DEFAULT).length);
        binding.ivMotor.setImageBitmap(bitmap);
        binding.tvMotorName.setText(motor.nama);
        binding.tvKode.setText(motor.kode);
        binding.tvHarga.setText(new StringBuilder("Rp " + formatter.format(Integer.parseInt(motor.harga))));
        binding.tvSatuan.setText(new StringBuilder(" / 1 " + motor.satuan));
        binding.tvStock.setText(new StringBuilder("Stock : " + motor.jumlah));
        binding.tvQty.setText(String.valueOf(qty));

        binding.plusBtn.setOnClickListener(v -> {
            qty++;
            if (qty > Integer.parseInt(motor.jumlah)) {
                qty = Integer.parseInt(motor.jumlah);
            }
            binding.tvQty.setText(String.valueOf(qty));
        });

        binding.removeBtn.setOnClickListener(v -> {
            qty--;
            if (qty < 1) {
                qty = 1;
            }
            binding.tvQty.setText(String.valueOf(qty));
        });

        binding.btnBuy.setOnClickListener(v -> {
            Intent intent = new Intent();

            Motor motorUpdate = new Motor();
            motorUpdate.id = motor.id;
            motorUpdate.gambar = motor.gambar;
            motorUpdate.kode = motor.kode;
            motorUpdate.nama = motor.nama;
            motorUpdate.satuan = motor.satuan;
            motorUpdate.harga = motor.harga;
            motorUpdate.jumlah = String.valueOf((Integer.parseInt(motor.jumlah) - qty));

            intent.putParcelableArrayListExtra(EXTRA_MOTOR, motorUpdate);
            intent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();
            values.put(KODE, motor.kode);
            values.put(NAMA, motor.nama);
            values.put(SATUAN, motor.satuan);
            values.put(HARGA, motor.harga);
            values.put(JUMLAH, String.valueOf((Integer.parseInt(motor.jumlah) - qty)));
            values.put(GAMBAR, motor.gambar);


            long result = motorHelper.update(String.valueOf(motor.id), values, TABLE_NAME_MOTOR);
            if (result > 0) {
                setResult(RESULT_UPDATE, intent);

                Cursor dataCursor = motorHelper.queryAll(TABLE_NAME_CART);
                ArrayList<MotorCart> motorCartList = new ArrayList<>();
                motorCartList = MappingHelper.mapCursorToArrayListCart(dataCursor);

                if (motorCartList.size() > 0) {

                    MotorCart motorCart = new MotorCart();

                    for (int i = 0; i < motorCartList.size(); i++) {
                        if (Objects.equals(motorCartList.get(i).kode, motor.kode)) {
                            motorCart.id = motorCartList.get(i).id;
                            motorCart.kode = motorCartList.get(i).kode;
                            motorCart.nama = motorCartList.get(i).nama;
                            motorCart.satuan = motorCartList.get(i).satuan;
                            motorCart.harga = motorCartList.get(i).harga;
                            motorCart.jumlah = motorCartList.get(i).jumlah;
                            motorCart.terjual = String.valueOf(Integer.parseInt(motorCartList.get(i).terjual) + qty);
                            motorCart.gambar = motorCartList.get(i).gambar;

                        }
                    }
                    ContentValues valuesCart = new ContentValues();
                    if (motorCart.nama == null) {
                        valuesCart.put(DatabaseContract.CartColumns.KODE, motor.kode);
                        valuesCart.put(DatabaseContract.CartColumns.NAMA, motor.nama);
                        valuesCart.put(DatabaseContract.CartColumns.SATUAN, motor.satuan);
                        valuesCart.put(DatabaseContract.CartColumns.HARGA, motor.harga);
                        valuesCart.put(DatabaseContract.CartColumns.JUMLAH, motor.jumlah);
                        valuesCart.put(DatabaseContract.CartColumns.TERJUAL, String.valueOf(qty));
                        valuesCart.put(DatabaseContract.CartColumns.GAMBAR, motor.gambar);

                        long resultCartInsert = motorHelper.insert(valuesCart, TABLE_NAME_CART);

                        if (resultCartInsert > 0) {
                            Intent iOrder = new Intent(DetailActivity.this, OrderActivity.class);
                            iOrder.putParcelableArrayListExtra(EXTRA_MOTOR, motorUpdate);
                            iOrder.putExtra(EXTRA_QUANTITY, qty);
                            startActivity(iOrder);
                            finish();
                        }
                    } else {
                        valuesCart.put(DatabaseContract.CartColumns.KODE, motorCart.kode);
                        valuesCart.put(DatabaseContract.CartColumns.NAMA, motorCart.nama);
                        valuesCart.put(DatabaseContract.CartColumns.SATUAN, motorCart.satuan);
                        valuesCart.put(DatabaseContract.CartColumns.HARGA, motorCart.harga);
                        valuesCart.put(DatabaseContract.CartColumns.JUMLAH, motorCart.jumlah);
                        valuesCart.put(DatabaseContract.CartColumns.TERJUAL, motorCart.terjual);
                        valuesCart.put(DatabaseContract.CartColumns.GAMBAR, motorCart.gambar);

                        long resultCartUpdate = motorHelper.update(String.valueOf(motorCart.id), valuesCart, TABLE_NAME_CART);

                        if (resultCartUpdate > 0) {
                            Intent iOrder = new Intent(DetailActivity.this, OrderActivity.class);
                            iOrder.putParcelableArrayListExtra(EXTRA_MOTOR, motorUpdate);
                            iOrder.putExtra(EXTRA_QUANTITY, qty);
                            startActivity(iOrder);
                            finish();
                        }
                    }
                } else {
                    ContentValues valuesCart = new ContentValues();
                    valuesCart.put(DatabaseContract.CartColumns.KODE, motor.kode);
                    valuesCart.put(DatabaseContract.CartColumns.NAMA, motor.nama);
                    valuesCart.put(DatabaseContract.CartColumns.SATUAN, motor.satuan);
                    valuesCart.put(DatabaseContract.CartColumns.HARGA, motor.harga);
                    valuesCart.put(DatabaseContract.CartColumns.JUMLAH, motor.jumlah);
                    valuesCart.put(DatabaseContract.CartColumns.TERJUAL, String.valueOf(qty));
                    valuesCart.put(DatabaseContract.CartColumns.GAMBAR, motor.gambar);

                    long resultCartInsert = motorHelper.insert(valuesCart, TABLE_NAME_CART);

                    if (resultCartInsert > 0) {
                        Intent iOrder = new Intent(DetailActivity.this, OrderActivity.class);
                        iOrder.putParcelableArrayListExtra(EXTRA_MOTOR, motorUpdate);
                        iOrder.putExtra(EXTRA_QUANTITY, qty);
                        startActivity(iOrder);
                        finish();
                    }
                }
            } else {
                Toast.makeText(this, "Gagal membeli!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog() {
        String dialogTitle, dialogMessage;
        dialogTitle = "Hapus";
        dialogMessage = "Apakah anda ingin menghapus item ini?";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    long result = motorHelper.deleteById(String.valueOf(motor.id));
                    if (result > 0) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_POSITION, position);
                        setResult(RESULT_DELETE, intent);
                        finish();
                    } else {
                        Toast.makeText(DetailActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}