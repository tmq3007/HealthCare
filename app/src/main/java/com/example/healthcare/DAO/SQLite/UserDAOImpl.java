package com.example.healthcare.DAO.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.healthcare.DAO.AGenericDAO;
import com.example.healthcare.DAO.IUserDAO;
import com.example.healthcare.Model.User;

public class UserDAOImpl extends AGenericDAO<User> implements IUserDAO {
    private static UserDAOImpl instance;
    private User currentUser;

    private final String columnId = "id";
    private final String columnUsername = "username";
    private final String columnFullname = "fullname";
    private final String columnEmail = "email";
    private final String columnPhoneNumber = "phoneNumber";
    private final String columnPassword = "password";

    private UserDAOImpl() {
        tableName = "users";
        modelClass = User.class;
    }

    public static synchronized UserDAOImpl getInstance() {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    @Override
    protected String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                columnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnFullname + " TEXT, " +
                columnUsername + " TEXT UNIQUE, " +
                columnEmail + " TEXT, " +
                columnPhoneNumber + " TEXT, " +
                columnPassword + " TEXT" +
                ");";
    }

    @Override
    public void register(String username, String email, String password) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(columnUsername, username);
        values.put(columnEmail, email);
        values.put(columnPassword, password);
        db.insert(tableName, null, values);
    }

    @Override
    public User login(String username, String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(
                tableName,
                null,
                columnUsername + "=? AND " + columnPassword + "=?",
                new String[]{username, password},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            currentUser = extractUserFromCursor(cursor);
        }
        cursor.close();

        return currentUser;
    }

    @Override
    public User getUserByUsername(String username) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                tableName,
                null,
                columnUsername + "=?",
                new String[]{username},
                null, null, null
        );

        User user = null;
        if (cursor.moveToFirst()) {
            user = extractUserFromCursor(cursor);
        }
        cursor.close();
        return user;
    }

    /**
     * ✅ Improved: Retrieve current user, even after app restarts.
     * If currentUser is null, try loading it from SharedPreferences.
     */
    @Override
    public User getCurrentUser(Context context) {
        if (currentUser != null) {
            return currentUser;
        }

        SharedPreferences prefs = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username == null) return null;

        currentUser = getUserByUsername(username);
        return currentUser;
    }

    @Override
    public int getCurrentUserId(Context context) {
        User user = getCurrentUser(context);
        return user == null ? -1 : user.getId();
    }

    @Override
    public void logout(Context context) {
        currentUser = null;
        SharedPreferences prefs = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    // ✅ Helper to avoid duplicate cursor parsing
    private User extractUserFromCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(columnId)));
        user.setFullname(cursor.getString(cursor.getColumnIndexOrThrow(columnFullname)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(columnUsername)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(columnEmail)));
        user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(columnPhoneNumber)));
        return user;
    }
}
