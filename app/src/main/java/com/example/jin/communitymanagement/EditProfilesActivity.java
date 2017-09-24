package com.example.jin.communitymanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.net.sip.SipAudioCall;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

public class EditProfilesActivity extends AppCompatActivity {

    private EditText edit_nickname,edit_gender,edit_position,
            edit_address,edit_hobbits,edit_goal,edit_demand;
    private Button btn_edit_save,btn_edit_back;
    private MyDBHelper dbHelper;
    private NiftyDialogBuilder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profiles);
        dbHelper = new MyDBHelper(this, "UserStore.db", null, BaseActivity.DATABASE_VERSION);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        edit_nickname = (EditText) findViewById(R.id.edit_nickname);
        edit_nickname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        edit_gender = (EditText) findViewById(R.id.edit_gender);
        edit_gender.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});

        edit_position = (EditText) findViewById(R.id.edit_position);
        edit_position.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_address.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});

        edit_hobbits = (EditText) findViewById(R.id.edit_hobbits);
        edit_hobbits.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});

        edit_goal = (EditText) findViewById(R.id.edit_goal);
        edit_goal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});

        edit_demand = (EditText) findViewById(R.id.edit_demand);
        edit_demand.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    switch (view.getId()){
                        case R.id.edit_nickname:
                            edit_nickname.setSelection(edit_nickname.getText().length());
                            break;
                        case R.id.edit_gender:
                            edit_gender.setSelection(edit_gender.getText().length());
                            break;
                        case R.id.edit_position:
                            edit_position.setSelection(edit_position.getText().length());
                            break;
                        case R.id.edit_address:
                            edit_address.setSelection(edit_address.getText().length());
                            break;
                        case R.id.edit_hobbits:
                            edit_hobbits.setSelection(edit_hobbits.getText().length());
                            break;
                        case R.id.edit_goal:
                            edit_goal.setSelection(edit_goal.getText().length());
                            break;
                        case R.id.edit_demand:
                            edit_demand.setSelection(edit_demand.getText().length());
                            break;
                        default:
                            break;
                    }
                }
            }
        };

        edit_nickname.setOnFocusChangeListener(focusChangeListener);
        edit_gender.setOnFocusChangeListener(focusChangeListener);
        edit_position.setOnFocusChangeListener(focusChangeListener);
        edit_address.setOnFocusChangeListener(focusChangeListener);
        edit_hobbits.setOnFocusChangeListener(focusChangeListener);
        edit_goal.setOnFocusChangeListener(focusChangeListener);
        edit_demand.setOnFocusChangeListener(focusChangeListener);

        btn_edit_save = (Button) findViewById(R.id.btn_edit_save);
        btn_edit_back = (Button) findViewById(R.id.btn_edit_back);


        try {
            String sql = "select * from user where name=?";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, new String[]{MainActivity.userName});
            if (cursor.moveToFirst()) {
                edit_nickname.setText(cursor.getString(3));
                edit_gender.setText(cursor.getString(4));
                edit_position.setText(cursor.getString(6));
                edit_address.setText(cursor.getString(7));
                edit_hobbits.setText(cursor.getString(8));
                edit_goal.setText(cursor.getString(9));
                edit_demand.setText(cursor.getString(10));
            }else Log.e("Didn't found :","没有找到本用户名");
            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.btn_edit_save:
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("nickname", edit_nickname.getText().toString());
                        values.put("gender", edit_gender.getText().toString());
                        values.put("position", edit_position.getText().toString());
                        values.put("address", edit_address.getText().toString());
                        values.put("hobbits", edit_hobbits.getText().toString());
                        values.put("goal", edit_goal.getText().toString());
                        values.put("demand", edit_demand.getText().toString());
                        db.update("user", values, "name=?", new String[]{MainActivity.userName});
                        db.close();
                        Toast.makeText(EditProfilesActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfilesActivity.this,MainActivity.class);
                        intent.putExtra("itemId",4);
                        startActivity(intent);
                        break;
                    case R.id.btn_edit_back:
                        dialogBuilder //构造选择头像的对话框
                                .withTitle("确认退出")
                                .withTitleColor("#FFcc00")
                                .withDividerColor("#11000000")
                                .withMessage("您填写的信息尚未保存，是否确认退出")
                                .withMessageColor("#ffcc00")
                                .withDialogColor("#333333")
                                .withEffect(Effectstype.RotateLeft)
                                .withDuration(700)
                                .withButton2Text("确定")
                                .withButton1Text("取消")
                                .isCancelableOnTouchOutside(true)
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.cancel();
                                    }
                                })
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        dialogBuilder.cancel();
                                    }
                                }).show();
                        break;
                    default:
                        break;
                }
            }
        };
        btn_edit_save.setOnClickListener(listener);
        btn_edit_back.setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        btn_edit_back.performClick();
    }
}
