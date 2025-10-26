package com.example.healthcare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.Activities.ClientActivities.LoginActivity;
import com.example.healthcare.DAO.IBookingDAO;
import com.example.healthcare.DAO.IDoctorDAO;
import com.example.healthcare.DAO.IUserDAO;
import com.example.healthcare.DAO.SQLite.BookingDAOImpl;
import com.example.healthcare.DAO.SQLite.DatabaseHelper;
import com.example.healthcare.DAO.SQLite.DoctorDAOImpl;
import com.example.healthcare.DAO.SQLite.UserDAOImpl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // === STEP 1: Initialize Database Helper ===
        DatabaseHelper helper = new DatabaseHelper(this);
        // === STEP 2: Initialize DAOs ===
        IBookingDAO bookingDAO = (IBookingDAO) BookingDAOImpl.getInstance().setHelper(helper);
        IUserDAO userDAO = (IUserDAO) UserDAOImpl.getInstance().setHelper(helper);
        IDoctorDAO doctorDAO = (IDoctorDAO) DoctorDAOImpl.getInstance().setHelper(helper);
        // === STEP 3: Use DAOs ===
        // Create
        bookingDAO.deleteTable();
        doctorDAO.createTable();
        userDAO.createTable();
        bookingDAO.createTable();
        Log.d("MainActivity", "booking table"+bookingDAO.getAll().toString());
        Log.d("MainActivity", "user table"+userDAO.getAll());
        Log.d("MainActivity", "doctor table"+doctorDAO.getAll());

        // Optional: print schema info
        ((DoctorDAOImpl)doctorDAO).getTableSchema("doctors");
        ((UserDAOImpl)userDAO).getTableSchema("users");
        ((BookingDAOImpl)bookingDAO).getTableSchema("bookings");



        // === Navigate to LoginActivity ===
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // close MainActivity so user cannot return to it
    }
}