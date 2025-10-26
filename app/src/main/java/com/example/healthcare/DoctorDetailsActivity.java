package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.Adaptor.DoctorAdapter;
import com.example.healthcare.DAO.SQLite.DoctorDAOImpl;
import com.example.healthcare.Model.Doctor;

import java.util.List;

public class DoctorDetailsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private Button btnBack;
    private ListView listView;
    private DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_details);

        // Initialize views
        tvTitle = findViewById(R.id.textViewDDTitle);
        btnBack = findViewById(R.id.buttonDDBack);
        listView = findViewById(R.id.listviewDD);

        // Get specialty title from intent
        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tvTitle.setText(title);

        // Get doctors from DAO
        DoctorDAOImpl doctorDAO = DoctorDAOImpl.getInstance();
        List<Doctor> doctors = doctorDAO.getDoctorsByMedicalSpecialty(title);
        Log.d("DoctorDetailsActivity", "Doctors found: " + doctors.size());

        // Check if doctors list is empty
        if (doctors.isEmpty()) {
            Toast.makeText(this, "No doctors found for " + title, Toast.LENGTH_SHORT).show();
        }

        // Create and set custom adapter
        adapter = new DoctorAdapter(this, doctors);
        listView.setAdapter(adapter);

        // Set item click listener for the entire list item
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Doctor selectedDoctor = doctors.get(position);
            Toast.makeText(this, "Selected: " + selectedDoctor.getDoctorName(),
                    Toast.LENGTH_SHORT).show();
        });

        // Back button click listener
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(DoctorDetailsActivity.this, FindDoctorActivity.class));
            finish();
        });
    }
}