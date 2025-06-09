package com.example.fotpulse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import androidx.cardview.widget.CardView;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class News_Screen_1 extends AppCompatActivity {

    // Bottom nav elements
    private LinearLayout navSports, navAcademic, navEvent;
    private ImageView iconSports, iconAcademic, iconEvent;
    private TextView labelSports, labelAcademic, labelEvent;

    // Search + News cards
    private EditText searchBar;
    private CardView cardCricket, cardVolleyball, cardFootball;
    private TextView titleCricket, titleVolleyball, titleFootball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_screen1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupBottomNav();
        setupSearchFunction();
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
        // Initialize views
        searchBar = findViewById(R.id.editText2);

        cardCricket = findViewById(R.id.cardView_cricket);
        cardVolleyball = findViewById(R.id.cardView_volleyball);
        cardFootball = findViewById(R.id.cardView_football);

        titleCricket = findViewById(R.id.title_cricket);
        titleVolleyball = findViewById(R.id.title_volleyball);
        titleFootball = findViewById(R.id.title_football);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase();

                filterCard(cardCricket, titleCricket, query);
                filterCard(cardVolleyball, titleVolleyball, query);
                filterCard(cardFootball, titleFootball, query);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterCard(CardView card, TextView title, String query) {
        String text = title.getText().toString().toLowerCase();
        if (text.contains(query)) {
            card.setVisibility(CardView.VISIBLE);
        } else {
            card.setVisibility(CardView.GONE);
        }
    }
}
