package com.app.asiaflixapp.model;

public class Episode {
    String title, link, type, year;

    public Episode(String title, String link, String type, String year) {
        this.title = title;
        this.link = link;
        this.type = type;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
