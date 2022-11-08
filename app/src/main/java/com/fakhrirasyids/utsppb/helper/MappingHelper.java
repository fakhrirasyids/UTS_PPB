package com.fakhrirasyids.utsppb.helper;

import android.database.Cursor;

import com.fakhrirasyids.utsppb.database.DatabaseContract;
import com.fakhrirasyids.utsppb.entity.Motor;
import com.fakhrirasyids.utsppb.entity.MotorCart;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<Motor> mapCursorToArrayList(Cursor motorCursor) {
        ArrayList<Motor> motorList = new ArrayList<>();

        while (motorCursor.moveToNext()) {
            int id = motorCursor.getInt(motorCursor.getColumnIndexOrThrow(DatabaseContract.MotorColumns._ID));
            String kode = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.MotorColumns.KODE));
            String nama = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.MotorColumns.NAMA));
            String satuan = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.MotorColumns.SATUAN));
            String harga = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.MotorColumns.HARGA));
            String jumlah = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.MotorColumns.JUMLAH));
            String gambar = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.MotorColumns.GAMBAR));

            Motor motor = new Motor();
            motor.id = id;
            motor.kode = kode;
            motor.nama = nama;
            motor.satuan = satuan;
            motor.harga = harga;
            motor.jumlah = jumlah;
            motor.gambar = gambar;
            motorList.add(motor);
        }

        return motorList;
    };

    public static ArrayList<MotorCart> mapCursorToArrayListCart (Cursor motorCursor) {
        ArrayList<MotorCart> motorCart = new ArrayList<>();

        while (motorCursor.moveToNext()) {
            MotorCart motor = new MotorCart();

            int id = motorCursor.getInt(motorCursor.getColumnIndexOrThrow(DatabaseContract.CartColumns._ID));
            String kode = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.CartColumns.KODE));
            String nama = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.CartColumns.NAMA));
            String satuan = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.CartColumns.SATUAN));
            String harga = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.CartColumns.HARGA));
            String jumlah = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.CartColumns.JUMLAH));
            String terjual = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.CartColumns.TERJUAL));
            String gambar = motorCursor.getString(motorCursor.getColumnIndexOrThrow(DatabaseContract.CartColumns.GAMBAR));

            motor.id = id;
            motor.kode = kode;
            motor.nama = nama;
            motor.satuan = satuan;
            motor.harga = harga;
            motor.jumlah = jumlah;
            motor.terjual = terjual;
            motor.gambar = gambar;
            motorCart.add(motor);
        }

        return motorCart;
    };
}
