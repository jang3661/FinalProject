package com.example.doublejk.laboum.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doublejk.laboum.tools.NowPlayingPlaylist;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.sqlite.SQLiteHelper;
import com.example.doublejk.laboum.tools.SelectedMusicProvider;
import com.example.doublejk.laboum.adapter.TracksRecyclerAdapter;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.example.doublejk.laboum.retrofit.youtube.YoutubeRetroClient;
import com.example.doublejk.laboum.util.ViewAnimation;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
   // private SearchRecyclerAdapter recyclerAdapter;
    private TracksRecyclerAdapter tracksRecyclerAdapter;
    private LinearLayoutManager linearLayout;
    private Toolbar toolbar;
    private YoutubeRetroClient youtubeRetroClient;
    private EditText searchEdit;
    private InputMethodManager imm;
    private LinkedHashMap<Integer, Music> musicMap;
    private LinearLayout selectingLayout;
    private SQLiteHelper sqLiteHelper;
    private Button resetBtn, playBtn, saveBtn;
    private  ArrayList<Music> musics;
    private String[] titleList;
    private static final String API_KEY = "AIzaSyAH-UUr_Y7XKUg7OUy38J1H6paTdbgOqGo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindowSize();

        //키보드 내리기
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //화살표

        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        selectingLayout = (LinearLayout) findViewById(R.id.search_selectLinear);
        resetBtn = (Button) findViewById(R.id.search_resetBtn);
        playBtn = (Button) findViewById(R.id.search_playBtn);
        saveBtn = (Button) findViewById(R.id.search_saveBtn);
        resetBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        musicMap = new LinkedHashMap<>();

        sqLiteHelper = new SQLiteHelper(this);
        titleList = sqLiteHelper.selectPlaylistTitle();

        getWindowSize();

        youtubeRetroClient = YoutubeRetroClient.getInstance(this).createBaseApi();

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Toast.makeText(getApplicationContext(), "검색!", Toast.LENGTH_SHORT).show();
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        showSearchList();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    SelectedMusicProvider selectedMusicProvider = new SelectedMusicProvider() {
        @Override
        public void selectedList(int pos, Music music) {
            Log.d("selected","호출");
            if(musicMap.size() == 0) {
                ViewAnimation.riseUp(selectingLayout);
            }
            musicMap.put(pos, music);
        }

        @Override
        public void unSelectedList(int pos) {
            Log.d("unselected","호출");
            musicMap.remove(pos);
            if(musicMap.size() == 0)
                 ViewAnimation.dropDwon(selectingLayout);
        }
    };
    public void getWindowSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ViewAnimation.initPos(selectingLayout, size.y);
    }

    public void initAnimation() {
        tracksRecyclerAdapter.resetMusicList();
        ViewAnimation.dropDwon(selectingLayout);
        musicMap.clear();
    }

    public void showSearchList() {
        Toast.makeText(this, "GET 1 Clicked", Toast.LENGTH_SHORT).show();
        youtubeRetroClient.getSearch(searchEdit.getText().toString(),API_KEY ,20, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("", t.toString());
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                musics = (ArrayList<Music>) receivedData;
               // new UriToPalette().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, musics);
                tracksRecyclerAdapter = new TracksRecyclerAdapter(getApplicationContext(), musics, selectedMusicProvider);
                recyclerView.setAdapter(tracksRecyclerAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

 /*               recyclerAdapter = new SearchRecyclerAdapter(getApplicationContext(), musics);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerAdapter.setItemClick(new SearchRecyclerAdapter.ItemClickListner() {
                    @Override
                    public void onItemClickListener(ArrayList<Music> items, int position) {
                        Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                        intent.putExtra("videoId", items.get(position).getVideoId());
                        startActivity(intent);
                        //타이틀도 보내자
                    }
                });*/

/*                codeResultTextView.setText(String.valueOf(code));
                idResultTextView.setText(musics.get(0).getTitle());*/
                //titleResultTextView.setText(searchData);
/*                useridResultTextView.setText(String.valueOf(data.userId));
                bodyResultTextView.setText(data.body);*/
            }

            @Override
            public void onFailure(int code) {
            }
        });
    }
//    public class UriToPalette extends AsyncTask<ArrayList<Music>, Void, ArrayList<Music>> {
//
//        @Override
//        protected ArrayList<Music> doInBackground(ArrayList<Music>... params) {
//            ArrayList<Music> items = params[0];
//            for(int i = 0; i < items.size(); i++)
//                UrlToColor.setColor(items.get(i));
//            return items;
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Music> items) {
//            super.onPostExecute(items);
//            musics = items;
//            Log.d("ㅇㅇ", "성공");
//        }
//    }
    public void setTitleList(String[] titleList) {
        this.titleList = titleList;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_resetBtn:
                tracksRecyclerAdapter.resetMusicList();
                musicMap.clear();
                break;
            case R.id.search_playBtn:
                //현재 플레이리스트에 저장, 디비에도 저장
                //MainActivity.getPlaylist(NowPlayingPlaylist.title).setMusics(musicMap);
                Playlist playlist = MainActivity.getPlaylist(NowPlayingPlaylist.title);
                //처음 클릭한 노래부터 재생하기 위한 변수
                int playingPos = playlist.getMusics().size();
                playlist.add(musicMap);
                sqLiteHelper.insert(musicMap, NowPlayingPlaylist.title);
                //myFragment 갱신, 선택한 곡들 추가
                //mCallback.onChangeMusic(playlist);
//                MyFragment myFragment  = (MyFragment) getFragmentManager().findFragmentById(R.);
//                myFragment.updatePlaylist(playlist);

                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("playlist", playlist);
                intent.putExtra("playingPos", playingPos);
                startActivity(intent); //parcel 시리얼라이즈

                //액티비티 중지되지 않을까?
                initAnimation();
                break;

            case R.id.saveMusic:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("재생목록")
                        .setItems(titleList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(titleList.length -1 == which) {
                                    //새 재생목록에 추가
                                    showDialog();
                                }else {
                                    //기존 재생목록에 추가
                                    Playlist clickedPlaylist = MainActivity.getPlaylist(titleList[which]);
                                    clickedPlaylist.setMusics(musicMap);
                                    sqLiteHelper.insert(musicMap, clickedPlaylist.getTitle());
                                    //기존재생목록이 nowPlayling이면 추가한 노래 재생해야한다
                                    initAnimation();
                                    //musicMap을 playlist에 저장, 디비에도 저장

                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }
    void showDialog() {
        //보낼 정보 있으면 보내자
        Log.d("오오", ""+musicMap.size());
        DialogFragment newFragment = NewPlaylistDialogFragment.newInstance(musicMap);
        newFragment.show(getSupportFragmentManager(), "dialog");
        titleList = sqLiteHelper.selectPlaylistTitle();
    }
}
