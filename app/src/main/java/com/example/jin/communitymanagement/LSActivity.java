package com.example.jin.communitymanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LSActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
    private Button btnSignin;

    private MyDBHelper dbHelper;
    private TextInputEditText username_register;
    private TextInputEditText password_register;
    private TextInputEditText password2_register;
    private TextInputEditText username_login;
    private TextInputEditText password_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ls);

        username_login = (TextInputEditText) findViewById(R.id.editText2);
        password_login = (TextInputEditText) findViewById(R.id.editText);
        username_register = (TextInputEditText) findViewById(R.id.editText3);
        password_register = (TextInputEditText) findViewById(R.id.editText4);
        password2_register = (TextInputEditText) findViewById(R.id.editText5);

        llSignin = (LinearLayout) findViewById(R.id.llSignin);
        llSignin.setOnClickListener(this);

        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);

        btnSignup= (Button) findViewById(R.id.btnSignup);
        btnSignin= (Button) findViewById(R.id.btnSignin);

        llSignup = (LinearLayout) findViewById(R.id.llSignup);
        llSignin = (LinearLayout) findViewById(R.id.llSignin);

        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = false;
                showSignupForm();
            }
        });

        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = true;
                showSigninForm();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginClicked(view);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
                register(view);
                if(isSigninScreen)
                    btnSignup.startAnimation(clockwise);
            }
        });


        //添加了一个构造函数，初始化ActivityTable
        List<Bitmap> bitmapList=new ArrayList<>();
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.letsdance);
        for (int i = 0; i < 5; i++)
        {
            bitmapList.add(bitmap1);
        }
        dbHelper = new MyDBHelper(this, "UserStore.db", null,BaseActivity.DATABASE_VERSION,bitmapList);


        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);

        }

    }

    private void showSignupForm() {
        username_login.setFocusable(false);
        password_login.setFocusable(false);

        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();

        username_register.requestFocus();

        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        username_login.setFocusable(true);
        password_login.setFocusable(true);
        password_login.setFocusableInTouchMode(true);
        password_login.requestFocus();
        password_login.findFocus();
        username_login.setFocusableInTouchMode(true);
        username_login.requestFocus();
        username_login.findFocus();

        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();



        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
    }
 @Override
    public void onClick(View v) {
     switch (v.getId())
     {
         case  R.id.llSignin:
         case R.id.llSignup:
             InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
             methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
             break;
     }

    }



    //点击登录按钮
    public void loginClicked(View view) {
        String userName = username_login.getText().toString();
        String passWord = password_login.getText().toString();
        if (userName.equals("")) {
            Toast.makeText(this, "请输入您的账号", Toast.LENGTH_SHORT).show();
            username_login.requestFocus();
        }
        if (passWord.equals("")) {
            Toast.makeText(this, "请输入您的密码", Toast.LENGTH_SHORT).show();
            password_login.requestFocus();
        }

        if (login(userName, passWord)) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
            finish();
        }
    }

    //验证登录
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = null;
        if(!exits("user"))
            db.execSQL("create table user(id integer primary key ,"
                    + "name varchar(255),"
                    + "password varchar(255),"
                    + "nickname varchar(50),"
                    + "gender  varchar(2),"
                    + "photo varchar(255),"
                    + "position varchar(50),"
                    + "address varchar(255),"
                    + "hobbits varchar(255),"
                    + "goal varchar(255),"
                    + "demand varchar(255))");
        try {
            sql = "select * from user where name=? and password=?";
            Cursor cursor = db.rawQuery(sql, new String[]{username, password});
            if (cursor.moveToFirst()) {
                cursor.close();
                return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"账号密码不匹配", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean register(View view) {

        String newname = username_register.getText().toString();
        String password = password_register.getText().toString();
        String passwordCer = password2_register.getText().toString();

        if (newname.equals("")) {
            Toast.makeText(this, "请输入您的账号", Toast.LENGTH_SHORT).show();
            username_register.requestFocus();
            return false;
        }

        if (!checkName(newname)) {
            Toast.makeText(this, "账号格式应为字母开头可包含数字但不得超过16位", Toast.LENGTH_SHORT).show();
            username_register.requestFocus();
            return false;
        }
        if (CheckIsDataAlreadyInDBorNot(newname)) {
            Toast.makeText(this, "账号已存在", Toast.LENGTH_SHORT).show();
            username_register.requestFocus();
            return false;
        }
        if (password.equals("")) {
            Toast.makeText(this, "请输入您的密码", Toast.LENGTH_SHORT).show();
            password_register.requestFocus();
            return false;
        }
        if (!checkPassword(password)) {
            Toast.makeText(this, "密码格式不正确（8~16位，可包含字母数字和特殊字符）", Toast.LENGTH_SHORT).show();
            password_register.requestFocus();
            return false;
        }

        if (passwordCer.equals("")) {
            Toast.makeText(this, "请确认您的密码", Toast.LENGTH_SHORT).show();
            password2_register.requestFocus();
            return false;
        }
        if (!password.equals(passwordCer)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            password2_register.requestFocus();
            return false;
        }
        if (register(newname, password)) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            username_register.setText("");
            password_register.setText("");
            password2_register.setText("");
            showSigninForm();
        }
        else Toast.makeText(this, "意外错误", Toast.LENGTH_SHORT).show();

        return true;
    }

    //向数据库插入数据
    public boolean register(String username, String password) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if (!exits("user"))
                db.execSQL("create table user(id integer primary key ,"
                        + "name varchar(255),"
                        + "password varchar(255),"
                        + "nickname varchar(50),"
                        + "gender  varchar(2),"
                        + "photo varchar(255),"
                        + "position varchar(50),"
                        + "address varchar(255),"
                        + "hobbits varchar(255),"
                        + "goal varchar(255),"
                        + "demand varchar(255))");

            ContentValues values = new ContentValues();
            values.put("name", username);
            values.put("password", password);
            db.insert("user", null, values);
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return true;
    }

    //检验用户名是否已存在
    public boolean CheckIsDataAlreadyInDBorNot(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            String Query = "Select * from user where name =?";
            Cursor cursor = db.rawQuery(Query, new String[]{value});

            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();

        } catch (SQLiteException e) {
            e.printStackTrace();

        }
        return false;
    }

    public boolean exits(String table) {
        boolean exits = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from sqlite_master where name=" + "'" + table + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() != 0) {
            exits = true;
        }
        cursor.close();
        return exits;
    }

    public boolean checkName(String str) {

        // 编译正则表达式
        Pattern pattern = Pattern.compile("[a-zA-Z]{1}[A-Za-z0-9_]{1,15}");
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    public boolean checkPassword(String str) {

        // 编译正则表达式
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_!@#$%^&*/()-+.]{8,16}");
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }


}
