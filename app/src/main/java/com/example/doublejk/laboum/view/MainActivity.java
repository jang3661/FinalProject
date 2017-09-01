package com.example.doublejk.laboum.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.sqlite.SQLiteHelper;
import com.example.doublejk.laboum.adapter.ViewPagerAdpater;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.Room;
import com.example.doublejk.laboum.model.User;
import com.example.doublejk.laboum.retrofit.nodejs.NodeRetroClient;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.example.doublejk.laboum.util.ViewAnimation;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements TracksFragment.PlaylistsChangedListener,
        NewPlaylistDialogFragment.onAddPlaylistListener, ViewPager.OnPageChangeListener, HomeFragment.ReplaceFrgmentListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_searchBtn) ImageButton searchBtn;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    private static HashMap<String, Playlist> playlists;
    private static User user;
    private TracksFragment tracksFragment;
    private SQLiteHelper sqliteHelper;
    private ViewPagerAdpater viewPagerAdpater;
    private ArrayList<Music> musics;
    private NodeRetroClient nodeRetroClient;
    private ArrayList<Room> rooms;
    private boolean isHomeChanged; //홈화면 클릭했을때
    private String titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("메인!!", "oncreate");
        initView();
        nodeRetroClient = NodeRetroClient.getInstance(this).createBaseApi();

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

//        Iterator<String> keys = playlists.keySet().iterator();
//        while(keys.hasNext()) {
//            Log.d("Select", "" + playlists.get(keys.next()).getTitle());
//        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LaBoum");
        getSupportActionBar().setIcon(R.drawable.main_icon);

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

    public void getRoomList() {
        Toast.makeText(this, "RoomLIst", Toast.LENGTH_SHORT).show();

        nodeRetroClient.getRoomList(new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("onError", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d("onSuccess", "" + code + " " + receivedData.toString());
                rooms = (ArrayList<Room>) receivedData;
                if(rooms != null) {
                    ShareFragment shareFragment = (ShareFragment) viewPagerAdpater.instantiateItem(viewPager, 2);
                    shareFragment.updateRoomList(rooms);
                }
            }
            @Override
            public void onFailure(int code) {
                Log.d("onFailure", "" + code);
            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            restoreHomeFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
            restoreHomeFragment();
        }
    }

    public void restoreHomeFragment() {
        isHomeChanged = false;
        getSupportActionBar().setTitle("LaBoum");
        getSupportActionBar().setIcon(R.drawable.main_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ViewAnimation.selectLayoutToTab(tracksFragment.getSelectingLayout(), getTabLayout());
        HomeFragment homeFragment = (HomeFragment) viewPagerAdpater.instantiateItem(viewPager, 0);
        homeFragment.getHideLayout().setVisibility(View.VISIBLE);
    }

    @Override
    public void addNewPlaylist(Playlist playlist) {
        Log.d("안갔냐", "ㅇㅇ" + playlist.getMusics().size());
        MyFragment myFragment = (MyFragment) viewPagerAdpater.instantiateItem(viewPager, 1);
        myFragment.addPlaylist(playlist);

        tracksFragment.setTitleList(sqliteHelper.selectPlaylistTitle());
        tracksFragment.initAnimation();
    }

    @Override
    public void onReplace(ArrayList<Music> musics, String name) {
        //이건 일간순위 눌렀을때 이벤트임 다른 이벤트도 고려해야함
        isHomeChanged = true;
        titleName = name;
        HomeFragment homeFragment = (HomeFragment) viewPagerAdpater.instantiateItem(viewPager, 0);
        homeFragment.getHideLayout().setVisibility(View.GONE);
        getSupportActionBar().setTitle(name); //이거 그때그때 바꿔줘야함
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        tracksFragment = TracksFragment.newInstance(musics);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_layout,tracksFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(isHomeChanged) {
            if(position != 0) {
                getSupportActionBar().setTitle("LaBoum");
                getSupportActionBar().setIcon(R.drawable.main_icon);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }else {
                getSupportActionBar().setTitle(titleName); //이거 그때그때 바꿔줘야함
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
            }
        }
        if(position == 1) {
            Log.d("변화", "ㅎ자");
            MyFragment myFragment = (MyFragment) viewPagerAdpater.instantiateItem(viewPager, 1);
            myFragment.getMyTabRecyclerAdapter().notifyDataSetChanged();
        }else if(position == 2) {
            getRoomList();
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
