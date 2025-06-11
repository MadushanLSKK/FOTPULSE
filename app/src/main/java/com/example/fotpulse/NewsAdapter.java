package com.example.fotpulse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<NewsItem> newsList;

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem news = newsList.get(position);

        holder.title.setText(news.title + "\n" + news.date);
        if (news.imageUrl != null && !news.imageUrl.isEmpty()) {
            Picasso.get().load(news.imageUrl).into(holder.image);
        } else {

        }

        holder.readMoreBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsScreen2.class);
            intent.putExtra("title", news.title);
            intent.putExtra("description", news.description);
            intent.putExtra("imageUrl", news.imageUrl);
            intent.putExtra("date", news.date);
            intent.putExtra("time", news.time);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateList(List<NewsItem> newList) {
        this.newsList = newList;
        notifyDataSetChanged();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        Button readMoreBtn;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            image = itemView.findViewById(R.id.newsImage);
            readMoreBtn = itemView.findViewById(R.id.readMoreBtn);
        }
    }
}
