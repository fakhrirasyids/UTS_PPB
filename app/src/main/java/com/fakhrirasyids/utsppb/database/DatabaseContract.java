package com.fakhrirasyids.utsppb.database;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static String TABLE_NAME_MOTOR = "motor";
    public static String TABLE_NAME_CART = "cart";

    public static final class MotorColumns implements BaseColumns {
        public static String KODE = "kode";
        public static String NAMA = "nama";
        public static String SATUAN = "satuan";
        public static String HARGA = "harga";
        public static String JUMLAH = "jumlah";
        public static String GAMBAR = "gambar";
    }

    public static final class CartColumns implements BaseColumns {
        public static String KODE = "kode";
        public static String NAMA = "nama";
        public static String SATUAN = "satuan";
        public static String HARGA = "harga";
        public static String JUMLAH = "jumlah";
        public static String TERJUAL = "terjual";
        public static String GAMBAR = "gambar";
    }
}
