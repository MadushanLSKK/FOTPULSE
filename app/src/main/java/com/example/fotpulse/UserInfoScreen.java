package com.example.fotpulse;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfoScreen extends AppCompatActivity {

    Button signOut, btnDialogCancel, btnDialogYes;
    Dialog dialog;

    TextView usernameLabel, emailLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info_screen);

        signOut = findViewById(R.id.sign_out_button);
        usernameLabel = findViewById(R.id.username_label);
        emailLabel = findViewById(R.id.email_label);

        // 🟡 Get username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUsername = prefs.getString("loggedInUsername", null);

        if (savedUsername == null) {
            // Not logged in, redirect to sign-in screen
            startActivity(new Intent(this, signinActivity.class));
            finish();
            return;
        }

        // 🔵 Fetch user info from Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(savedUsername);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uname = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    usernameLabel.setText("Username : " + uname);
                    emailLabel.setText("Email : " + email);
                } else {
                    usernameLabel.setText("Username : not found");
                    emailLabel.setText("Email : not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                usernameLabel.setText("Failed to load data");
                emailLabel.setText(error.getMessage());
            }
        });

        // 🔴 Logout dialog setup
        dialog = new Dialog(UserInfoScreen.this);
        dialog.setContentView(R.layout.activity_user_info_screen2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        btnDialogYes = dialog.findViewById(R.id.btnDialogYes);
        btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);

        btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        btnDialogYes.setOnClickListener(v -> {
            prefs.edit().clear().apply(); // Clear session
            startActivity(new Intent(UserInfoScreen.this, signinActivity.class));
            finish();
        });

        signOut.setOnClickListener(v -> dialog.show());
    }
}
