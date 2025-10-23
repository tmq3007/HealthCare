package com.example.healthcare.DAO.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.healthcare.DAO.AGenericDAO;
import com.example.healthcare.DAO.IDoctorDAO;
import com.example.healthcare.Model.Doctor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoctorDAOImpl extends AGenericDAO<Doctor> implements IDoctorDAO {
    private static DoctorDAOImpl instance;

    private final String columnId = "id";
    private final String columnDoctorName = "doctorName";
    private final String columnHospitalAddress = "hospitalAddress";
    private final String columnYearsOfExperience = "yearsOfExperience";
    private final String columnPhoneNumber = "phoneNumber";
    private final String columnFee = "fee";
    private final String columnMedicalSpecialty = "medicalSpecialty";

    private DoctorDAOImpl() { tableName = "doctors"; modelClass=Doctor.class;}

    public static synchronized DoctorDAOImpl getInstance() {
        if (instance == null) {
            instance = new DoctorDAOImpl();
        }
        return instance;
    }

    @Override
    public List<Doctor> getDoctorsByMedicalSpecialty(String specialty) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Doctor> doctors = new ArrayList<>();

        Cursor cursor = database.query(
                tableName,
                new String[]{columnId, columnDoctorName, columnHospitalAddress, columnYearsOfExperience, columnPhoneNumber, columnFee, columnMedicalSpecialty},
                columnMedicalSpecialty + " = ?",
                new String[]{specialty},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor();
                doctor.setId(cursor.getInt(cursor.getColumnIndexOrThrow(columnId)));
                doctor.setDoctorName(cursor.getString(cursor.getColumnIndexOrThrow(columnDoctorName)));
                doctor.setHospitalAddress(cursor.getString(cursor.getColumnIndexOrThrow(columnHospitalAddress)));
                doctor.setYearsOfExperience(cursor.getInt(cursor.getColumnIndexOrThrow(columnYearsOfExperience)));
                doctor.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(columnPhoneNumber)));
                doctor.setFee(cursor.getInt(cursor.getColumnIndexOrThrow(columnFee)));
                doctor.setMedicalSpecialty(cursor.getString(cursor.getColumnIndexOrThrow(columnMedicalSpecialty)));

                doctors.add(doctor);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return doctors;
    }
    @Override
    public List<Doctor> getDoctorsByName(String name) {
        return Collections.emptyList();
    }

    @Override
    protected String getCreateTableSQL() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                columnId + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                columnDoctorName + " TEXT" + "," +
                columnHospitalAddress + " TEXT" + "," +
                columnYearsOfExperience + " INTEGER" + "," +
                columnPhoneNumber + " TEXT" + "," +
                columnFee + " INTEGER" + "," +
                columnMedicalSpecialty + " TEXT" +
                ");";
        return sql;
    }
}
