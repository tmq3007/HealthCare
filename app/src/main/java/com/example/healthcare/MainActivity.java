package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcare.DAO.IDoctorDAO;
import com.example.healthcare.DAO.SQLite.DatabaseHelper;
import com.example.healthcare.DAO.SQLite.DoctorDAOImpl;
import com.example.healthcare.Model.Doctor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // === STEP 1: Initialize Database Helper ===
        DatabaseHelper helper = new DatabaseHelper(this);
        // === STEP 2: Initialize DAOs ===
        IDoctorDAO doctorDAO = (IDoctorDAO) DoctorDAOImpl.getInstance().setHelper(helper);
        // === STEP 3: Use DAOs ===
        doctorDAO.deleteTable();
        // Create tables
        doctorDAO.createTable();



        // Optional: print schema info
        ((DoctorDAOImpl)doctorDAO).getTableSchema("doctors");


        //TODO: remove this when have data --->
        // Insert data

        Doctor doc1 = new Doctor(0, "Dr. John Smith", "123 Main St", 10, "0901234567", 500, "Family Physicians");
        Doctor doc2 = new Doctor(0, "Dr. Alice Nguyen", "456 Park Ave", 8, "0907654321", 400, "Family Physicians");
        Doctor doc3 = new Doctor(0, "Dr. Tom Lee", "789 Lake Rd", 5, "0912345678", 600, "Dentist");

        doctorDAO.save(doc1);
        doctorDAO.save(doc2);
        doctorDAO.save(doc3);


        Log.d("MainActivity", "All Doctors"+doctorDAO.getAll());
        //TODO: remove this when have data <---

        // === Navigate to LoginActivity ===
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // close MainActivity so user cannot return to it
    }
}