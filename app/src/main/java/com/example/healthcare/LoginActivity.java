package com.example.healthcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcare.DAO.SQLite.UserDAOImpl;
import com.example.healthcare.Model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText edUsername, edPassword;
    private TextView tvRegister;
    private Button btnLogin;

    private final UserDAOImpl userDAO = UserDAOImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edUsername = findViewById(R.id.editTextLoginUsername);
        edPassword = findViewById(R.id.editTextLoginPassword);
        tvRegister = findViewById(R.id.textViewRegister);
        btnLogin = findViewById(R.id.button);

        btnLogin.setOnClickListener(v -> handleLogin());
        tvRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Auto-login if user already logged in previously
        SharedPreferences prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);

        if (username != null) {
            // Optional: verify the username still exists in DB
            User user = userDAO.getUserByUsername(username);
            if (user != null) {
                Toast.makeText(this, "Welcome back, " + username + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish(); // Prevent returning to login
            } else {
                // If user no longer exists, clear stored data
                prefs.edit().clear().apply();
            }
        }
    }

    private void handleLogin() {
        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userDAO.login(username, password);
        if (user != null) {
            // Save session
            SharedPreferences prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", user.getUsername());
            editor.putInt("userId", user.getId());
            editor.apply();

            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Login failed: invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
