package com.example.healthcare.DAO;

import com.example.healthcare.Model.Doctor;

import java.util.List;

public interface IDoctorDAO extends IGenericDAO {

    boolean save(Doctor doctor);
    void update(Doctor doctor);
    Doctor getDoctorById(int id);
    List<Doctor> getAllDoctors();
    List<Doctor> getDoctorsByName(String name);
}
