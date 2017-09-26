package com.example.jin.communitymanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Borrow_Info_Activity extends AppCompatActivity {

    public static final String BR_NAME="br_name";
    public static final String START_TIME="start_time";
    public static final String END_TIME="end_time";
    public static final String INTRODUCTION="introduction";
    public static final String IMAGE="image";
    public static final String CONNECTION="connection";

    private TextView textViewS_T;
    private TextView textViewE_T;
    private  TextView textViewBorrow_name;
    private  TextView textView_connection;
    private TextView textView_introduction;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_info);
        initView();
        addInfo();
    }

    private void addInfo() {
        Intent intent_ac=getIntent();
        String br_name=intent_ac.getStringExtra(BR_NAME);
        String connection_name=intent_ac.getStringExtra(CONNECTION);
        String introduction=intent_ac.getStringExtra(INTRODUCTION);
        String start_time=intent_ac.getStringExtra(START_TIME);
        String end_time=intent_ac.getStringExtra(END_TIME);
        byte[] image= intent_ac.getByteArrayExtra(IMAGE);
        Bitmap bitmap= getBmp(image);


        textViewE_T.setText(end_time);
        textViewS_T.setText(start_time);
        textView_introduction.setText(introduction);
        imageView.setImageBitmap(bitmap);
        textViewBorrow_name.setText("租赁物品："+br_name);
        textView_connection.setText("联系方式："+connection_name);
       // textView_connection.setText("联系方式：");
    }

    private void initView() {

        textView_introduction=(TextView)findViewById(R.id.text_borrow_info_introduction);
        textViewBorrow_name=(TextView)findViewById(R.id.text_my_borrow_name);
        textView_connection=(TextView)findViewById(R.id.text_my_borrow_connection);
        textViewS_T=(TextView)findViewById(R.id.text_borrow_info_start_time);
        textViewE_T=(TextView)findViewById(R.id.text_borrow_info_end_time);
        imageView=(ImageView)findViewById(R.id.imageView_borrow_info_image);
    }
    public Bitmap getBmp(byte[] in)
    {
        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        return bmpout;
    }
}
