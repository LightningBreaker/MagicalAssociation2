package com.example.jin.communitymanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Activity_InfoActivity extends AppCompatActivity {

    public static final String AC_NAME="ac_name";
    public static final String ASS_NAME="ass_name";
    public static final String START_TIME="start_time";
    public static final String END_TIME="end_time";
    public static final String INTRODUCTION="introduction";
    public static final String IMAGE="image";
    public static  final  String CONNECTION="connection";
    private TextView textViewS_T;
    private TextView textViewE_T;
    private  TextView textViewAc_name;
    private  TextView textViewAss_name;
    private TextView textView_introduction;
    private ImageView imageView;
    private  TextView textView_connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__info);
        initView();
        addInfo();
    }

    private void addInfo() {
        Intent intent_ac=getIntent();
        String ac_name=intent_ac.getStringExtra(AC_NAME);
        String asso_name=intent_ac.getStringExtra(ASS_NAME);
        String introduction=intent_ac.getStringExtra(INTRODUCTION);
        String start_time=intent_ac.getStringExtra(START_TIME);
        String end_time=intent_ac.getStringExtra(END_TIME);
        String connection=intent_ac.getStringExtra(CONNECTION);
        byte[] image= intent_ac.getByteArrayExtra(IMAGE);
        Bitmap bitmap= getBmp(image);
        textViewAss_name.setText("社团名字："+asso_name);
        textViewAc_name.setText("活动名字:"+ac_name);
        textViewE_T.setText("结束时间:"+end_time);
        textViewS_T.setText("开始时间:"+start_time);
        textView_introduction.setText("活动简介："+introduction);
        imageView.setImageBitmap(bitmap);
        textView_connection.setText("赞助联系方式:"+connection);

    }
    public Bitmap getBmp(byte[] in)
    {
        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        return bmpout;
    }

    private void initView() {
            textView_introduction=(TextView)findViewById(R.id.text_ac_info_introduction);
            textViewAc_name=(TextView)findViewById(R.id.text_ac_info_ac_name);
            textViewAss_name=(TextView)findViewById(R.id.text_ac_info_asso_name);
            textViewS_T=(TextView)findViewById(R.id.text_ac_info_start_time);
            textViewE_T=(TextView)findViewById(R.id.text_ac_info_end_time);
            imageView=(ImageView)findViewById(R.id.imageView_ac_info_image);
            textView_connection=(TextView)findViewById(R.id.text_ac_info_connection);
    }
}
