package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.DAO.SQLite.DoctorDAOImpl;
import com.example.healthcare.Model.Doctor;

import java.util.List;

public class DoctorDetailsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private Button btnBack;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_details);

        tvTitle = findViewById(R.id.textViewDDTitle);
        btnBack = findViewById(R.id.buttonDDBack);
        listView = findViewById(R.id.listviewDD);

        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tvTitle.setText(title);

        // 🔹 Get data from DAO
        DoctorDAOImpl doctorDAO = DoctorDAOImpl.getInstance();
        List<Doctor> doctors = doctorDAO.getDoctorsByMedicalSpecialty(title);
        Log.d("DoctorDetailsActivity", "Doctors: " + doctors);

        // 🔹 Populate ListView
        ArrayAdapter<Doctor> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                doctors
        );
        listView.setAdapter(adapter);

        // 🔹 Back button
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(DoctorDetailsActivity.this, FindDoctorActivity.class));
            finish();
        });
    }
}
