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

public class DoctorDAOImpl extends AGenericDAO implements IDoctorDAO {

    private final String columnId = "id";
    private final String columnDoctorName = "doctorName";
    private final String columnHospitalAddress = "hospitalAddress";
    private final String columnYearsOfExperience = "yearsOfExperience";
    private final String columnPhoneNumber = "phoneNumber";
    private final String columnFee = "fee";
    private final String columnMedicalSpecialty = "medicalSpecialty";

    private DoctorDAOImpl() { tableName = "doctors"; }

    @Override
    public synchronized boolean save(Doctor doctor) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(columnDoctorName, doctor.getDoctorName());
            values.put(columnHospitalAddress, doctor.getHospitalAddress());
            values.put(columnYearsOfExperience, doctor.getYearsOfExperience());
            values.put(columnPhoneNumber, doctor.getPhoneNumber());
            values.put(columnFee, doctor.getFee());
            values.put(columnMedicalSpecialty, doctor.getMedicalSpecialty());
            database.insert(tableName, null, values);
            database.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e(tableName, "Error on saving book", e);
            return false;
        } finally {
            database.endTransaction();
        }
        return true;    }

    @Override
    public synchronized void update(Doctor doctor) {
        ContentValues values = new ContentValues();
        values.put(columnDoctorName, doctor.getDoctorName());
        values.put(columnHospitalAddress, doctor.getHospitalAddress());
        values.put(columnYearsOfExperience, doctor.getYearsOfExperience());
        values.put(columnPhoneNumber, doctor.getPhoneNumber());
        values.put(columnFee, doctor.getFee());
        values.put(columnMedicalSpecialty, doctor.getMedicalSpecialty());

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.update(tableName, values,columnId + " = ?",
                new String[]{String.valueOf(doctor.getId())});

    }

    @Override
    public Doctor getDoctorById(int id) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(
                tableName,
                new String[]{columnId, columnDoctorName, columnHospitalAddress, columnYearsOfExperience, columnPhoneNumber, columnFee, columnMedicalSpecialty},
                columnId + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        Doctor doctor = null;
        if (cursor.moveToFirst()) {
            doctor = new Doctor();
            doctor.setId(id);
            doctor.setDoctorName(cursor.getString(cursor.getColumnIndexOrThrow(columnDoctorName)));
            doctor.setHospitalAddress(cursor.getString(cursor.getColumnIndexOrThrow(columnHospitalAddress)));
            doctor.setYearsOfExperience(cursor.getInt(cursor.getColumnIndexOrThrow(columnYearsOfExperience)));
            doctor.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(columnPhoneNumber)));
            doctor.setFee(cursor.getInt(cursor.getColumnIndexOrThrow(columnFee)));
            doctor.setMedicalSpecialty(cursor.getString(cursor.getColumnIndexOrThrow(columnMedicalSpecialty)));


        }
            cursor.close();
            return doctor;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(
                tableName,
                new String[]{columnId, columnDoctorName, columnHospitalAddress, columnYearsOfExperience, columnPhoneNumber, columnFee, columnMedicalSpecialty},
                "*",
                null,
                null, null, null
        );

        List<Doctor> allDoctors = new ArrayList<>();
        Doctor doctor = null;
        if (cursor.moveToFirst()) {
            do {
                doctor = new Doctor();
                doctor.setId(cursor.getInt(cursor.getColumnIndexOrThrow(columnId)));
                doctor.setDoctorName(cursor.getString(cursor.getColumnIndexOrThrow(columnDoctorName)));
                doctor.setHospitalAddress(cursor.getString(cursor.getColumnIndexOrThrow(columnHospitalAddress)));
                doctor.setYearsOfExperience(cursor.getInt(cursor.getColumnIndexOrThrow(columnYearsOfExperience)));
                doctor.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(columnPhoneNumber)));
                doctor.setFee(cursor.getInt(cursor.getColumnIndexOrThrow(columnFee)));
                doctor.setMedicalSpecialty(cursor.getString(cursor.getColumnIndexOrThrow(columnMedicalSpecialty)));

                // Add to list
                allDoctors.add(doctor);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return allDoctors;
    }

    @Override
    public List<Doctor> getDoctorsByName(String name) {
        return Collections.emptyList();
    }

    @Override
    public boolean createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                columnId + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                columnDoctorName + " TEXT" + "," +
                columnHospitalAddress + " TEXT" + "," +
                columnYearsOfExperience + " INTEGER" + "," +
                columnPhoneNumber + " TEXT" + "," +
                columnFee + " INTEGER" + "," +
                columnMedicalSpecialty + " TEXT" +
                ");";
        try {
            databaseHelper.getWritableDatabase().execSQL(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
