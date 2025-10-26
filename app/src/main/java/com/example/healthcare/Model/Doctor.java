package com.example.healthcare.Model;

public class Doctor {

    private int id;
    private String doctorName;
    private String hospitalAddress;
    private int yearsOfExperience;
    private String phoneNumber;
    private int fee;
    private String medicalSpecialty;

    public Doctor(int id, String doctorName, String hospitalAddress, int yearsOfExperience,
                  String phoneNumber, int fee, String medicalSpecialty) {
        this.id =id;
        this.doctorName = doctorName;
        this.hospitalAddress = hospitalAddress;
        this.yearsOfExperience = yearsOfExperience;
        this.phoneNumber = phoneNumber;
        this.fee = fee;
        this.medicalSpecialty = medicalSpecialty;
    }

    public Doctor(){}

    public int getId() {return id;}
    public String getDoctorName() { return doctorName; }
    public String getHospitalAddress() { return hospitalAddress; }
    public int getYearsOfExperience() { return yearsOfExperience; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getFee() { return fee; }
    public String getMedicalSpecialty() { return medicalSpecialty; }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public void setMedicalSpecialty(String medicalSpecialty) {
        this.medicalSpecialty = medicalSpecialty;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "Id=" + id +
                "Name='" + doctorName + '\'' +
                ", Address='" + hospitalAddress + '\'' +
                ", Exp=" + yearsOfExperience + " yrs" +
                ", Phone='" + phoneNumber + '\'' +
                ", Fee=" + fee +
                ", Specialty='" + medicalSpecialty + '\'' +
                '}';
    }
}
