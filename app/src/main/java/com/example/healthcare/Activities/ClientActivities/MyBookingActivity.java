package com.example.healthcare.Activities.ClientActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcare.Adapters.BookingAdapter;
import com.example.healthcare.DAO.SQLite.BookingDAOImpl;
import com.example.healthcare.DAO.SQLite.UserDAOImpl;
import com.example.healthcare.Model.Booking;
import com.example.healthcare.R;

import java.util.List;

public class MyBookingActivity extends AppCompatActivity {

    private ListView listViewBookings;
    private Button buttonBack;
    private BookingDAOImpl bookingDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_booking);

        listViewBookings = findViewById(R.id.listviewBookings);
        buttonBack = findViewById(R.id.buttonBack);

        bookingDAO = BookingDAOImpl.getInstance();

        loadMyBookings();

        buttonBack.setOnClickListener(v -> {
            finish(); // go back to previous screen
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listviewBookings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadMyBookings() {
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        int userId =  userDAO.getCurrentUserId(this);

        List<Booking> bookingList = bookingDAO.getBookingsByUserId(userId);

        if (bookingList == null || bookingList.isEmpty()) {
            Toast.makeText(this, "You have no bookings", Toast.LENGTH_SHORT).show();
        } else {
            BookingAdapter adapter = new BookingAdapter(this, bookingList);
            listViewBookings.setAdapter(adapter);
        }
    }
}
