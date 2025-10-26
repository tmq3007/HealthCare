package com.example.healthcare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthcare.Activities.ClientActivities.BookAppointmentActivity;
import com.example.healthcare.Model.Doctor;
import com.example.healthcare.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DoctorAdapter extends ArrayAdapter<Doctor> {

    private Context context;
    private List<Doctor> doctors;

    public DoctorAdapter(@NonNull Context context, List<Doctor> doctors) {
        super(context, 0, doctors);
        this.context = context;
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.multi_lines, parent, false);
            holder = new ViewHolder();
            holder.doctorName = convertView.findViewById(R.id.line_a);
            holder.specialty = convertView.findViewById(R.id.line_b);
            holder.experience = convertView.findViewById(R.id.line_c);
            holder.phone = convertView.findViewById(R.id.line_d);
            holder.bookButton = convertView.findViewById(R.id.line_e);
            holder.address = convertView.findViewById(R.id.line_address);
            holder.fee = convertView.findViewById(R.id.line_fee);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Doctor doctor = doctors.get(position);

        // Set doctor information
        holder.doctorName.setText(doctor.getDoctorName());
        holder.specialty.setText(doctor.getMedicalSpecialty());
        holder.address.setText(doctor.getHospitalAddress());
        
        // Format experience
        holder.experience.setText(doctor.getYearsOfExperience() + " yrs");
        
        // Format phone number
        holder.phone.setText(doctor.getPhoneNumber());
        
        // Format fee in Vietnamese Dong
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedFee = formatter.format(doctor.getFee());
        holder.fee.setText(formattedFee + " ₫");

        // Book button click listener
        holder.bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookAppointmentActivity.class);
            intent.putExtra("doctorId", doctor.getId());
            intent.putExtra("doctorName", doctor.getDoctorName());
            intent.putExtra("hospitalAddress", doctor.getHospitalAddress());
            intent.putExtra("phoneNumber", doctor.getPhoneNumber());
            intent.putExtra("fee", doctor.getFee());
            intent.putExtra("specialty", doctor.getMedicalSpecialty());
            context.startActivity(intent);
        });

        return convertView;
    }

    static class ViewHolder {
        TextView doctorName;
        TextView specialty;
        TextView experience;
        TextView phone;
        TextView address;
        TextView fee;
        Button bookButton;
    }
}