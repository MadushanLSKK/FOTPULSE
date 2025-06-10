package com.example.fotpulse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import androidx.cardview.widget.CardView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import com.google.firebase.database.*;

public class News_Screen_1 extends AppCompatActivity {

    private LinearLayout navSports, navAcademic, navEvent;
    private ImageView iconSports, iconAcademic, iconEvent;
    private TextView labelSports, labelAcademic, labelEvent;

    private EditText searchBar;
    private CardView cardCricket, cardVolleyball, cardFootball;
    private TextView titleCricket, titleVolleyball, titleFootball;
    private Button readMore1, readMore2, readMore3;
    private ImageView imageCricket, imageVolleyball, imageFootball;
    private DatabaseReference newsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen1);

        initViews();
        setupBottomNav();
        setupSearchFunction();
        loadNewsFromFirebase();
    }

    private void initViews() {
        imageCricket = findViewById(R.id.image_cricket);
        imageVolleyball = findViewById(R.id.image_volleyball);
        imageFootball = findViewById(R.id.image_football);

        searchBar = findViewById(R.id.editText2);

        cardCricket = findViewById(R.id.cardView_cricket);
        cardVolleyball = findViewById(R.id.cardView_volleyball);
        cardFootball = findViewById(R.id.cardView_football);

        titleCricket = findViewById(R.id.title_cricket);
        titleVolleyball = findViewById(R.id.title_volleyball);
        titleFootball = findViewById(R.id.title_football);

        readMore1 = findViewById(R.id.readmore1);
        readMore2 = findViewById(R.id.readmore2);
        readMore3 = findViewById(R.id.readmore3);

        newsRef = FirebaseDatabase.getInstance().getReference("news");
    }

    private void setupBottomNav() {
        navSports = findViewById(R.id.nav_sports);
        navAcademic = findViewById(R.id.nav_academic);
        navEvent = findViewById(R.id.nav_event);

        iconSports = findViewById(R.id.icon_sports);
        iconAcademic = findViewById(R.id.icon_academic);
        iconEvent = findViewById(R.id.icon_event);

        labelSports = findViewById(R.id.label_sports);
        labelAcademic = findViewById(R.id.label_academic);
        labelEvent = findViewById(R.id.label_event);

        navSports.setOnClickListener(v -> selectTab(navSports, iconSports, labelSports));
        navAcademic.setOnClickListener(v -> selectTab(navAcademic, iconAcademic, labelAcademic));
        navEvent.setOnClickListener(v -> selectTab(navEvent, iconEvent, labelEvent));

        selectTab(navSports, iconSports, labelSports);
    }

    private void selectTab(LinearLayout selectedLayout, ImageView selectedIcon, TextView selectedLabel) {
        resetTab(navSports, iconSports, labelSports);
        resetTab(navAcademic, iconAcademic, labelAcademic);
        resetTab(navEvent, iconEvent, labelEvent);

        selectedLayout.setSelected(true);
        selectedIcon.setSelected(true);
        selectedLabel.setSelected(true);
    }

    private void resetTab(LinearLayout layout, ImageView icon, TextView label) {
        layout.setSelected(false);
        icon.setSelected(false);
        label.setSelected(false);
    }

    private void setupSearchFunction() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase();
                filterCard(cardCricket, titleCricket, query);
                filterCard(cardVolleyball, titleVolleyball, query);
                filterCard(cardFootball, titleFootball, query);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filterCard(CardView card, TextView title, String query) {
        String text = title.getText().toString().toLowerCase();
        card.setVisibility(text.contains(query) ? CardView.VISIBLE : CardView.GONE);
    }

    private void loadNewsFromFirebase() {
        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot) {
                int index = 0;
                for (DataSnapshot newsSnap : snapshot.getChildren()) {
                    String title = newsSnap.child("title").getValue(String.class);
                    String imageUrl = newsSnap.child("imageUrl").getValue(String.class);
                    String description = newsSnap.child("description").getValue(String.class);
                    String date = newsSnap.child("date").getValue(String.class);
                    String time = newsSnap.child("time").getValue(String.class);

                    if (index == 0) {
                        titleCricket.setText(title);
                        Picasso.get().load(imageUrl).into(imageCricket);
                        readMore1.setOnClickListener(v -> openNewsScreen2(title, imageUrl, description, date, time));
                    } else if (index == 1) {
                        titleVolleyball.setText(title);
                        Picasso.get().load(imageUrl).into(imageVolleyball);
                        readMore2.setOnClickListener(v -> openNewsScreen2(title, imageUrl, description, date, time));
                    } else if (index == 2) {
                        titleFootball.setText(title);
                        Picasso.get().load(imageUrl).into(imageFootball);
                        readMore3.setOnClickListener(v -> openNewsScreen2(title, imageUrl, description, date, time));
                    }

                    index++;
                }
            }
            @Override public void onCancelled(DatabaseError error) {}
        });
    }

    private void openNewsScreen2(String title, String imageUrl, String description, String date, String time) {
        Intent intent = new Intent(News_Screen_1.this, NewsScreen2.class);
        intent.putExtra("title", title);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("description", description);
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        startActivity(intent);
    }
}
