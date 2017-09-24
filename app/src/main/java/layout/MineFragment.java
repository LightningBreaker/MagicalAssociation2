package layout;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jin.communitymanagement.BaseActivity;
import com.example.jin.communitymanagement.CropOption;
import com.example.jin.communitymanagement.CropOptionAdapter;
import com.example.jin.communitymanagement.EditProfilesActivity;
import com.example.jin.communitymanagement.MainActivity;
import com.example.jin.communitymanagement.MyDBHelper;
import com.example.jin.communitymanagement.R;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FlipV;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MineFragment extends Fragment {

    private NiftyDialogBuilder dialogBuilder;
    Uri imgUri;
    ImageView profile_icon, profile_gender;
    TextView mine_tv_setProfiles, mine_tv_nickname, mine_tv_position, mine_tv_address,
            mine_tv_hobbits, mine_tv_goal, mine_tv_demand;
    private MyDBHelper dbHelper;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        profile_icon = (ImageView) view.findViewById(R.id.profile_icon);
        profile_gender = (ImageView) view.findViewById(R.id.profile_gender);

        mine_tv_setProfiles = (TextView) view.findViewById(R.id.mine_tv_setProfiles);
        mine_tv_nickname = (TextView) view.findViewById(R.id.mine_tv_nickname);
        mine_tv_position = (TextView) view.findViewById(R.id.mine_tv_position);
        mine_tv_address = (TextView) view.findViewById(R.id.mine_tv_address);
        mine_tv_hobbits = (TextView) view.findViewById(R.id.mine_tv_hobbits);
        mine_tv_goal = (TextView) view.findViewById(R.id.mine_tv_goal);
        mine_tv_demand = (TextView) view.findViewById(R.id.mine_tv_demand);
        dbHelper = new MyDBHelper(getActivity(), "UserStore.db", null, BaseActivity.DATABASE_VERSION);

        try {
            String sql = "select * from user where name=?";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, new String[]{MainActivity.userName});
            if (cursor.moveToFirst()) {
                mine_tv_nickname.setText(cursor.getString(3));
                if (cursor.getString(4) != null) {
                    if (cursor.getString(4).contains("男"))
                        profile_gender.setImageBitmap(toRoundCorner(BitmapFactory.decodeResource
                                (getActivity().getResources(), R.drawable.profile_gender_male3), 100));
                    else if (cursor.getString(4).contains("女"))
                        profile_gender.setImageBitmap(toRoundCorner(BitmapFactory.decodeResource
                                (getActivity().getResources(), R.drawable.profile_gender_female2), 100));
                }

                if (cursor.getString(6) != null) {
                    mine_tv_position.setText("社团职位: " + cursor.getString(6));
                } else {
                    mine_tv_position.setText("社团职位: 未填写");
                }
                if (cursor.getString(7) != null) {
                    mine_tv_address.setText("宿舍住址: " + cursor.getString(7));
                } else {
                    mine_tv_address.setText("宿舍住址: 未填写");
                }
                if (cursor.getString(8) != null) {
                    mine_tv_hobbits.setText("兴趣爱好: " + cursor.getString(8));
                } else {
                    mine_tv_hobbits.setText("兴趣爱好: 未填写");
                }
                if (cursor.getString(9) != null) {
                    mine_tv_goal.setText("人生目标: " + cursor.getString(9));
                } else {
                    mine_tv_goal.setText("人生目标: 未填写");
                }
                if (cursor.getString(10) != null) {
                    mine_tv_demand.setText("社团要求: " + cursor.getString(10));
                } else {
                    mine_tv_demand.setText("社团要求: 未填写");
                }

                String iconPath = cursor.getString(5);
                if (iconPath != null) {
                    File iconFile = new File(iconPath);
                    if (iconFile.exists()) {
                        profile_icon.setImageBitmap(toRoundCorner
                                (BitmapFactory.decodeFile(iconPath), 100));
                    } else {
                        //如果数据库中指向的头像文件不存在直接从头像文件夹里选择最新的头像
                        File iconDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Meituan" +
                                File.separator + "crop_icons");
                        File[] iconFiles = iconDir.listFiles();
                        if (iconFiles != null) {

                            long latest = 0;
                            File latestFile = null;
                            for (int i = 0; i < iconFiles.length; i++) {
                                String fileName = iconFiles[i].getName();
                                String prefix = fileName.substring(fileName.lastIndexOf("."));//如果想获得不带点的后缀，变为fileName.lastIndexOf(".")+1
                                int num = prefix.length();//得到后缀名长度
                                String fileOtherName = fileName.substring(0, fileName.length() - num);//得到文件名。去掉了后缀
                                Log.e("fileOtherName:  ", fileOtherName);
                                if (i == 0) {
                                    latest = Long.parseLong(fileOtherName);
                                    latestFile = iconFiles[i];
                                } else {
                                    if (Long.parseLong(fileOtherName) > latest) {
                                        latest = Long.parseLong(fileOtherName);
                                        latestFile = iconFiles[i];
                                    }
                                }
                            }

                            if (latestFile != null) {
                                profile_icon.setImageBitmap(toRoundCorner(BitmapFactory.decodeFile(latestFile.getPath()), 100));
                            } else Log.e("NULL", "No icons");
                        } else { //如果头像文件夹里也没有任何头像的话则使用默认头像
                            profile_icon.setImageResource(R.drawable.default_icon);
                        }

                    }
                } else { // 如果数据库中没有头像路径则使用默认头像
                    profile_icon.setImageResource(R.drawable.default_icon);
                }

            } else Log.e("Didn't found :", "没有找到用户名下的数据");
            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.profile_icon:
                        dialogBuilder //构造选择头像的对话框
                                .withTitle("切换头像")
                                .withTitleColor("#FFcc00")
                                .withDividerColor("#11000000")
                                .withMessage("请选择照片路径")
                                .withMessageColor("#ffcc00")
                                .withDialogColor("#333333")
                                .withEffect(Effectstype.RotateLeft)
                                .withDuration(700)
                                .withButton1Text("拍照")
                                .withButton2Text("相册")
                                .isCancelableOnTouchOutside(true)
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PickImageFromCamera();
                                        dialogBuilder.cancel();
                                    }
                                })
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PickImageFromGallery();
                                        dialogBuilder.cancel();
                                    }
                                }).show();
                        break;

                    case R.id.mine_tv_setProfiles:
                        Intent intent = new Intent(getActivity(), EditProfilesActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;

                }
            }
        };
        profile_icon.setOnClickListener(listener);
        mine_tv_setProfiles.setOnClickListener(listener);
    }

    private void PickImageFromCamera() {    //使用相机拍摄获取头像
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                + "/Meituan" + "/avatar_icons/"
                + String.valueOf(System.currentTimeMillis())
                + ".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void PickImageFromGallery() {    //从相册中选择头像

        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //从图库中选择
            startActivityForResult(intent, PICK_FROM_FILE);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "相册打开异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;
            case PICK_FROM_FILE:
                imgUri = data.getData();
                doCrop();               //不管从哪里获取的头像都要进行裁剪
                break;
            case CROP_FROM_CAMERA:
                if (null != data) {
                    setCropImg(data);   //设置头像

                }
                break;
        }
    }

    private void doCrop() {

        final ArrayList<CropOption> cropOptions = new ArrayList<>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "can't find crop app", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            intent.setData(imgUri);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            // 手机上只安装了一个裁剪功能的App时调用
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                //手机上安装了多个裁剪功能的App时调用
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getActivity().getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getActivity().getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("choose a app");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (imgUri != null) {
                            getContext().getContentResolver().delete(imgUri, null, null);
                            imgUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    /**
     * set the bitmap
     *
     * @param picdata
     */
    private void setCropImg(Intent picdata) {  //设置头像
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");
            profile_icon.setImageBitmap(toRoundCorner(mBitmap, 100));  //将头像转换成圆角
            String filename = Environment.getExternalStorageDirectory() + File.separator + "Meituan" +
                    File.separator + "crop_icons" + File.separator + System.currentTimeMillis() + ".png";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("photo", filename);//key为字段名，value为值
            db.update("user", values, "name=?", new String[]{MainActivity.userName});
            db.close();
            saveBitmap(filename, mBitmap);
        }
    }

    /**
     * save the crop bitmap
     *
     * @param fileName
     * @param mBitmap
     */
    public void saveBitmap(String fileName, Bitmap mBitmap) {
        File f = new File(fileName);
        if (!f.getParentFile().exists()) {// 判断目标文件所在的目录是否存在
            // 如果目标文件所在的文件夹不存在，则创建父文件夹
            Log.e("TAG", "目标文件所在目录不存在，准备创建它！");
            if (!f.getParentFile().mkdirs()) {// 判断创建目录是否成功
                Log.e("TAG", "创建目标文件所在的目录失败！");
            }
        }
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
                Toast.makeText(getActivity(), "save success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        final float roundPx = pixels;

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        File filedir = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Meituan" + File.separator + "avatar_icons");
        File[] files = filedir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

            dbHelper = new MyDBHelper(getActivity(), "UserStore.db", null, BaseActivity.DATABASE_VERSION);

            try {
                String sql = "select * from user where name=?";
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery(sql, new String[]{MainActivity.userName});
                if (cursor.moveToFirst()) {
                    mine_tv_nickname.setText(cursor.getString(3));
                    mine_tv_position.setText("社团职位: " + cursor.getString(6));
                    mine_tv_address.setText("宿舍住址: " + cursor.getString(7));
                    mine_tv_hobbits.setText("兴趣爱好: " + cursor.getString(8));
                    mine_tv_goal.setText("人生目标: " + cursor.getString(9));
                    mine_tv_demand.setText("社团要求: " + cursor.getString(10));

                    String iconPath = cursor.getString(5);
                    if (iconPath != null) profile_icon.setImageBitmap(toRoundCorner
                            (BitmapFactory.decodeFile(iconPath), 100));
                } else Log.e("Didn't found :", "没有找到用户名下的数据");
                cursor.close();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
    }
}
