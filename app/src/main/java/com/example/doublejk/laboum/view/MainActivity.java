package com.example.doublejk.laboum.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.NowPlayingPlaylist;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SQLiteHelper;
import com.example.doublejk.laboum.adapter.ViewPagerAdpater;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.User;
import com.example.doublejk.laboum.util.ViewAnimation;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements HomeFragment.PlaylistsChangedListener,
        NewPlaylistDialogFragment.onAddPlaylistListener, ViewPager.OnPageChangeListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_searchBtn) ImageButton imageButton;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    private static HashMap<String, Playlist> playlists;
    private static User user;
    private SQLiteHelper sqliteHelper;
    private ViewPagerAdpater viewPagerAdpater;
    private ArrayList<Music> musics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("메인!!", "oncreate");
        initView();

        //playlist 정보 가져온다.

//        MyFragment myFragment = (MyFragment) viewPagerAdpater.instantiateItem(viewPager, 1);
//        myFragment.addPlaylist(playlists);

//        musics = new ArrayList<>();
//        musics = sqliteHelper.selectMusicList(NowPlayingPlaylist.title);
//        if(musics != null) {
//            playlists.get(NowPlayingPlaylist.title).setMusics(musics);
//        }
//        playlists.put(NowPlayingPlaylist.title, sqliteHelper.selectPlaylist(NowPlayingPlaylist.title));

        //MyFragment에 있는 playlist들 초기화해줘야함
        //디비에 있는 음악 가져와서 현재 플레이리스트에 저장 후 map에 플레이리스트 등록
        //onSaveInstanceState 해줘야하나?
    }

    public void initView() {
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        sqliteHelper = new SQLiteHelper(this);

        playlists = new HashMap<>();
        sqliteHelper.uploadPlaylists(playlists);
        sqliteHelper.select();

        Iterator<String> keys = playlists.keySet().iterator();
        while(keys.hasNext()) {
            Log.d("Select", "" + playlists.get(keys.next()).getTitle());
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Laboum");
//        getSupportActionBar().setIcon();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //화살표
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon); // 화살표 이미지 변경

        collapsingToolbar.setTitleEnabled(false);
        //collapsingToolbar.setContentScrimColor(Color.GREEN); //툴바가 사라지는 동안 색상 지정

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(this);
        viewPagerAdpater = new ViewPagerAdpater(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPagerAdpater.setPlaylist(playlists);
        viewPager.setAdapter(viewPagerAdpater);
        tabLayout.setupWithViewPager(viewPager);
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

    @OnClick(R.id.toolbar_searchBtn)
    public void onSearchBtnClick() {
        Log.d("이미지버튼", "클릭!");
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
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
//        MyFragment myFragment = (MyFragment) viewPagerAdpater.instantiateItem(viewPager, 1);
//        myFragment.addPlaylist(playlist);
    }

    @Override
    public void addNewPlaylist(Playlist playlist) {
        Log.d("안갔냐", "ㅇㅇ" + playlist.getMusics().size());
        MyFragment myFragment = (MyFragment) viewPagerAdpater.instantiateItem(viewPager, 1);
        myFragment.addPlaylist(playlist);

        HomeFragment homeFragment = (HomeFragment) viewPagerAdpater.instantiateItem(viewPager, 0);
        homeFragment.setTitleList(sqliteHelper.selectPlaylistTitle());
        homeFragment.initAnimation();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 1) {
            Log.d("변화", "ㅎ자");
            MyFragment myFragment = (MyFragment) viewPagerAdpater.instantiateItem(viewPager, 1);
            myFragment.getMyTabRecyclerAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
//        Intent intent = getIntent();
//        String s = intent.getStringExtra("room");
//        if(s != null)
//            Log.d("왔냐!!", s);
//        else
//            Log.d("왔냐!!", "null" );
        if (getIntent().getExtras() != null) {
            Log.d("오냐", "뭥미");
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("Message", "Key: " + key + " Value: " + String.valueOf(value));
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("메인!", "onRestart");
    }
}
