package com.example.doublejk.laboum.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.doublejk.laboum.view.HomeFragment;
import com.example.doublejk.laboum.view.MyFragment;

/**
 * Created by doublejk on 2017-08-07.
 */

public class ViewPagerAdpater extends FragmentStatePagerAdapter{

    int tabCount;
    public ViewPagerAdpater(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                 return "Home";
            case 1:
                return "My";
            case 2:
                return "공유";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("겟아이템" ,"getItem");
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return MyFragment.newInstance();
            case 2:
                return HomeFragment.newInstance();
            default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
