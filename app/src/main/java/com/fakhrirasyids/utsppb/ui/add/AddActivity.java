package com.fakhrirasyids.utsppb.ui.add;

import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.GAMBAR;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.HARGA;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.JUMLAH;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.KODE;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.NAMA;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.MotorColumns.SATUAN;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.TABLE_NAME_MOTOR;
import static com.fakhrirasyids.utsppb.utils.Constants.ALERT_DIALOG_CLOSE;
import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_MOTOR;
import static com.fakhrirasyids.utsppb.utils.Constants.EXTRA_POSITION;
import static com.fakhrirasyids.utsppb.utils.Constants.RESULT_ADD;
import static com.fakhrirasyids.utsppb.utils.Constants.RESULT_DELETE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.fakhrirasyids.utsppb.R;
import com.fakhrirasyids.utsppb.database.DatabaseContract;
import com.fakhrirasyids.utsppb.database.MotorHelper;
import com.fakhrirasyids.utsppb.databinding.ActivityAddBinding;
import com.fakhrirasyids.utsppb.entity.Motor;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;

    MotorHelper motorHelper;
    int quantity;
    String image;

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.ivMotorProfile.setImageBitmap(bitmap);
                            image = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        motorHelper = MotorHelper.getInstance(getApplicationContext(), TABLE_NAME_MOTOR);
        motorHelper.open();

        String[] satuan = getResources().getStringArray(R.array.satuan);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, satuan);
        binding.etSatuanMotor.setAdapter(arrayAdapter);

        setListeners();
    }

    private void setListeners() {
        quantity = 1;

        binding.btnChangeImage.setOnClickListener(v -> {
            Intent iPickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            iPickImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(iPickImage);
        });

        binding.plusBtn.setOnClickListener(v -> {
            quantity++;
            binding.tvQty.setText(String.valueOf(quantity));
        });

        binding.removeBtn.setOnClickListener(v -> {
            quantity--;
            if (quantity < 1) {
                quantity = 1;
            }
            binding.tvQty.setText(String.valueOf(quantity));
        });

        binding.btnAddMotor.setOnClickListener(v -> {
            if (isValidAddDetails()) {
                String image = this.image;
                String kode = binding.etKodeMotor.getText().toString();
                String nama = binding.etNamaMotor.getText().toString();
                String satuan = binding.etSatuanMotor.getText().toString();
                String harga = binding.etHargaMotor.getText().toString();
                String jumlah = String.valueOf(quantity);

                Motor motor = new Motor();
                motor.gambar = image;
                motor.kode = kode;
                motor.nama = nama;
                motor.satuan = satuan;
                motor.harga = harga;
                motor.jumlah = jumlah;

                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(EXTRA_MOTOR, motor);

                ContentValues values = new ContentValues();
                values.put(KODE, kode);
                values.put(NAMA, nama);
                values.put(SATUAN, satuan);
                values.put(HARGA, harga);
                values.put(JUMLAH, jumlah);
                values.put(GAMBAR, image);

                long result = motorHelper.insert(values, TABLE_NAME_MOTOR);

                if (result > 0) {
                    motor.id = (int) result;
                    setResult(RESULT_ADD, intent);
                    finish();
                } else {
                    showToast("Gagal menambahkan data!");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private boolean isValidAddDetails() {
        if (image == null) {
            showToast("Tambahkan gambar terlebih dahulu!");
            return false;
        } else if (binding.etKodeMotor.getText().toString().trim().isEmpty()) {
            showToast("Masukkan kode motor terlebih dahulu!");
            return false;
        } else if (binding.etNamaMotor.getText().toString().trim().isEmpty()) {
            showToast("Masukkan nama motor terlebih dahulu!");
            return false;
        } else if (!binding.etHargaMotor.getText().toString().matches(".*\\d.*")) {
            showToast("Masukkan harga dengan benar!");
            return false;
        } else {
            return true;
        }
    }

    public String encodeImage(Bitmap bitmap) {
        int previewWidth = 200;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showAlertDialog() {
        String dialogTitle, dialogMessage;
        dialogTitle = "Batal";
        dialogMessage = "Apakah anda ingin membatalkan penambahan data?";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    finish();
                })
                .setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}