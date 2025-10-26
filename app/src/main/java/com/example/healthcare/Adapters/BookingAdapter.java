package com.example.healthcare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.healthcare.DAO.SQLite.BookingDAOImpl;
import com.example.healthcare.DAO.SQLite.DoctorDAOImpl;
import com.example.healthcare.Model.Booking;
import com.example.healthcare.Model.Doctor;
import com.example.healthcare.R;

import java.util.List;

public class BookingAdapter extends BaseAdapter {

    private final Context context;
    private final List<Booking> bookings;

    public BookingAdapter(Context context, List<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    @Override
    public int getCount() {
        return bookings.size();
    }

    @Override
    public Object getItem(int position) {
        return bookings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookings.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        }

        Booking booking = bookings.get(position);

        TextView tvDoctor = convertView.findViewById(R.id.tvDoctorName);
        TextView tvDate = convertView.findViewById(R.id.tvBookingDate);
        TextView tvStatus = convertView.findViewById(R.id.tvBookingStatus);

        DoctorDAOImpl doctorDAO = DoctorDAOImpl.getInstance();
        Doctor doctor = doctorDAO.get(booking.getDoctorId());
        tvDoctor.setText(doctor.getDoctorName());
        tvDate.setText(booking.getBookingDate());
        tvStatus.setText(booking.getStatus());

        return convertView;
    }
}
