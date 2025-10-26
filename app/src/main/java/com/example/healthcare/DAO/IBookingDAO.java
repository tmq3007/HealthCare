package com.example.healthcare.DAO;

import com.example.healthcare.Model.Booking;

public interface IBookingDAO extends IGenericDAO<Booking>{

    boolean isTimeSlotAvailable(int doctorId, String selectedDate, String selectedTime);
}
