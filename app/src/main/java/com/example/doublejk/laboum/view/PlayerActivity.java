package com.example.doublejk.laboum.view;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.RecyclerItemClickListener;
import com.example.doublejk.laboum.adapter.PlaylistRecyclerAdapter;
import com.example.doublejk.laboum.retrofit.SearchItem;
import com.example.doublejk.laboum.util.DimensionConverter;
import com.example.doublejk.laboum.util.GlidePalette;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        YouTubePlayer.OnFullscreenListener, View.OnClickListener {
    YouTubePlayerView youTubePlayerView;
    static final String YOUTUBE_KEY = "AIzaSyBwqHpHu9AwlEfiIVKcJ4rsBWOfgP6WmB0";
    private RecyclerView recyclerView;
    private PlaylistRecyclerAdapter playlistRecyclerAdapter;
    private LinkedHashMap<Integer, SearchItem> musics;
    private ArrayList<SearchItem> searchItems;
    private YouTubePlayer youTubePlayer;
    private List<String> videoIds;
    private MyPlaybackEventListener myPlaybackEventListener;
    private MyPlayerStateChangeListener myPlayerStateChangeListener;
    private MyPlaylistEventListener myPlaylistEventListener;
    private GlidePalette glidePalette;
    private FrameLayout playerLayout;
    private LinearLayout divider;
    private Window window;
    private ImageButton playBtn, previousBtn, nextBtn, fullSreenBtn;
    private TextView currentTimeTv, durationMillisTv;
    private int currentTime;

    private final int REPEAT_PLAY = 0;
    private final int ONE_REPEAT_PLAY = 1;
    private final int RANDOM_PLAY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        videoIds = new ArrayList<>();
        glidePalette = new GlidePalette();
        playerLayout = (FrameLayout) findViewById(R.id.player_linear);
        divider = (LinearLayout) findViewById(R.id.player_divider);

        playBtn = (ImageButton) findViewById(R.id.playBtn);
        nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        previousBtn = (ImageButton) findViewById(R.id.previousBtn);
        fullSreenBtn = (ImageButton) findViewById(R.id.fullscreenBtn);
        currentTimeTv = (TextView) findViewById(R.id.currentTimeTv);
        durationMillisTv = (TextView) findViewById(R.id.durationMillisTv);

        playBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        fullSreenBtn.setOnClickListener(this);

        receiveDataInit();

       // new UriToPalette().execute(searchItems);
        myPlaybackEventListener = new MyPlaybackEventListener();
        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
        myPlaylistEventListener = new MyPlaylistEventListener();

        recyclerView = (RecyclerView) findViewById(R.id.player_recyclerview);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, ));
        //recyclerView.addItemDecoration(new CustomDividerItemDecoration(this));

        playlistRecyclerAdapter = new PlaylistRecyclerAdapter(this, searchItems);
        playlistRecyclerAdapter.setPlayingMusicPostion(0); //현재 재생중인 노래 index
        recyclerView.setAdapter(playlistRecyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //window, layout, 각 아이템 색변경
                //view.setBackgroundColor(searchItems.get(position).getBackGround());
               // playlistRecyclerAdapter.notifyDataSetChanged();
                youTubePlayer.loadVideos(videoIds, position, 0);
                updateData(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        backGroundColorInit();
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(YOUTUBE_KEY, this);
    }

    public void backGroundColorInit() {
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(searchItems.get(0).getPaletteColor().getDarkMutedRgb());
        playerLayout.setBackgroundColor(searchItems.get(0).getPaletteColor().getMutedRgb());
        if(searchItems.size() < 5) {
            divider.setBackgroundColor(searchItems.get(0).getPaletteColor().getDarkMutedRgb());
            divider.setVisibility(View.VISIBLE);
        } else {
            //나중에 다른곳도 추가해줘야한다. 재생목록 추가삭제할때
            recyclerView.setPadding(0, 0, 0, DimensionConverter.dpToPx(72));
        }
    }
    public void receiveDataInit() {
        Gson gson = new Gson();
        musics = new LinkedHashMap<>();
        String musicList = getIntent().getStringExtra("musicInfo");
        Type entityType = new TypeToken<LinkedHashMap<Integer, SearchItem>>(){}.getType();
        musics = gson.fromJson(musicList, entityType);

        Iterator<Integer> keys = musics.keySet().iterator();
        searchItems = new ArrayList<>();
        while (keys.hasNext()) {
            final SearchItem searchItem = musics.get(keys.next());
            searchItems.add(searchItem);
            videoIds.add(searchItem.getVideoId());
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        Log.d("onInitializationSuccess", "한번만?");
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        youTubePlayer.setPlaylistEventListener(myPlaylistEventListener);
        youTubePlayer.setPlayerStateChangeListener(myPlayerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(myPlaybackEventListener);
        youTubePlayer.setOnFullscreenListener(this);
        //youTubePlayer.setFullscreenControlFlags(youTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        youTubePlayer.loadVideos(videoIds);
    }
    public void showLog(String s) {
        Log.d(s, "보여라");
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d("실패이유", youTubeInitializationResult.toString());
    }

    @Override
    public void onFullscreen(boolean b) {
        Log.d("onFullscreen", "" + b);
        if(!b) {
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playBtn:
                if(youTubePlayer.isPlaying()) {
                    playBtn.setImageResource(R.drawable.playbtn);
                    youTubePlayer.pause();
                }else {
                    playBtn.setImageResource(R.drawable.pausebtn);
                    youTubePlayer.play();
                }
                break;
            case R.id.nextBtn:
                if(youTubePlayer.hasNext()) {
                    youTubePlayer.next();
                    updateData(playlistRecyclerAdapter.getPlayingMusicPostion() +1);
                }else {
                    youTubePlayer.loadVideos(videoIds, 0, 0);
                    updateData(0);
                }
                break;
            case R.id.previousBtn:
                if(youTubePlayer.hasPrevious()) {
                    youTubePlayer.previous();
                    updateData(playlistRecyclerAdapter.getPlayingMusicPostion() -1);
                }else {
                    youTubePlayer.loadVideos(videoIds, videoIds.size()-1, 0);
                    updateData(videoIds.size() -1);
                }
                break;
            case R.id.fullscreenBtn:
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.setFullscreen(true);
        }
    }

    public void updateData(int position) {
        window.setStatusBarColor(searchItems.get(position).getPaletteColor().getDarkMutedRgb());
        playerLayout.setBackgroundColor(searchItems.get(position).getPaletteColor().getMutedRgb());
        //divider 색변경
        if(searchItems.size() < 5) {
            divider.setBackgroundColor(searchItems.get(position).getPaletteColor().getDarkMutedRgb());
        }
        else {
            divider.setVisibility(View.GONE);
        }
        playlistRecyclerAdapter.setPlayingMusicPostion(position);
        playlistRecyclerAdapter.notifyDataSetChanged();
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            showLog("onPlaying!");
            playBtn.setImageResource(R.drawable.pausebtn);
        }

        @Override
        public void onPaused() {
            showLog("onPaused");
        }

        @Override
        public void onStopped() {
            showLog("onStopped!");
        }

        @Override
        public void onBuffering(boolean b) {
            showLog("onBuffering!");
        }

        @Override
        public void onSeekTo(int i) {
            showLog("onSeekTo!");
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            showLog("onLoading!");
        }

        @Override
        public void onLoaded(String s) {
            showLog("onLoaded!");
        }

        @Override
        public void onAdStarted() {
            showLog("onAdStarted!");
        }

        @Override
        public void onVideoStarted() {
            showLog("onVideoStarted!");
        }

        @Override
        public void onVideoEnded() {
            showLog("onVideoEnded");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Log.d("onError", "" + errorReason);
        }
    }

    private final class MyPlaylistEventListener implements YouTubePlayer.PlaylistEventListener {

        @Override
        public void onPrevious() {
            showLog("onPrevious!");
        }

        @Override
        public void onNext() {
            showLog("onNext!");
        }

        @Override
        public void onPlaylistEnded() {
            showLog("onPlaylistEnded!");
        }
    }

     /*public class UriToPalette extends AsyncTask<ArrayList<SearchItem>, Void, ArrayList<PaletteColor>> {

        @Override
        protected ArrayList<PaletteColor> doInBackground(ArrayList<SearchItem>... params) {
            ArrayList<PaletteColor> colors = new ArrayList<>();
            ArrayList<SearchItem> items = params[0];
            PaletteColor paletteColor;
            for(int i = 0; i < items.size(); i++) {
                try {
                    Log.d("맞냐", ""+items.get(i).getImgUrl());
                    URL url = new URL(items.get(i).getImgUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Palette palette = Palette.from(bitmap).generate();

                    if(palette == null) {
                        Log.d("놀이냐", "");
                        paletteColor = new PaletteColor(0, 0, 0);
                    }
                    else {
                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                        if (vibrantSwatch != null) {
                            paletteColor = new PaletteColor(vibrantSwatch.getRgb(),
                                    vibrantSwatch.getTitleTextColor(), vibrantSwatch.getBodyTextColor());
                            Log.d("응응", "" + vibrantSwatch.getRgb());
                        }
                        else {
                            paletteColor = new PaletteColor(0, 0, 0);
                        }

                    }

                    colors.add(paletteColor);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return colors;
        }

    *//*      Palette.Swatch mutedSwatch = palette.getMutedSwatch();
          if(mutedSwatch != null) {
              hashMap.put("mutedRgb", mutedSwatch.getRgb());
              hashMap.put("mutedTitle", mutedSwatch.getTitleTextColor());
              hashMap.put("mutedBody", mutedSwatch.getBodyTextColor());
          }*//*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<PaletteColor> colors) {
            super.onPostExecute(colors);
            for(int i = 0; i < searchItems.size(); i++) {
                Log.d("과연", "" + colors.get(i).getVibrantRgb());
                searchItems.get(i).setPaletteColor(colors.get(i));
            }
        }
    }*/
}
