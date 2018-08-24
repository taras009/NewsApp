package com.example.comp_admin.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }
        News currentNews = getItem(position);
        // setting title
        TextView titleTextViw = (TextView) listItemView.findViewById(R.id.title);
        titleTextViw.setText(currentNews.getTitle());

        //setting section
        TextView sectionTextViw = (TextView) listItemView.findViewById(R.id.section);
        sectionTextViw.setText(currentNews.getSection());

        //setting authors
        TextView authorTextViw = (TextView) listItemView.findViewById(R.id.author);
        String writtenByAuthor = getContext().getString(R.string.written_by)+ " " + currentNews.getAuthorName();
        authorTextViw.setText(writtenByAuthor);

        // editing date
        String originDate = currentNews.getDate();
        String[] arrayOfDate = originDate.split("T");

        TextView dateTextViw = (TextView) listItemView.findViewById(R.id.date);
        dateTextViw.setText(arrayOfDate[0]);

        return listItemView;
    }
}
