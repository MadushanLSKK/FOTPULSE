package com.example.fotpulse;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewsScreen2 extends AppCompatActivity {

    private TextView titleView, dateView, timeView, bodyView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen2);

        titleView = findViewById(R.id.news_title);
        dateView = findViewById(R.id.news_date);
        timeView = findViewById(R.id.news_time);
        bodyView = findViewById(R.id.news_body);
        imageView = findViewById(R.id.news_image); // ✅ fixed this

        // Get extras
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String body = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Set data
        titleView.setText(title);
        dateView.setText(date);
        timeView.setText(time);
        bodyView.setText(body.replace("\\n", "\n"));

        // Load image
        Picasso.get().load(imageUrl).into(imageView);
    }
}
