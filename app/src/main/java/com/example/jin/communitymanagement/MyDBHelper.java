package com.example.jin.communitymanagement;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyDBHelper extends SQLiteOpenHelper {

    public static final String CREATE_USERDATA = "create table userData(" +
            "id integer primary key autoincrement,"
            + "name text,"
            + "password text)";
    public static final  String CREATE_AC_TABLE="create table ActivityTable("+
            "time_start text,"
            +"time_end text,"
            +"association text,"
            +"introduction text,"
            +"activity_name text,"
            +"image BLOB,"
            +"inNeedMoney integer)";
    private List<Bitmap> bitmapList=new ArrayList<>();

    private Context mContext;

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version,List<Bitmap> imgList) {
        super(context, name, cursorFactory, version);
        mContext = context;
        bitmapList.addAll(imgList);
    }

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
        mContext = context;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERDATA);
        db.execSQL(CREATE_AC_TABLE);
        for (int i=0;i<bitmapList.size();i++)
        add_activity_to_database(db,"开始时间：2017年9月1日23时00分00秒","结束时间：2017年9月1日23时00分00秒","魅团",
                "这是测试的活动",bitmapList.get(i),"测试活动",0);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists userData");
        db.execSQL("drop table if exists ActivityTable");
        onCreate(db);

       // Toast.makeText(mContext, "升级成功", Toast.LENGTH_SHORT).show();
    }

    private void add_activity_to_database( SQLiteDatabase db ,String m_start, String m_end, String asso_name, String introduction, Bitmap m_photo, String activity_name, int inNeedMoney) {
       byte[] image=get_bitimg(m_photo);

            db.execSQL("insert into ActivityTable(time_start,time_end,association,introduction,activity_name,image,inNeedMoney) values (?,?,?,?,?,?,?)"
                    , new Object[]{m_start, m_end, asso_name, introduction, activity_name,image, inNeedMoney});

    }
    public byte[] get_bitimg(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
