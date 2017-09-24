package com.example.jin.communitymanagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import layout.BorrowFragment;
import layout.DateFragment;
import layout.HomeFragment;
import layout.MineFragment;
import layout.MoneyFragment;

/**
 * Created by summe on 2017/8/15.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private static List<Fragment> fragments = new ArrayList<>();
    public Fragment currentFragment;
  //  private static   HomeFragment homeFragment=null;
//    private  static BorrowFragment borrowFragment=null;
//    private static DateFragment dateFragment=null;
//    private static MineFragment mineFragment=null;
//    private static MoneyFragment moneyFragment=null;

    public MainViewPagerAdapter(FragmentManager fm, List<Fragment> afragments) {
        super(fm);
        fragments.clear();
        fragments.addAll(afragments);

    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        this.currentFragment= (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);

    }

    @Override
    public int getCount() {
        return fragments.size();

    }

//    public static HomeFragment getHomeFragment() {
//        return homeFragment;
//    }
//
//    public static void setHomeFragment(HomeFragment homeFragment) {
//        fragments.add(homeFragment);
//        MainViewPagerAdapter.homeFragment = homeFragment;
//    }
//
//    public static BorrowFragment getBorrowFragment() {
//        return borrowFragment;
//    }
//
//    public static void setBorrowFragment(BorrowFragment borrowFragment) {
//        fragments.add(borrowFragment);
//        MainViewPagerAdapter.borrowFragment = borrowFragment;
//    }
//
//    public static DateFragment getDateFragment() {
//        return dateFragment;
//    }
//
//    public static void setDateFragment(DateFragment dateFragment) {
//        fragments.add(dateFragment);
//        MainViewPagerAdapter.dateFragment = dateFragment;
//    }
//
//    public static MineFragment getMineFragment() {
//        return mineFragment;
//    }
//
//    public static void setMineFragment(MineFragment mineFragment) {
//        fragments.add(mineFragment);
//        MainViewPagerAdapter.mineFragment = mineFragment;
//    }
//
//    public static MoneyFragment getMoneyFragment() {
//        return moneyFragment;
//    }
//
//    public static void setMoneyFragment(MoneyFragment moneyFragment) {
//        fragments.add(moneyFragment);
//        MainViewPagerAdapter.moneyFragment = moneyFragment;
//    }
}
