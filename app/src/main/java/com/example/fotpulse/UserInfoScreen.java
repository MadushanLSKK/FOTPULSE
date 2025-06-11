package com.example.fotpulse;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
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

public class UserInfoScreen extends AppCompatActivity {

    Button signOut, btnDialogCancel, btnDialogYes, editInfoBtn;
    Dialog dialog;

    TextView usernameLabel, emailLabel;
    SharedPreferences prefs;
    DatabaseReference usersRef;
    String savedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info_screen);

        signOut = findViewById(R.id.sign_out_button);
        editInfoBtn = findViewById(R.id.edit_info_button);
        usernameLabel = findViewById(R.id.username_label);
        emailLabel = findViewById(R.id.email_label);

        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        savedUsername = prefs.getString("loggedInUsername", null);

        if (savedUsername == null) {
            startActivity(new Intent(this, signinActivity.class));
            finish();
            return;
        }

        usersRef = FirebaseDatabase.getInstance().getReference("users");


        usersRef.child(savedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uname = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    usernameLabel.setText("Username : " + uname);
                    emailLabel.setText("Email : " + email);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                usernameLabel.setText("Failed to load");
                emailLabel.setText(error.getMessage());
            }
        });


        dialog = new Dialog(UserInfoScreen.this);
        dialog.setContentView(R.layout.activity_user_info_screen2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        btnDialogYes = dialog.findViewById(R.id.btnDialogYes);
        btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);

        btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        btnDialogYes.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(UserInfoScreen.this, signinActivity.class));
            finish();
        });

        signOut.setOnClickListener(v -> dialog.show());


        editInfoBtn.setOnClickListener(v -> {
            Dialog editDialog = new Dialog(UserInfoScreen.this);
            editDialog.setContentView(R.layout.dialog_edit_info);
            editDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editDialog.setCancelable(true);

            EditText editUsername = editDialog.findViewById(R.id.edit_username);
            EditText editEmail = editDialog.findViewById(R.id.edit_email);
            Button btnOk = editDialog.findViewById(R.id.btn_ok);
            Button btnCancel = editDialog.findViewById(R.id.btn_cancel);

            editUsername.setText(usernameLabel.getText().toString().replace("Username : ", ""));
            editEmail.setText(emailLabel.getText().toString().replace("Email : ", ""));

            btnOk.setOnClickListener(view -> {
                String newUsername = editUsername.getText().toString().trim();
                String newEmail = editEmail.getText().toString().trim();


                if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newEmail)) {
                    Toast.makeText(UserInfoScreen.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    Toast.makeText(UserInfoScreen.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newUsername.equals(savedUsername)) {

                    usersRef.child(savedUsername).child("email").setValue(newEmail);
                    emailLabel.setText("Email : " + newEmail);
                    Toast.makeText(UserInfoScreen.this, "Email updated", Toast.LENGTH_SHORT).show();
                    editDialog.dismiss();
                    return;
                }


                usersRef.child(newUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot existsSnapshot) {
                        if (existsSnapshot.exists()) {
                            Toast.makeText(UserInfoScreen.this, "Username already taken", Toast.LENGTH_SHORT).show();
                        } else {

                            usersRef.child(savedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String password = snapshot.child("password").getValue(String.class);

                                        DatabaseReference newRef = usersRef.child(newUsername);
                                        newRef.child("username").setValue(newUsername);
                                        newRef.child("email").setValue(newEmail);
                                        newRef.child("password").setValue(password);

                                        usersRef.child(savedUsername).removeValue();

                                        usernameLabel.setText("Username : " + newUsername);
                                        emailLabel.setText("Email : " + newEmail);
                                        prefs.edit().putString("loggedInUsername", newUsername).apply();

                                        Toast.makeText(UserInfoScreen.this, "Info updated", Toast.LENGTH_SHORT).show();
                                        editDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(UserInfoScreen.this, "Update failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(UserInfoScreen.this, "Validation failed", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            btnCancel.setOnClickListener(view -> editDialog.dismiss());
            editDialog.show();
        });
    }
}
