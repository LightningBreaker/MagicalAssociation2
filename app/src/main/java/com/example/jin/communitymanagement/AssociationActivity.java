package com.example.jin.communitymanagement;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by summe on 2017/8/15.
 */

public class AssociationActivity {
    private String associationName;
    private String activityName;
    private String start_time;
    private Bitmap bitmap;
    private String end_time;
    private String introduction;
    private int inNeedMoney;


    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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

    public int getInNeedMoney() {
        return inNeedMoney;
    }

    public void setInNeedMoney(int inNeedMoney) {
        this.inNeedMoney = inNeedMoney;
    }


    //opo
    public AssociationActivity(String associationName, String activityName,
                               String start_time,String end_time,Bitmap bitmap,String introduction
    ,int inNeedMoney) {
        this.associationName = associationName;
        this.activityName = activityName;
       this.end_time=end_time;
        this.start_time=start_time;
        this.introduction=introduction;
        this.inNeedMoney=inNeedMoney;
        this.bitmap=bitmap;

    }

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }


}
