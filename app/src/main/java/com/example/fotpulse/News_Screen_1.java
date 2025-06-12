package com.example.fotpulse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class News_Screen_1 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<NewsItem> newsList = new ArrayList<>();
    private DatabaseReference newsRef;
    private EditText searchBar;
    private ImageView info_img, user_info;

    private LinearLayout navSports, navAcademic, navEvent;
    private String selectedCategory = "Sports";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen1);


        recyclerView = findViewById(R.id.newsRecyclerView);
        searchBar = findViewById(R.id.editText2);
        info_img = findViewById(R.id.info_img);
        user_info = findViewById(R.id.user_info);
        navSports = findViewById(R.id.nav_sports);
        navAcademic = findViewById(R.id.nav_academic);
        navEvent = findViewById(R.id.nav_event);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(adapter);


        newsRef = FirebaseDatabase.getInstance().getReference("news");

        // Developer info & User info buttons
        info_img.setOnClickListener(v -> startActivity(new Intent(this, DeveloperInfo.class)));
        user_info.setOnClickListener(v -> startActivity(new Intent(this, UserInfoScreen.class)));


        navSports.setOnClickListener(v -> {
            selectedCategory = "Sports";
            selectTab(navSports);
            loadNewsFromFirebase(selectedCategory);
        });

        navAcademic.setOnClickListener(v -> {
            selectedCategory = "Academic";
            selectTab(navAcademic);
            loadNewsFromFirebase(selectedCategory);
        });

        navEvent.setOnClickListener(v -> {
            selectedCategory = "Event";
            selectTab(navEvent);
            loadNewsFromFirebase(selectedCategory);
        });


        selectTab(navSports);
        loadNewsFromFirebase(selectedCategory);


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNews(s.toString().toLowerCase());
            }
        });
    }

    private void selectTab(LinearLayout selected) {
        navSports.setSelected(false);
        navAcademic.setSelected(false);
        navEvent.setSelected(false);
        selected.setSelected(true);
    }

    private void loadNewsFromFirebase(String categoryFilter) {
        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    NewsItem item = snap.getValue(NewsItem.class);
                    if (item != null && item.getCategory().equalsIgnoreCase(categoryFilter)) {
                        newsList.add(item);
                    }
                }
                adapter.updateList(newsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterNews(String query) {
        List<NewsItem> filteredList = new ArrayList<>();
        for (NewsItem item : newsList) {
            if (item.title.toLowerCase().contains(query)) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }
}
