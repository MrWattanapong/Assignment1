package com.egco428.a13281;

public class Comment {
    private long id;
    private String comment;
    private String img;
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public String getImg() {
        return img;
    }

    public String getDate() {
        return date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return comment;
    }
}