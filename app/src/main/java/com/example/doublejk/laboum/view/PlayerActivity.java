package com.example.doublejk.laboum.view;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doublejk.laboum.PlayerControlProvider;
import com.example.doublejk.laboum.PushEvent;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.RecyclerItemClickListener;
import com.example.doublejk.laboum.adapter.PlayerRecyclerAdapter;
import com.example.doublejk.laboum.firebase.FirebaseMessage;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.Room;
import com.example.doublejk.laboum.retrofit.FCMRetroClient;
import com.example.doublejk.laboum.retrofit.NodeRetroClient;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.example.doublejk.laboum.util.DimensionConverter;
import com.example.doublejk.laboum.util.GlidePalette;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        YouTubePlayer.OnFullscreenListener, SeekBar.OnSeekBarChangeListener{
    YouTubePlayerView youTubePlayerView;
    static final String YOUTUBE_KEY = "AIzaSyBwqHpHu9AwlEfiIVKcJ4rsBWOfgP6WmB0";
    private RecyclerView recyclerView;
    private PlayerRecyclerAdapter playerRecyclerAdapter;
    private LinkedHashMap<Integer, Music> PlayerMusics;
    private ArrayList<Music> musics;
    private YouTubePlayer youTubePlayer;
    private List<String> videoIds;
    private MyPlaybackEventListener myPlaybackEventListener;
    private MyPlayerStateChangeListener myPlayerStateChangeListener;
    private MyPlaylistEventListener myPlaylistEventListener;
    private GlidePalette glidePalette;
    @BindView(R.id.player_layout)  FrameLayout playerLayout;
    @BindView(R.id.player_divider) LinearLayout divider;
    @BindView(R.id.player_playBtn) ImageButton playBtn;
    @BindView(R.id.player_previousBtn) ImageButton previousBtn;
    @BindView(R.id.player_nextBtn) ImageButton nextBtn;
    @BindView(R.id.player_fullscreenBtn) ImageButton fullSreenBtn;
    @BindView(R.id.player_randomBtn) ImageButton randomBtn;
    @BindView(R.id.player_repeatBtn) ImageButton repeatBtn;
    @BindView(R.id.player_sharebtn) ImageButton shareBtn;
    @BindView(R.id.player_currentTimeTv) TextView currentTimeTv;
    @BindView(R.id.player_durationTv) TextView durationTv;
    @BindView(R.id.player_playlist_name) TextView playlistName;
    @BindView(R.id.player_user_name) TextView userName;
    @BindView(R.id.player_seekbar) SeekBar seekBar;
    private Window window;
    private TimerTask timerTask;
    private Timer timer;
    private Playlist playlist;
    private boolean isPlayStarted, doTouchPlayer, isClickedRandomBtn, isClickedShareBtn;
    private int stateFlag;
    private NodeRetroClient nodeRetroClient;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Room myRoom;
    private FCMRetroClient fcmRetroClient;


    private final int ORDINARY_PLAY = 0;
    private final int REPEAT_PLAY = 1;
    private final int ONE_REPEAT_PLAY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        PlayerControlProvider.getInstance().register(this);
        ButterKnife.bind(this);
        Log.d("onCreate", "오오");
        nodeRetroClient = NodeRetroClient.getInstance(this).createBaseApi();
        fcmRetroClient = FCMRetroClient.getInstance(this).createBaseApi();

        videoIds = new ArrayList<>();
        glidePalette = new GlidePalette();

        seekBar.setOnSeekBarChangeListener(this);

        receiveDataInit();

       // new UriToPalette().execute(musics);
        myPlaybackEventListener = new MyPlaybackEventListener();
        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
        myPlaylistEventListener = new MyPlaylistEventListener();

        recyclerView = (RecyclerView) findViewById(R.id.player_recyclerview);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, ));
        //recyclerView.addItemDecoration(new CustomDividerItemDecoration(this));

        playerRecyclerAdapter = new PlayerRecyclerAdapter(this, musics);
        playerRecyclerAdapter.setPlayingMusicPostion(0); //현재 재생중인 노래 index
        recyclerView.setAdapter(playerRecyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                doTouchPlayer = true;
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

        timer = new Timer();
    }
    public void postGroupMsg(FirebaseMessage firebaseMessage) {
        fcmRetroClient.postGroupMsg(firebaseMessage, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d("외잉", receivedData.toString());
            }
            @Override
            public void onFailure(int code) {
            }
        });
    }

    @Subscribe
    public void playerControl(PushEvent mPushEvent) {
        if(mPushEvent.getPushData().get("action").equals("next")) {
            doTouchPlayer = true;
            if(playerRecyclerAdapter.getPlayingMusicPostion() < videoIds.size() -1) {
                updateData(playerRecyclerAdapter.getPlayingMusicPostion() +1);
                youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), 0);
            }else {
                updateData(0);
                youTubePlayer .loadVideos(videoIds, 0, 0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sf = getSharedPreferences("pref", 0);
        stateFlag = sf.getInt("stateFlag", 0);
        if (stateFlag == ORDINARY_PLAY) {
            repeatBtn.setImageResource(R.drawable.unrepeatbtn);
        } else if (stateFlag == REPEAT_PLAY) {
            repeatBtn.setImageResource(R.drawable.repeatbtn);
        } else {
            repeatBtn.setImageResource(R.drawable.onerepeatbtn);
        }
        isClickedRandomBtn = sf.getBoolean("randomBtn", false);
        if(isClickedRandomBtn) {
            randomBtn.setImageResource(R.drawable.randombtn);
        }else {
            randomBtn.setImageResource(R.drawable.unrandombtn);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sf = getSharedPreferences("pref", 0);
        editor = sf.edit();
        editor.putInt("stateFlag", stateFlag);
        editor.putBoolean("randomBtn", isClickedRandomBtn);
        editor.commit();
        playBtn.setImageResource(R.drawable.playbtn);
    }

    public TimerTask timerTaskMaker() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                PlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //back키 눌렀을때 이벤트처리
                        currentTimeTv.setText(convertTime(youTubePlayer.getCurrentTimeMillis()));
                        seekBar.setProgress(youTubePlayer.getCurrentTimeMillis());
                    }
                });
            }
        };
        return timerTask;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(timerTask != null) {
            timerTask.cancel();
        }
    }

    public String convertTime(int currentTime) {
        int seconds = ( currentTime / 1000 ) % 60;
        int minutes = ( currentTime / 60000 ) % 60;
        int hours = ( currentTime / 3600000 );

        String time = "";

        if(hours > 0) {
            time += String.valueOf(hours) + ":";
        }
        if(minutes < 10) {
            time += "0" + String.valueOf(minutes) + ":";
        }else {
            time += String.valueOf(minutes) + ":";
        }
        if(seconds < 10) {
            time += "0" + String.valueOf(seconds);
        } else {
            time += String.valueOf(seconds);
        }
        return time;
    }

    public void backGroundColorInit() {
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //window.setStatusBarColor(musics.get(0).getPaletteColor().getDarkVibrantRgb());
        playerLayout.setBackgroundColor(musics.get(0).getPaletteColor().getVibrantRgb());
        if(musics.size() < 5) {
            divider.setBackgroundColor(musics.get(0).getPaletteColor().getDarkVibrantRgb());
            divider.setVisibility(View.VISIBLE);
        } else {
            //나중에 다른곳도 추가해줘야한다. 재생목록 추가삭제할때
            recyclerView.setPadding(0, 0, 0, DimensionConverter.dpToPx(72));
        }
        playlistName.setText(playlist.getTitle());
        userName.setText(playlist.getUserName());
    }
    public void receiveDataInit() {
//        Gson gson = new Gson();
//        PlayerMusics = new LinkedHashMap<>();
//        String musicList = getIntent().getStringExtra("musicInfo");
//        Type entityType = new TypeToken<LinkedHashMap<Integer, Music>>(){}.getType();
//        PlayerMusics = gson.fromJson(musicList, entityType);
//
//        Iterator<Integer> keys = PlayerMusics.keySet().iterator();
//        musics = new ArrayList<>();
//        while (keys.hasNext()) {
//            final Music music = PlayerMusics.get(keys.next());
//            musics.add(music);
//            videoIds.add(music.getVideoId());
//        }

//        musics = new ArrayList<>();
//        musics = getIntent().getParcelableArrayListExtra("musicInfo");
//        for(int i = 0; i < musics.size(); i++) {
//            videoIds.add(musics.get(i).getVideoId());
//        }
        playlist = getIntent().getParcelableExtra("playlist");
        musics = playlist.getMusics();
        for(int i = 0; i < musics.size(); i++) {
            videoIds.add(musics.get(i).getVideoId());
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

    @OnClick({R.id.player_playBtn, R.id.player_nextBtn, R.id.player_previousBtn,
            R.id.player_fullscreenBtn, R.id.player_repeatBtn, R.id.player_randomBtn, R.id.player_sharebtn})
    public void onPlayerClick(View v) {
        switch (v.getId()) {
            case R.id.player_playBtn:
                if(youTubePlayer.isPlaying()) {
                    playBtn.setImageResource(R.drawable.playbtn);
                    youTubePlayer.pause();
                }else {
                    playBtn.setImageResource(R.drawable.pausebtn);
                    youTubePlayer.play();
                }
                break;
            case R.id.player_nextBtn:
                postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "next"));
                doTouchPlayer = true;
                if(playerRecyclerAdapter.getPlayingMusicPostion() < videoIds.size() -1) {
                    updateData(playerRecyclerAdapter.getPlayingMusicPostion() +1);
                    youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), 0);
                }else {
                    updateData(0);
                    youTubePlayer .loadVideos(videoIds, 0, 0);
                }
                break;
            case R.id.player_previousBtn:
                doTouchPlayer = true;
                if(playerRecyclerAdapter.getPlayingMusicPostion() > 0) {
                    updateData(playerRecyclerAdapter.getPlayingMusicPostion() -1);
                    youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), 0);
                }else {
                    updateData(videoIds.size() -1);
                    youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), 0);
                }
                break;
            case R.id.player_fullscreenBtn:
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.setFullscreen(true);
                break;
            case R.id.player_repeatBtn:
                if(stateFlag == ORDINARY_PLAY) {
                    stateFlag = REPEAT_PLAY;
                    repeatBtn.setImageResource(R.drawable.repeatbtn);
                }else if(stateFlag == REPEAT_PLAY) {
                    stateFlag = ONE_REPEAT_PLAY;
                    repeatBtn.setImageResource(R.drawable.onerepeatbtn);
                }else {
                    stateFlag = ORDINARY_PLAY;
                    repeatBtn.setImageResource(R.drawable.unrepeatbtn);
                }
                break;
            case R.id.player_randomBtn:
                if(isClickedRandomBtn) {
                    randomBtn.setImageResource(R.drawable.unrandombtn);
                    isClickedRandomBtn = false;
                }else {
                    randomBtn.setImageResource(R.drawable.randombtn);
                    isClickedRandomBtn = true;
                }
                break;
            case R.id.player_sharebtn:
                if(!isClickedShareBtn) {
                    final EditText roomName = new EditText(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    roomName.setLayoutParams(lp);

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("재생목록을 공유하시겠습니까?")
                            // Add action buttons
                            .setView(roomName)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    myRoom = new Room(roomName.getText().toString(), playlist.getUserEmail(), playlist.getUserName(), playlist.getTitle());
                                    myRoom.setPlaylist(playlist);
                                    FirebaseMessaging.getInstance().subscribeToTopic(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')));
                                    postCreateRoom(myRoom);
                                    isClickedShareBtn = true;
                                    Toast.makeText(getApplicationContext(), "방을 공유하였습니다.", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //다시 색 바꿔주고
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //어차피 현재상태에서 방목록 못본다.
                }else {
                    postDeleteRoom(myRoom);
                    isClickedShareBtn = false;
                    Toast.makeText(getApplicationContext(), "공유를 해제하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void updateData(int position) {
        Log.d("보여라!!!updateData", "" + position);
        recyclerView.scrollToPosition(position);
        playerRecyclerAdapter.setPlayingMusicPostion(position);
        playerRecyclerAdapter.notifyDataSetChanged();

        //window.setStatusBarColor(musics.get(position).getPaletteColor().getDarkVibrantRgb());
        playerLayout.setBackgroundColor(musics.get(position).getPaletteColor().getVibrantRgb());
        //divider 색변경
        if(musics.size() < 5) {
            divider.setBackgroundColor(musics.get(position).getPaletteColor().getDarkVibrantRgb());
        }
        else {
            divider.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            currentTimeTv.setText(convertTime(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(timerTask != null) {
            timerTask.cancel();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isPlayStarted = true;
        doTouchPlayer = false;
        youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), seekBar.getProgress());
/*        timerTask = timerTaskMaker();
        timer.schedule(timerTask, 500, 300);*/
}

    public void postCreateRoom(Room room) {
        nodeRetroClient.postCreateRoom(room, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("onError", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d("onSuccess", "" + code + " " + receivedData.toString());
            }

            @Override
            public void onFailure(int code) {
                Log.d("onFailure", "" + code);
            }
        });
    }

    public void postDeleteRoom(Room room) {
        Toast.makeText(this, "검색 결과!", Toast.LENGTH_SHORT).show();
        nodeRetroClient.postDeleteRoom(room, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("onError", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d("onSuccess", "" + code + " " + receivedData.toString());
            }

            @Override
            public void onFailure(int code) {
                Log.d("onFailure", "" + code);
            }
        });
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            showLog("onPlaying!");
            playBtn.setImageResource(R.drawable.pausebtn);
            seekBar.setMax(youTubePlayer.getDurationMillis());
            durationTv.setText(convertTime(youTubePlayer.getDurationMillis()));
            if(isPlayStarted) {
                timerTask = timerTaskMaker();
                timer.schedule(timerTask, 500, 300);
                isPlayStarted = false;
            } else {
                timerTask = timerTaskMaker();
                timer.schedule(timerTask, 0, 300);
            }
        }

        @Override
        public void onPaused() {
            showLog("onPaused");
        }

        @Override
        public void onStopped() {
            showLog("onStopped!");
            if(timerTask != null) {
                timerTask.cancel();
            }
        }

        @Override
        public void onBuffering(boolean b) {
            showLog("onBuffering!");
        }

        @Override
        public void onSeekTo(int i) {
            showLog("onSeekTo!");
            Log.d("보여라", ""+i);
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
            if(!doTouchPlayer) {
                if(stateFlag == ONE_REPEAT_PLAY) {
                    youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), 0);
                }else {
                    //자동play도 배경 update해줘야 함, 랜덤일때 이럼 안된다.
                    if(playerRecyclerAdapter.getPlayingMusicPostion() < videoIds.size() -1) {
                        updateData(playerRecyclerAdapter.getPlayingMusicPostion() + 1);
                    }else { //마지막곡
                        if(stateFlag == ORDINARY_PLAY) {
                            playBtn.setImageResource(R.drawable.playbtn);
                        }else if(stateFlag == REPEAT_PLAY) {
                            updateData(0);
                            youTubePlayer.loadVideos(videoIds, 0, 0);
                        }
                    }
                }
            }
            doTouchPlayer = false;
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
            if(stateFlag == REPEAT_PLAY && playerRecyclerAdapter.getPlayingMusicPostion() == videoIds.size() -1) {
//                //랜덤일때 제어해줘야함

            }else if(stateFlag == ONE_REPEAT_PLAY) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        PlayerControlProvider.getInstance().unregister(this);
        super.onDestroy();
        if(timer != null) {
            timer.cancel();
        }
        FirebaseMessaging.getInstance().unsubscribeFromTopic(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')));
    }
    /*public class UriToPalette extends AsyncTask<ArrayList<Music>, Void, ArrayList<PaletteColor>> {

        @Override
        protected ArrayList<PaletteColor> doInBackground(ArrayList<Music>... params) {
            ArrayList<PaletteColor> colors = new ArrayList<>();
            ArrayList<Music> items = params[0];
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
            for(int i = 0; i < musics.size(); i++) {
                Log.d("과연", "" + colors.get(i).getVibrantRgb());
                musics.get(i).setPaletteColor(colors.get(i));
            }
        }
    }*/
}
