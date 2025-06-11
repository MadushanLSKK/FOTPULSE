package com.example.fotpulse;

public class NewsItem {
    public String title;
    public String description;
    public String imageUrl;
    public String date;
    public String time;
    public String category;


    public NewsItem() {}


    public NewsItem(String title, String description, String imageUrl, String date, String time, String category) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.time = time;
        this.category = category;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }
}
