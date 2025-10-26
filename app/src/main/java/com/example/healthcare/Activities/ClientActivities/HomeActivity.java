package com.example.healthcare.Activities.ClientActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.healthcare.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // --- Retrieve and greet logged-in user ---
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Toast.makeText(getApplicationContext(), "Welcome " + username, Toast.LENGTH_SHORT).show();

        // --- Logout card ---
        CardView exit = findViewById(R.id.cardExit);
        exit.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });

        // --- Find Doctor card ---
        CardView findDoctor = findViewById(R.id.cardFindDoctor);
        findDoctor.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, FindDoctorActivity.class));
        });

        // --- My Bookings card ---
        CardView myBookings = findViewById(R.id.cardMyBookings);
        myBookings.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MyBookingActivity.class));
        });
    }
}
