package com.example.doublejk.laboum.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.view.HomeFragment;
import com.example.doublejk.laboum.view.TracksFragment;
import com.example.doublejk.laboum.view.MyFragment;
import com.example.doublejk.laboum.view.ShareFragment;

import java.util.HashMap;

/**
 * Created by doublejk on 2017-08-07.
 */

public class ViewPagerAdpater extends FragmentStatePagerAdapter{

    private int tabCount;
    private HashMap<String, Playlist> playlists;

    public ViewPagerAdpater(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    public void setPlaylist(HashMap<String, Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                 return "Home";
            case 1:
                return "My";
            case 2:
                return "Share";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("프래그먼트" ,"getItem");
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                Log.d("프래그먼트" ,"getItem");
                return MyFragment.newInstance(playlists);
            case 2:
                return ShareFragment.newInstance();
            default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
