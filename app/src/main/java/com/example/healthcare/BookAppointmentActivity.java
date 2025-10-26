package com.example.healthcare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcare.DAO.SQLite.BookingDAOImpl;
import com.example.healthcare.DAO.SQLite.UserDAOImpl;
import com.example.healthcare.Model.Booking;
import com.example.healthcare.Model.Doctor;
import com.example.healthcare.Model.User;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookAppointmentActivity extends AppCompatActivity {

    private TextView tvDoctorName, tvSpecialty, tvHospitalAddress, tvDoctorPhone, tvFee;
    private EditText etFullName, etAddress, etContactNumber;
    private Button btnDate, btnTime, btnBook, btnBack;
    private int doctorId;
    private String doctorName, specialty, hospitalAddress, doctorPhone;
    private int fee;
    private String selectedDate = "";
    private String selectedTime = "";
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_appointment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        initializeViews();

        // Get doctor information from intent
        getDoctorInfoFromIntent();

        // Display doctor information
        displayDoctorInfo();

        // Set up date and time pickers
        setupDateTimePickers();

        // Set up button listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        // Doctor info views
        tvDoctorName = findViewById(R.id.textViewDoctorName);
        tvSpecialty = findViewById(R.id.textViewSpecialty);
        tvHospitalAddress = findViewById(R.id.textViewHospitalAddress);
        tvDoctorPhone = findViewById(R.id.textViewDoctorPhone);
        tvFee = findViewById(R.id.textViewAppFee);

        // Patient info inputs
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        User user = userDAO.getCurentUser();
        etFullName = findViewById(R.id.editTextAppFullname);
        etFullName.setText(user.getFullname());
        etAddress = findViewById(R.id.editTextAppAddress);
        etAddress.setText(user.getEmail());
        etContactNumber = findViewById(R.id.editTextAppContactNumber);
        etContactNumber.setText(user.getPhoneNumber());


        // Buttons
        btnDate = findViewById(R.id.editTextAppDate);
        btnTime = findViewById(R.id.editTextAppTime);
        btnBook = findViewById(R.id.buttonAppBook);
        btnBack = findViewById(R.id.buttonAppBack);

        calendar = Calendar.getInstance();
    }

    private void getDoctorInfoFromIntent() {
        Intent intent = getIntent();
        doctorId = intent.getIntExtra("doctorId", 0);
        doctorName = intent.getStringExtra("doctorName");
        specialty = intent.getStringExtra("specialty");
        hospitalAddress = intent.getStringExtra("hospitalAddress");
        doctorPhone = intent.getStringExtra("phoneNumber");
        fee = intent.getIntExtra("fee", 0);
    }

    private void displayDoctorInfo() {
        tvDoctorName.setText(doctorName != null ? doctorName : "N/A");
        tvSpecialty.setText(specialty != null ? specialty : "N/A");
        tvHospitalAddress.setText(hospitalAddress != null ? hospitalAddress : "N/A");
        tvDoctorPhone.setText(doctorPhone != null ? doctorPhone : "N/A");

        // Format fee in Vietnamese Dong
        if (fee > 1000) {
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedFee = formatter.format(fee / 1000);
            tvFee.setText(formattedFee + "K ₫");
        } else {
            tvFee.setText(fee + " ₫");
        }
    }

    private void setupDateTimePickers() {
        // Date Picker
        btnDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookAppointmentActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        selectedDate = dateFormat.format(calendar.getTime());
                        btnDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // Time Picker
        btnTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    BookAppointmentActivity.this,
                    (view, hourOfDay, minute) -> {
                        selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        btnTime.setText(selectedTime);
                    },
                    9, // Default hour (9 AM)
                    0, // Default minute
                    true // 24-hour format
            );
            timePickerDialog.show();
        });
    }

    private void setupButtonListeners() {
        // Book button
        btnBook.setOnClickListener(v -> {
            if (validateInputs()) {
                bookAppointment();
            }
        });

        // Back button
        btnBack.setOnClickListener(v -> {
            finish(); // Go back to previous activity (DoctorDetailsActivity)
        });
    }

    private boolean validateInputs() {
        String fullName = etFullName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String contactNumber = etContactNumber.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Please enter your full name");
            etFullName.requestFocus();
            return false;
        }

        if (fullName.length() < 3) {
            etFullName.setError("Name must be at least 3 characters");
            etFullName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Please enter your address");
            etAddress.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(contactNumber)) {
            etContactNumber.setError("Please enter your contact number");
            etContactNumber.requestFocus();
            return false;
        }

        if (contactNumber.length() < 9) {
            etContactNumber.setError("Please enter a valid phone number");
            etContactNumber.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(selectedDate)) {
            Toast.makeText(this, "Please select appointment date", Toast.LENGTH_SHORT).show();
            btnDate.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(selectedTime)) {
            Toast.makeText(this, "Please select appointment time", Toast.LENGTH_SHORT).show();
            btnTime.requestFocus();
            return false;
        }

        return true;
    }

    private void bookAppointment() {
        // Get patient information
        String patientName = etFullName.getText().toString().trim();
        String patientAddress = etAddress.getText().toString().trim();
        String patientPhone = etContactNumber.getText().toString().trim();

        // Get current date for booking date
        SimpleDateFormat bookingDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String bookingDate = bookingDateFormat.format(Calendar.getInstance().getTime());

        // Check if time slot is available
        BookingDAOImpl bookingDAO = BookingDAOImpl.getInstance();
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        User user = userDAO.getCurentUser();
        int userId = user.getId();

        if (!bookingDAO.isTimeSlotAvailable(doctorId, selectedDate, selectedTime)) {
            Toast.makeText(this, "This time slot is already booked. Please select another time.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Create patient object

        // Create booking object
        Booking booking = new Booking(
                doctorId,
                patientName,
                patientAddress,
                patientPhone,
                userId,
                fee,
                selectedDate,
                selectedTime,
                bookingDate,
                "pending", // Initial status
                0
        );

        // Save booking to database
        boolean success = bookingDAO.save(booking);

        if (success) {
            Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show();

            // Show confirmation dialog
            showBookingConfirmation();
        } else {
            Toast.makeText(this, "Failed to book appointment. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showBookingConfirmation() {
        // Create a confirmation dialog
        new android.app.AlertDialog.Builder(this)
                .setTitle("✅ Booking Confirmed")
                .setMessage("Your appointment has been booked successfully!\n\n" +
                        "Doctor: " + doctorName + "\n" +
                        "Specialty: " + specialty + "\n" +
                        "Date: " + selectedDate + "\n" +
                        "Time: " + selectedTime + "\n" +
                        "Fee: " + tvFee.getText().toString() + "\n\n" +
                        "📍 Location: " + hospitalAddress + "\n\n" +
                        "⏰ Please arrive 15 minutes before your appointment.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Clear form and go back
                    clearForm();
                    finish();
                })
                .setNegativeButton("View Bookings", (dialog, which) -> {
                    // TODO: Navigate to bookings list activity
                    clearForm();
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void clearForm() {
        etFullName.setText("");
        etAddress.setText("");
        etContactNumber.setText("");
        btnDate.setText("Select");
        btnTime.setText("Select");
        selectedDate = "";
        selectedTime = "";
    }
}