package com.example.doublejk.laboum.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.RecyclerItemClickListener;
import com.example.doublejk.laboum.adapter.PlaylistRecyclerAdapter;
import com.example.doublejk.laboum.retrofit.SearchItem;
import com.example.doublejk.laboum.util.CustomDividerItemDecoration;
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

public class PlaylistActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
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
    private LinearLayout playlistLayout, divider;
    private Window window;

    private final int REPEAT_PLAY = 0;
    private final int ONE_REPEAT_PLAY = 1;
    private final int RANDOM_PLAY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        videoIds = new ArrayList<>();
        glidePalette = new GlidePalette();
        playlistLayout = (LinearLayout) findViewById(R.id.playlist_linear);
        divider = (LinearLayout) findViewById(R.id.playlist_divider);

        receiveDataInit();
        backGroundColorInit();
       // new UriToPalette().execute(searchItems);
        myPlaybackEventListener = new MyPlaybackEventListener();
        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
        myPlaylistEventListener = new MyPlaylistEventListener();

        recyclerView = (RecyclerView) findViewById(R.id.playlist_recyclerview);
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
                //view.setBackgroundColor(searchItems.get(position).getBackGround());
               // playlistRecyclerAdapter.notifyDataSetChanged();
                youTubePlayer.loadVideos(videoIds, position, 0);
                window.setStatusBarColor(searchItems.get(position).getPaletteColor().getDarkMutedRgb());
                playlistLayout.setBackgroundColor(searchItems.get(position).getPaletteColor().getMutedRgb());
                if(searchItems.size() < 7)
                    divider.setBackgroundColor(searchItems.get(position).getPaletteColor().getDarkMutedRgb());
                playlistRecyclerAdapter.setPlayingMusicPostion(position);
                playlistRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(YOUTUBE_KEY, this);
    }

    public void backGroundColorInit() {
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(searchItems.get(0).getPaletteColor().getDarkMutedRgb());
        playlistLayout.setBackgroundColor(searchItems.get(0).getPaletteColor().getMutedRgb());
        if(searchItems.size() < 7)
            divider.setBackgroundColor(searchItems.get(0).getPaletteColor().getDarkMutedRgb());
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
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        youTubePlayer.setPlaylistEventListener(myPlaylistEventListener);
        youTubePlayer.setPlayerStateChangeListener(myPlayerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(myPlaybackEventListener);

        youTubePlayer.loadVideos(videoIds);
    }
    public void showLog(String s) {
        Log.d(s, "보여라");
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d("실패이유", youTubeInitializationResult.toString());
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            showLog("onPlaying!");
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
            showLog("onError!");
        }
    }

    private final class MyPlaylistEventListener implements YouTubePlayer.PlaylistEventListener {

        @Override
        public void onPrevious() {
            showLog("onPrevious!");
            int position = playlistRecyclerAdapter.getPlayingMusicPostion() -1;
            window.setStatusBarColor(searchItems.get(position).getPaletteColor().getDarkMutedRgb());
            playlistLayout.setBackgroundColor(searchItems.get(position).getPaletteColor().getMutedRgb());
            if(searchItems.size() < 7)
                divider.setBackgroundColor(searchItems.get(position).getPaletteColor().getDarkMutedRgb());
            playlistRecyclerAdapter.setPlayingMusicPostion(position);
            playlistRecyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNext() {
            showLog("onNext!");
            int position = playlistRecyclerAdapter.getPlayingMusicPostion() +1;
            window.setStatusBarColor(searchItems.get(position).getPaletteColor().getDarkMutedRgb());
            playlistLayout.setBackgroundColor(searchItems.get(position).getPaletteColor().getMutedRgb());
            if(searchItems.size() < 7)
                divider.setBackgroundColor(searchItems.get(position).getPaletteColor().getDarkMutedRgb());
            playlistRecyclerAdapter.setPlayingMusicPostion(position);
            playlistRecyclerAdapter.notifyDataSetChanged();
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
