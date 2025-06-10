package com.example.fotpulse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signinActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginBtn;
    TextView signUp;

    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);

        // Firebase Database reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Link UI elements
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        signUp = findViewById(R.id.signUpText);

        // Sign Up redirection
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(signinActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Login button logic
        loginBtn.setOnClickListener(v -> {
            String enteredUsername = usernameField.getText().toString().trim();
            String enteredPassword = passwordField.getText().toString().trim();

            if (TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty(enteredPassword)) {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check credentials in Firebase
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean found = false;

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String dbUsername = userSnapshot.child("username").getValue(String.class);
                        String dbPassword = userSnapshot.child("password").getValue(String.class);

                        if (enteredUsername.equals(dbUsername) && enteredPassword.equals(dbPassword)) {
                            found = true;

                            // ✅ Save session in SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("loggedInUsername", enteredUsername);
                            editor.apply();

                            Toast.makeText(signinActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(signinActivity.this, News_Screen_1.class));
                            finish();
                            break;
                        }
                    }

                    if (!found) {
                        Toast.makeText(signinActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(signinActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
