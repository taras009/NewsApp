package com.example.comp_admin.newsapp;

public class News {
    private String title;
    private String section;
    private String url;
    private String date;
    private String authorName;

    public News(String title, String section, String url, String date, String authorName) {
        this.title = title;
        this.section = section;
        this.url = url;
        this.date = date;
        this.authorName = authorName;
    }


    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getAuthorName() {
        return authorName;
    }
}
