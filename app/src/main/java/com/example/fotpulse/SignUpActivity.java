package com.example.fotpulse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameField, emailField, passwordField, confirmPasswordField;
    Button signupBtn;
    TextView login;

    FirebaseDatabase database;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Link XML elements
        usernameField = findViewById(R.id.username);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.conformPassword);

        signupBtn = findViewById(R.id.signup_btn);
        login = findViewById(R.id.loginText);

        // Firebase setup
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Handle login text
        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, signinActivity.class);
            startActivity(intent);
        });

        // Handle signup
        signupBtn.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Push to Firebase
            String userId = username;
            User user = new User(username, email, password);

            usersRef.child(userId).setValue(user)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, signinActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Model class for User
    public static class User {
        public String username, email, password;

        public User() {} // required for Firebase

        public User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }
}
