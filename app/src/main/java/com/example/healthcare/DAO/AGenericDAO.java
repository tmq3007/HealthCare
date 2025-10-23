package com.example.healthcare.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.healthcare.DAO.SQLite.DatabaseHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Abstract Generic DAO implementation for common SQLite operations.
 * Subclasses must provide:
 *   - tableName
 *   - mapping between entity <T> and ContentValues / Cursor
 *   - SQL create table statement
 */
public abstract class AGenericDAO<T> implements IGenericDAO<T> {

    protected DatabaseHelper databaseHelper = null;
    protected String tableName = null;
    protected final String columnId = "id";
    protected Class<T> modelClass;

    @Override
    public IGenericDAO<T> setHelper(DatabaseHelper helper) {
        if (databaseHelper == null) {
            databaseHelper = helper;
        }
        return this;
    }

    // ===== ABSTRACT METHODS THAT MUST BE PROVIDED BY CHILD CLASSES =====
    protected abstract String getCreateTableSQL();

    // ========================= BASIC DAO OPS ===========================

    @Override
    public boolean createTable() {
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.execSQL(getCreateTableSQL());
            Log.d("AGenericDAO", "Table created: " + tableName);
            return true;
        } catch (Exception e) {
            Log.e("AGenericDAO", "Error creating table " + tableName + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public void deleteTable() {
        String sql = "DROP TABLE IF EXISTS " + tableName + ";";
        databaseHelper.getWritableDatabase().execSQL(sql);
        Log.d("AGenericDAO", "Table deleted: " + tableName);
    }

    @Override
    public boolean save(T entity) {
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = toContentValues(entity);
            // 👇 this line prevents inserting manual id
            if (values.containsKey("id") && values.getAsInteger("id") == 0) {
                values.remove("id");
            }
            long result = db.insert(tableName, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("AGenericDAO", "Error saving entity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(T entity) {
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = toContentValues(entity);
            int id = getIdValue(entity);
            int rows = db.update(tableName, values, columnId + "=?", new String[]{String.valueOf(id)});
            return rows > 0;
        } catch (Exception e) {
            Log.e("AGenericDAO", "Error updating entity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            int rows = db.delete(tableName, columnId + "=?", new String[]{String.valueOf(id)});
            return rows > 0;
        } catch (Exception e) {
            Log.e("AGenericDAO", "Error deleting entity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public T get(int id) {
        T entity = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            cursor = db.query(tableName, null, columnId + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor.moveToFirst()) {
                entity = fromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e("AGenericDAO", "Error getting entity by ID: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return entity;
    }

    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            cursor = db.query(tableName, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    T entity = fromCursor(cursor);
                    list.add(entity);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("AGenericDAO", "Error getting all entities: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    // ===== Helper method to extract the "id" from entity using reflection =====
    protected int getIdValue(T entity) {
        try {
            return (int) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            Log.e("AGenericDAO", "Failed to get ID from entity: " + e.getMessage());
            return -1;
        }
    }

    // ===== Utility: Print out table schema =====
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

                    Log.d("TableSchema", "Column " + cid + ": " + name +
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
            if (cursor != null) cursor.close();
        }
    }

    // ===== REFLECTION METHODS TO MAP ENTITY TO CONTENTVALUES / CURSOR =====

    private boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() ||
                type == String.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Double.class ||
                type == Float.class ||
                type == Boolean.class ||
                type == Date.class;
    }


    protected ContentValues toContentValues(T entity) {
        ContentValues values = new ContentValues();
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value == null) continue;

                if (value instanceof String) values.put(field.getName(), (String) value);
                else if (value instanceof Integer) values.put(field.getName(), (Integer) value);
                else if (value instanceof Long) values.put(field.getName(), (Long) value);
                else if (value instanceof Double) values.put(field.getName(), (Double) value);
                else if (value instanceof Float) values.put(field.getName(), (Float) value);
                else if (value instanceof Boolean) values.put(field.getName(), (Boolean) value);
                else if (value instanceof Date) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    values.put(field.getName(), sdf.format((Date) value));
                }

                else {
                    // Skip lists or custom objects (relationships)
                    if (value instanceof List || !isSimpleType(value.getClass())) {
                        Log.d("AGenericDAO", "Skipping non-primitive field: " + field.getName());
                        continue;
                    }
                }

            }
        } catch (Exception e) {
            Log.e("AGenericDAO", "Reflection toContentValues failed: " + e.getMessage());
        }
        return values;
    }

    @Override
    public T fromCursor(Cursor cursor) {
        T instance = null;
        try {
            instance = modelClass.newInstance(); // create empty model

            Field[] fields = modelClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                int columnIndex = cursor.getColumnIndex(field.getName());

                // Skip if no column found for this field
                if (columnIndex == -1) {
                    Log.d("AGenericDAO", "Skipping field (not in table): " + field.getName());
                    continue;
                }

                Class<?> type = field.getType();

                if (type == String.class) {
                    field.set(instance, cursor.getString(columnIndex));
                } else if (type == int.class || type == Integer.class) {
                    field.set(instance, cursor.getInt(columnIndex));
                } else if (type == long.class || type == Long.class) {
                    field.set(instance, cursor.getLong(columnIndex));
                } else if (type == double.class || type == Double.class) {
                    field.set(instance, cursor.getDouble(columnIndex));
                } else if (type == float.class || type == Float.class) {
                    field.set(instance, cursor.getFloat(columnIndex));
                } else if (type == boolean.class || type == Boolean.class) {
                    field.set(instance, cursor.getInt(columnIndex) == 1);
                } else if (type == Date.class) {
                    // Convert TEXT -> Date
                    String dateStr = cursor.getString(columnIndex);
                    if (dateStr != null && !dateStr.isEmpty()) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            field.set(instance, sdf.parse(dateStr));
                        } catch (Exception ex) {
                            Log.e("AGenericDAO", "Failed to parse date for field: " + field.getName() + " value: " + dateStr);
                        }
                    }
                } else if (List.class.isAssignableFrom(type)) {
                    Log.d("AGenericDAO", "Skipping List field: " + field.getName());
                    continue;
                } else if (!isSimpleType(type)) {
                    Log.d("AGenericDAO", "Skipping complex field: " + field.getName());
                    continue;
                } else {
                    Log.d("AGenericDAO", "Unhandled field type: " + type.getName());
                }
            }

        } catch (Exception e) {
            Log.e("AGenericDAO", "Reflection fromCursor failed for " + modelClass.getSimpleName() + ": " + e.getMessage());
        }
        return instance;
    }
    public List<T> find(String selection, String[] selectionArgs) {
        List<T> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    T entity = fromCursor(cursor);
                    list.add(entity);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("AGenericDAO", "Error finding entities: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

}
