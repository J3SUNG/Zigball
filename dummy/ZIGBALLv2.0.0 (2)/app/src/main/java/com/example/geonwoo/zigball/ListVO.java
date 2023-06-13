package com.example.geonwoo.zigball;

import android.graphics.drawable.Drawable;

public class ListVO {
    private Drawable img;
    private String title;
    private String context;
    private int room;

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getRoom() { return room; }

    public void setRoom(int room) { this.room = room; }

}
