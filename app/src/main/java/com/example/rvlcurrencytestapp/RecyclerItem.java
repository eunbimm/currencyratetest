package com.example.rvlcurrencytestapp;

import android.graphics.drawable.Drawable;

import lombok.Data;


@Data
public class RecyclerItem {
    private Drawable image;
    private String title;
    private String rate;

    public RecyclerItem(String title, String rate) {
        this.title = title;
        this.rate = rate;
    }
}

