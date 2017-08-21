package com.example.doublejk.laboum.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;

import com.example.doublejk.laboum.NowPlayingPlaylist;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SQLiteHelper;
import com.example.doublejk.laboum.adapter.ViewPagerAdpater;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.User;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HomeFragment.PlaylistsChangedListener{
    private Toolbar toolbar;
    private ImageButton imageButton;
    private TabLayout tabLayout;
    private SQLiteHelper sqliteHelper;
    private ViewPager viewPager;
    private ViewPagerAdpater viewPagerAdpater;
    private static User user;
    private static HashMap<String, Playlist> playlists; //hashMap으로 관리해야할지도...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("onCreate", "뭐뭐!!");
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        sqliteHelper = new SQLiteHelper(this);

        imageButton = (ImageButton) findViewById(R.id.toolbar_imgBtn);
        imageButton.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Laboum");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //화살표
        //getSupportActionBar().setHomeAsUpIndicator(); // 화살표 이미지 변경

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        //collapsingToolbar.setContentScrimColor(Color.GREEN); //툴바가 사라지는 동안 색상 지정

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        viewPager =  (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdpater = new ViewPagerAdpater(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdpater);
        tabLayout.setupWithViewPager(viewPager);

        sqliteHelper.select();
        //시작할때 play중인 playlist 등록

        playlists = new HashMap<>();
        ArrayList<Music> musics = new ArrayList<>();
        musics = sqliteHelper.selectMusicList(NowPlayingPlaylist.title);
        if(musics != null) {
            playlists.get(NowPlayingPlaylist.title).setMusics(musics);
        }
        playlists.put(NowPlayingPlaylist.title, sqliteHelper.selectPlaylist(NowPlayingPlaylist.title));

        //MyFragment에 있는 playlist들 초기화해줘야함
        //디비에 있는 음악 가져와서 현재 플레이리스트에 저장 후 map에 플레이리스트 등록
        //onSaveInstanceState 해줘야하나?
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("메인!", "onSaveInstance");
        outState.putSerializable("playlists", playlists);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("메인!", "onRestore");
        playlists = (HashMap<String, Playlist>)savedInstanceState.getSerializable("playlists");
    }

    public static User getUser() {
        return user;
    }

    public static HashMap<String, Playlist> getPlaylists() {
        return playlists;
    }

    public static Playlist getPlaylist(String title) {
        return playlists.get(title);
    }

    public static void setPlaylists(HashMap<String, Playlist> playlists) {
        MainActivity.playlists = playlists;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_imgBtn:
                Log.d("이미지버튼", "클릭!");
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public void onChangeMusic(Playlist playlist) {
        //playlists.add(playlist);
//        MyFragment myFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":1");

        MyFragment myFragment = (MyFragment) viewPagerAdpater.instantiateItem(viewPager, 1);
        myFragment.updatePlaylist(playlist);
    }

    @Override
    public void addPlaylist(Playlist playlist) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("메인!", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("메인!", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("메인!", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("메인!", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("메인!", "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("메인!", "onRestart");
    }
}
