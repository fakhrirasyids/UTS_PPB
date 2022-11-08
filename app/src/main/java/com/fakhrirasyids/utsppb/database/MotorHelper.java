package com.fakhrirasyids.utsppb.database;

import static android.provider.BaseColumns._ID;
import static com.fakhrirasyids.utsppb.database.DatabaseContract.TABLE_NAME_MOTOR;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MotorHelper {
//    private static final String DATABASE_TABLE = TABLE_NAME_MOTOR;
    private static String DATABASE_TABLE;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;

    private MotorHelper(Context context, String table) {
        DATABASE_TABLE = table;
        databaseHelper = new DatabaseHelper(context);
    }

    private static MotorHelper INSTANCE;

    public static MotorHelper getInstance(Context context, String table) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MotorHelper(context, table);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor queryAll(String table) {
        return database.query(
                table,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC"
        );
    }

    public Cursor queryById(String id, String table) {
        return database.query(
                table,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public long insert(ContentValues values, String table) {
        return database.insert(table, null, values);
    }

    public int update(String id, ContentValues values, String table) {
        return database.update(table, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = " + id, null);
    }
}
