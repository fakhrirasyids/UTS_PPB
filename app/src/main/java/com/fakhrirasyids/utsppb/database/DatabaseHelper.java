package com.fakhrirasyids.utsppb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmotorapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOTOR = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_NAME_MOTOR,
            DatabaseContract.MotorColumns._ID,
            DatabaseContract.MotorColumns.KODE,
            DatabaseContract.MotorColumns.NAMA,
            DatabaseContract.MotorColumns.SATUAN,
            DatabaseContract.MotorColumns.HARGA,
            DatabaseContract.MotorColumns.JUMLAH,
            DatabaseContract.MotorColumns.GAMBAR
    );

    private static final String SQL_CREATE_TABLE_CART = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_NAME_CART,
            DatabaseContract.CartColumns._ID,
            DatabaseContract.CartColumns.KODE,
            DatabaseContract.CartColumns.NAMA,
            DatabaseContract.CartColumns.SATUAN,
            DatabaseContract.CartColumns.HARGA,
            DatabaseContract.CartColumns.JUMLAH,
            DatabaseContract.CartColumns.TERJUAL,
            DatabaseContract.CartColumns.GAMBAR
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOTOR);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NAME_MOTOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NAME_CART);
        onCreate(sqLiteDatabase);
    }
}
