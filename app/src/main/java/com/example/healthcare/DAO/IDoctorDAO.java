package com.example.healthcare.DAO;

import com.example.healthcare.Model.Doctor;

import java.util.List;

public interface IDoctorDAO extends IGenericDAO<Doctor> {

    List<Doctor> getDoctorsByMedicalSpecialty(String specialty);
    List<Doctor> getDoctorsByName(String name);
}
