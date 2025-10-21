package com.example.healthcare.DAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.healthcare.DAO.SQLite.DatabaseHelper;


public abstract class AGenericDAO implements IGenericDAO {

    protected DatabaseHelper databaseHelper = null;
    protected String tableName = null;
    protected final String columnId = "id";

    @Override
    public IGenericDAO setHelper(DatabaseHelper helper) {
        if (databaseHelper == null) {
            databaseHelper = helper;
        }
        return this;
    }

    public synchronized void delete(long id) {
        databaseHelper.getWritableDatabase().
                delete(tableName, columnId + " = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void deleteTable() {
        String sql = "DROP TABLE IF EXISTS " + tableName + ";";
        databaseHelper.getWritableDatabase().execSQL(sql);
    }

    /**
     * Get table schema using PRAGMA table_info
     * This shows column names, types, constraints, etc.
     */
    public void getTableSchema(String tableName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "PRAGMA table_info(" + tableName + ")";
            cursor = db.rawQuery(query, null);

            Log.d("TableSchema", "=== SCHEMA FOR TABLE: " + tableName + " ===");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int cid = cursor.getInt(cursor.getColumnIndexOrThrow("cid"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                    int notNull = cursor.getInt(cursor.getColumnIndexOrThrow("notnull"));
                    String defaultValue = cursor.getString(cursor.getColumnIndexOrThrow("dflt_value"));
                    int pk = cursor.getInt(cursor.getColumnIndexOrThrow("pk"));

                    Log.d("TableSchema",
                            "Column " + cid + ": " + name +
                                    " | Type: " + type +
                                    " | NotNull: " + (notNull == 1 ? "YES" : "NO") +
                                    " | Default: " + (defaultValue != null ? defaultValue : "NULL") +
                                    " | PrimaryKey: " + (pk == 1 ? "YES" : "NO"));

                } while (cursor.moveToNext());
            } else {
                Log.d("TableSchema", "Table not found or no columns: " + tableName);
            }

        } catch (Exception e) {
            Log.e("TableSchema", "Error getting table schema: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
