package com.example.jin.communitymanagement;

import android.graphics.Bitmap;

/**
 * Created by summe on 2017/9/25.
 */

public class BorrowItem {
    private String start_time;
    private String end_time;
    private String introduction;
    private Bitmap bitmap;
    private String connection;
    private String BorrowName;

    public BorrowItem(String start_time, String end_time, String introduction, Bitmap bitmap, String connection, String borrowName) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.introduction = introduction;
        this.bitmap = bitmap;
        this.connection = connection;
        BorrowName = borrowName;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getBorrowName() {
        return BorrowName;
    }

    public void setBorrowName(String borrowName) {
        BorrowName = borrowName;
    }
}
