package com.example.healthcare.DAO.SQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.healthcare.DAO.AGenericDAO;
import com.example.healthcare.DAO.IBookingDAO;
import com.example.healthcare.Model.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl extends AGenericDAO<Booking> implements IBookingDAO {
    private static BookingDAOImpl instance;

    private final String columnId = "id";
    private final String columnDoctorId = "doctorId";
    private final String columnPatientName = "patientName";
    private final String columnPatientAddress = "patientAddress";
    private final String columnPatientPhone = "patientPhone";
    private final String columnUserId = "userId";
    private final String columnFee = "fee";
    private final String columnAppointmentDate = "appointmentDate";
    private final String columnAppointmentTime = "appointmentTime";
    private final String columnBookingDate = "bookingDate";
    private final String columnStatus = "status";

    private BookingDAOImpl() {
        tableName = "bookings";
        modelClass = Booking.class;
    }

    public static synchronized BookingDAOImpl getInstance() {
        if (instance == null) {
            instance = new BookingDAOImpl();
        }
        return instance;
    }

    @Override
    protected String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                columnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                columnDoctorId + " INTEGER, " +
                columnPatientName + " TEXT, " +
                columnPatientAddress + " TEXT, " +
                columnPatientPhone + " TEXT, " +
                columnUserId + " INTEGER, " +
                columnFee + " INTEGER, " +
                columnAppointmentDate + " TEXT, " +
                columnAppointmentTime + " TEXT, " +
                columnBookingDate + " TEXT, " +
                columnStatus + " TEXT" +
                ");";
    }

    @Override
    public boolean isTimeSlotAvailable(int doctorId, String selectedDate, String selectedTime) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                tableName,
                new String[]{columnId},
                columnDoctorId + "=? AND " + columnAppointmentDate + "=? AND " + columnAppointmentTime + "=? AND " + columnStatus + "!='cancelled'",
                new String[]{String.valueOf(doctorId), selectedDate, selectedTime},
                null, null, null
        );

        boolean available = !cursor.moveToFirst(); // if no rows found → available
        cursor.close();
        return available;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();

        Cursor cursor = db.query(
                tableName,
                null,
                columnUserId + "=?",
                new String[]{String.valueOf(userId)},
                null, null, columnBookingDate + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                        cursor.getInt(cursor.getColumnIndexOrThrow(columnDoctorId)),
                        cursor.getString(cursor.getColumnIndexOrThrow(columnPatientName)),
                        cursor.getString(cursor.getColumnIndexOrThrow(columnPatientAddress)),
                        cursor.getString(cursor.getColumnIndexOrThrow(columnPatientPhone)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(columnUserId)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(columnFee)),
                        cursor.getString(cursor.getColumnIndexOrThrow(columnAppointmentDate)),
                        cursor.getString(cursor.getColumnIndexOrThrow(columnAppointmentTime)),
                        cursor.getString(cursor.getColumnIndexOrThrow(columnBookingDate)),
                        cursor.getString(cursor.getColumnIndexOrThrow(columnStatus)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(columnId))
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bookings;
    }
}
