package com.example.jin.communitymanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import layout.BorrowFragment;
import layout.DateFragment;
import layout.HomeFragment;
import layout.MineFragment;
import layout.MoneyFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ViewPager viewPager;

    private MainViewPagerAdapter adapter;

    private AHBottomNavigation bottomNavigation;

    private Toolbar toolbar;

    private MyDBHelper dbHelper;


    private MaterialSearchView searchView;

    //做标签，说明现在是属于哪一个fragment
    public static int MARK = 0;
    public static String userName;


    //HomeFragment控件区域


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        String name = getIntent().getStringExtra("userName");
        dbHelper = new MyDBHelper(this, "UserStore.db", null, BaseActivity.DATABASE_VERSION);
        int itemId = getIntent().getIntExtra("itemId", 0);
        if (name != null) {
            userName = name;
            String sql = "select * from user where name=?";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, new String[]{MainActivity.userName});
            if (cursor.moveToFirst()) {

                if(cursor.getString(3) == null){
                    ContentValues values = new ContentValues();
                    values.put("nickname", "用户user_" + String.format("%07d", cursor.getInt(0)));//key为字段名，value为值
                    db.update("user", values, "name=?", new String[]{userName});
                    db.close();
                }
            }
            cursor.close();

        }

        List<Bitmap> bitmapList = new ArrayList<>();
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.letsdance);
        for (int i = 0; i < 5; i++) {
            bitmapList.add(bitmap1);
        }
        dbHelper = new MyDBHelper(this, "UserStore.db", null, BaseActivity.DATABASE_VERSION, bitmapList);


        ButterKnife.bind(this);
        setContentView(R.layout.activity_main);
        operateToolBar();
        initBottomNavigation();


        initViewPager();
        if (itemId != 0){
            viewPager.setCurrentItem(itemId);
            bottomNavigation.setCurrentItem(itemId);
        }

        initSearchView();


    }

    //初始化homeFragment里的操作


    //当主页背景被按下时候，关闭搜索框

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    private void operateToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    //初始化搜索框
    private void initSearchView() {


        searchView = (MaterialSearchView) findViewById(R.id.search_view);


    }


    //语音识别反馈
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    Log.d(TAG, "onActivityResult: 到if里面啦");
                    Intent intentVoice = new Intent("com.example.jin.communitymanagement.GET_THE_VOICE");
                    intentVoice.putExtra(HomeFragment.GET_THE_VOICE, searchWrd);
                    sendBroadcast(intentVoice);
                }

            }
            return;
        }


        try {
            super.onActivityResult(requestCode, resultCode, data);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.clear();
        fragments.add(new HomeFragment());
        fragments.add(new BorrowFragment());
        fragments.add(new DateFragment());
       // fragments.add(new MoneyFragment());
        fragments.add(new MineFragment());
        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments);

//        MainViewPagerAdapter.setHomeFragment(new HomeFragment());
//       MainViewPagerAdapter.setBorrowFragment(new BorrowFragment());
//        MainViewPagerAdapter.setDateFragment(new DateFragment());
//        MainViewPagerAdapter.setMoneyFragment(new MoneyFragment());
//        MainViewPagerAdapter.setMineFragment(new MineFragment());


        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                bottomNavigation.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomNavigation() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("主页", R.drawable.home, R.color.colorText);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("租赁", R.drawable.borrow, R.color.colorPrimaryDark);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("日程", R.drawable.date, R.color.colorPrimary);
       // AHBottomNavigationItem item4 = new AHBottomNavigationItem("赞助", R.drawable.money, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("我的", R.drawable.mine, R.color.colorPrimary);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
       // bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
        //设置背景颜色
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#333333"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);


// Change colors
        //选中的颜色
        bottomNavigation.setAccentColor(Color.parseColor("#FFcc00"));
        //非active的颜色
        bottomNavigation.setInactiveColor(Color.parseColor("#999999"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                viewPager.setCurrentItem(position);
                MARK = position;
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        return true;

    }
    public MyDBHelper getDbHelper() {
        return dbHelper;
    }


}
