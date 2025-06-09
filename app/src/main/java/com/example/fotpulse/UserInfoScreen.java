package com.example.fotpulse;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserInfoScreen extends AppCompatActivity {
    Button signOut;

    Dialog dialog;
    Button btnDialogCancel , btnDialogYes ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info_screen);
        signOut = findViewById(R.id.sign_out_button);

        dialog= new Dialog(UserInfoScreen.this);
        dialog.setContentView(R.layout.activity_user_info_screen2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        btnDialogYes=dialog.findViewById(R.id.btnDialogYes);
        btnDialogCancel=dialog.findViewById(R.id.btnDialogCancel);

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

       signOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.show();
           }
       });

    }
}