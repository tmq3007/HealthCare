package com.example.healthcare.Model;

public class Booking {
    private int id;
    private Doctor doctor;
    private int doctorId;
    private String patientName;
    private String patientAddress;
    private String patientPhone;
    private int userId;
    private int fee;
    private String appointmentDate;
    private String appointmentTime;
    private String bookingDate; // When the booking was made
    private String status; // "pending", "confirmed", "completed", "cancelled"

    public Booking(int doctorId, String patientName, String patientAddress, String patientPhone, int userId, int fee, String appointmentDate, String appointmentTime, String bookingDate, String status, int id) {
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.patientAddress = patientAddress;
        this.patientPhone = patientPhone;
        this.userId = userId;
        this.fee = fee;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.bookingDate = bookingDate;
        this.status = status;
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
