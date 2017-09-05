package com.example.doublejk.laboum.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doublejk.laboum.tools.PlayerControlProvider;
import com.example.doublejk.laboum.tools.PushEvent;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.tools.RecyclerItemClickListener;
import com.example.doublejk.laboum.adapter.PlayerRecyclerAdapter;
import com.example.doublejk.laboum.firebase.FirebaseMessage;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.Room;
import com.example.doublejk.laboum.retrofit.fcm.FCMRetroClient;
import com.example.doublejk.laboum.retrofit.nodejs.NodeRetroClient;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.example.doublejk.laboum.tools.ServerKey;
import com.example.doublejk.laboum.util.ColorConverter;
import com.example.doublejk.laboum.util.CustomEditText;
import com.example.doublejk.laboum.util.DimensionConverter;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        YouTubePlayer.OnFullscreenListener, SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.player_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.youtube_view) YouTubePlayerView youTubePlayerView;
    @BindView(R.id.player_layout) CoordinatorLayout playerLayout;
    @BindView(R.id.player_divider) LinearLayout divider;
    @BindView(R.id.player_info) LinearLayout infoLayout;
    @BindView(R.id.player_controller) LinearLayout controllerLayout;
    @BindView(R.id.player_playBtn) ImageButton playBtn;
    @BindView(R.id.player_previousBtn) ImageButton previousBtn;
    @BindView(R.id.player_nextBtn) ImageButton nextBtn;
    @BindView(R.id.player_fullscreenBtn) ImageButton fullSreenBtn;
    @BindView(R.id.player_randomBtn) ImageButton randomBtn;
    @BindView(R.id.player_repeatBtn) ImageButton repeatBtn;
    @BindView(R.id.player_chattingbtn) ImageButton chattingBtn;
    @BindView(R.id.player_sharebtn) ImageButton shareBtn;
    @BindView(R.id.player_currentTimeTv) TextView currentTimeTv;
    @BindView(R.id.player_durationTv) TextView durationTv;
    @BindView(R.id.player_playlist_name) TextView playlistName;
    @BindView(R.id.player_user_name) TextView userName;
    @BindView(R.id.player_chatting_input) LinearLayout chattingInputLayout;
    @BindView(R.id.player_chatting_content) TextView chattingContentTv;
    @BindView(R.id.player_chatting_edit) CustomEditText chattingEdit;
    @BindView(R.id.player_bottom_sheet) RelativeLayout bottomSheet;
    private InputMethodManager inputMethodManager;
    private SeekBar seekBar;
    private PlayerRecyclerAdapter playerRecyclerAdapter;
    private ArrayList<Music> musics;
    private YouTubePlayer youTubePlayer;
    private List<String> videoIds;
    private MyPlaybackEventListener myPlaybackEventListener;
    private MyPlayerStateChangeListener myPlayerStateChangeListener;
    private MyPlaylistEventListener myPlaylistEventListener;
    private Window window;
    private TimerTask timerTask;
    private Timer timer;
    private Playlist playlist;
    private boolean isSeekBarReseted, doTouchPlayer, isClickedRandomBtn, isClickedShareBtn, isMaster, isInitChatLyaoutSize;
    private int stateFlag, playingPos, playingCurrentMillis;
    private NodeRetroClient nodeRetroClient;
    private Room myRoom;
    private FCMRetroClient fcmRetroClient;
    //static final String YOUTUBE_KEY = "AIzaSyBwqHpHu9AwlEfiIVKcJ4rsBWOfgP6WmB0";
    private final int ORDINARY_PLAY = 0;
    private final int REPEAT_PLAY = 1;
    private final int ONE_REPEAT_PLAY = 2;
    private boolean shareMode, isShowBottomSheet;
    private boolean commonUser;
    private int chattingContentHeight;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ButterKnife.bind(this);
        PlayerControlProvider.getInstance().register(this);
        receiveMusicList();
        initView();
        backGroundColorInit();

        //recyclerview click listener
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(isMaster || commonUser) {
                    if (isMaster) {
                        postGroupMsg(new FirebaseMessage("/topics/" + playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "itemClick", position, 0));
                    }
                    doTouchPlayer = true;
                    youTubePlayer.loadVideos(videoIds, position, 0);
                    updateData(position);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        //chatting transmit
        chattingEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND:
                        //chattingContentTv.append(chattingEdit.getText() + "\n");
                        appendTextAndScroll(chattingEdit.getText().toString());
                        postGroupMsg(new FirebaseMessage("/topics/" + playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')) + "chatting", "chatting", chattingEdit.getText().toString()));
                        chattingEdit.getText().clear();
                        return true;
                }
                return false;
            }
        });

        //chatting softkeyboard backpress listener
        chattingEdit.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    chattingInputLayout.animate().translationY(0).withLayer();
                    chattingContentTv.animate().translationY(0).withLayer();
                }
            }
        });
        //chatting softkeyboard up listener
        chattingEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    chattingContentTv.animate().translationY(chattingInputLayout.getHeight()).withLayer();
                    chattingInputLayout.animate().translationY(-chattingContentTv.getHeight()).withLayer();
                }
                return false;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
            if(!isInitChatLyaoutSize) {
                chattingContentHeight = playerLayout.getHeight() -
                        (youTubePlayerView.getHeight() + infoLayout.getHeight() + chattingInputLayout.getHeight());
                chattingContentTv.setHeight(chattingContentHeight);
                isInitChatLyaoutSize = true;
            }
    }

    public void initView() {
        seekBar = (SeekBar) findViewById(R.id.player_seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        chattingContentTv.setMovementMethod(new ScrollingMovementMethod());
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        nodeRetroClient = NodeRetroClient.getInstance(this).createBaseApi();
        fcmRetroClient = FCMRetroClient.getInstance(this).createBaseApi();

        myPlaybackEventListener = new MyPlaybackEventListener();
        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
        myPlaylistEventListener = new MyPlaylistEventListener();

        playerRecyclerAdapter = new PlayerRecyclerAdapter(this, musics);
        playerRecyclerAdapter.setPlayingMusicPostion(playingPos); //현재 재생중인 노래 index
        recyclerView.scrollToPosition(playingPos);
        recyclerView.setAdapter(playerRecyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        youTubePlayerView.initialize(ServerKey.Key, this);
    }

    public void backGroundColorInit() {
        int color = changeColor(playingPos);
        int darkColor = ColorConverter.darker(color, 0.8f);
        int lightColor = ColorConverter.lighter(color, 0.15f);

        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(darkColor);
        controllerLayout.setBackgroundColor(darkColor);
        infoLayout.setBackgroundColor(darkColor);
        bottomSheet.setBackgroundColor(lightColor);
        chattingInputLayout.setBackgroundColor(color);
        playerLayout.setBackgroundColor(color);
        if(musics.size() < 5) {
            divider.setBackgroundColor(lightColor);
            divider.setVisibility(View.VISIBLE);
        } else {
            //나중에 다른곳도 추가해줘야한다. 재생목록 추가삭제할때
            recyclerView.setPadding(0, 0, 0, DimensionConverter.dpToPx(86));
        }
        playlistName.setText(playlist.getTitle());
        userName.setText(playlist.getUserName());
    }

    public int changeColor(int position) {
        if(musics.get(position).getPaletteColor().getDarkVibrantRgb() != 0) {
            return musics.get(position).getPaletteColor().getDarkVibrantRgb();
        }else if(musics.get(position).getPaletteColor().getDarkMutedRgb() != 0) {
            return musics.get(position).getPaletteColor().getDarkMutedRgb();
        }else {
            return Color.rgb(69, 90, 100);
        }
    }

    public void updateData(int position) {
        int color = changeColor(position);
        int darkColor = ColorConverter.darker(color, 0.8f);
        int lightColor = ColorConverter.lighter(color, 0.15f);

        recyclerView.scrollToPosition(position);
        playerRecyclerAdapter.setPlayingMusicPostion(position);
        playerRecyclerAdapter.notifyDataSetChanged();

        window.setStatusBarColor(darkColor);
        controllerLayout.setBackgroundColor(darkColor);
        infoLayout.setBackgroundColor(darkColor);
        playerLayout.setBackgroundColor(color);
        bottomSheet.setBackgroundColor(lightColor);
        chattingInputLayout.setBackgroundColor(color);
        //divider 색변경
        if(musics.size() < 5) {
            divider.setBackgroundColor(lightColor);
        }
        else {
            divider.setVisibility(View.GONE);
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

        youTubePlayer.loadVideos(videoIds, playingPos, playingCurrentMillis);
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d("실패이유", youTubeInitializationResult.toString());
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

    public void postExitRoom() {
        nodeRetroClient.postExitRoom(playlist, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d("onSuccess", "" + code);
            }

            @Override
            public void onFailure(int code) {
                Log.d("onFailure", "" + code);
            }
        });
    }
    @Subscribe
    public void playerControl(PushEvent mPushEvent) {
        shareMode = false;
        switch (mPushEvent.getPushData().get("action")) {
            case "next":
                onPlayerClick(nextBtn);
                break;
            case "previous":
                onPlayerClick(previousBtn);
                break;
            case "play":
                onPlayerClick(playBtn);
                break;
            case "fullscreen":
                onPlayerClick(fullSreenBtn);
                break;
            case "normalscreen":
                youTubePlayer.setFullscreen(false);
                break;
            case "random":
                onPlayerClick(randomBtn);
                break;
            case "repeat":
                onPlayerClick(repeatBtn);
                break;
            case "seekbar":
                onStartTrackingTouch(seekBar);
                seekBar.setProgress(Integer.valueOf(mPushEvent.getPushData().get("currentMillis")));
                onStopTrackingTouch(seekBar);
                break;
            case "itemClick":
                int position = Integer.valueOf(mPushEvent.getPushData().get("playingMusicIndex"));
                doTouchPlayer = true;
                youTubePlayer.loadVideos(videoIds, position, 0);
                updateData(position);
                break;
            case "playlistInfo":
                Log.d("버스2", "" + playerRecyclerAdapter.getPlayingMusicPostion() + " " + youTubePlayer.getCurrentTimeMillis());
                postGroupMsg(new FirebaseMessage(mPushEvent.getPushData().get("content"), "reply", playerRecyclerAdapter.getPlayingMusicPostion(), youTubePlayer.getCurrentTimeMillis() + 1000 ));
                break;
            case "chatting":
                chattingContentTv.append(mPushEvent.getPushData().get("content")+"\n");
                break;
            case "end":
                Toast.makeText(this, "공유가 해제되었습니다.", Toast.LENGTH_SHORT).show();
                shareMode = false;
                commonUser = true;
                FirebaseMessaging.getInstance().unsubscribeFromTopic(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')));
                FirebaseMessaging.getInstance().unsubscribeFromTopic(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')) + "chatting");
                break;
        }
        if(!commonUser) {
            shareMode = true;
        }
    }

    public void appendTextAndScroll(String text)
    {
        if(chattingContentTv != null){
            chattingContentTv.append(text + "\n");
            final Layout layout = chattingContentTv.getLayout();
            if(layout != null){
                int scrollDelta = layout.getLineBottom(chattingContentTv.getLineCount() - 1)
                        - chattingContentTv.getScrollY() - chattingContentTv.getHeight();
                if(scrollDelta > 0)
                    chattingContentTv.scrollBy(0, scrollDelta);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        timer = new Timer();
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
        Log.d("onStop", "player");
        super.onStop();
        if(timerTask != null) {
            timerTask.cancel();
            timer.cancel();
        }
        if(!commonUser) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')) + "chatting");
            if (!isMaster) {
                postExitRoom();
            } else {
                postDeleteRoom(myRoom);
                postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "end"));
                isClickedShareBtn = false;
                isMaster = false;
                commonUser = true;
            }
        }
        SharedPreferences sf = getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sf.edit();
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
        Log.d("onBackPressed", "clicked");

        if(isShowBottomSheet) {
            chattingEdit.setFocusable(false);
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

    public void receiveMusicList() {
        shareMode = getIntent().getBooleanExtra("mode", false);
        if(shareMode) {
            commonUser = false;
        }else {
            commonUser = true;
        }
        playingCurrentMillis = getIntent().getIntExtra("currentMillis", 0);
        playingPos = getIntent().getIntExtra("playingPos", 0);
        playlist = getIntent().getParcelableExtra("playlist");
        musics = playlist.getMusics();
        videoIds = new ArrayList<>();
        for(int i = 0; i < musics.size(); i++) {
            videoIds.add(musics.get(i).getVideoId());
        }
    }

    public void showLog(String s) {
        Log.d(s, "보여라");
    }

    @Override
    public void onFullscreen(boolean b) {
        Log.d("onFullscreen", "" + b);
        if(!b) {
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            if(isMaster) {
                postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "normalscreen"));
            }
        }
    }

    @OnClick( R.id.player_chattingbtn )
    public void onChattingClick(View v) {
        if(!isShowBottomSheet) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            isShowBottomSheet = true;
        }else {
            chattingContentTv.setVisibility(View.VISIBLE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            chattingInputLayout.animate().translationY(0).withLayer();
            chattingContentTv.animate().translationY(0).withLayer();
            isShowBottomSheet = false;
        }
    }
    @OnClick({R.id.player_playBtn, R.id.player_nextBtn, R.id.player_previousBtn,
            R.id.player_fullscreenBtn, R.id.player_repeatBtn, R.id.player_randomBtn, R.id.player_sharebtn})
    public void onPlayerClick(View v) {
        Log.d("shareMode", "" + shareMode);
        if(!shareMode) {
            if(!isMaster && !commonUser) {
                shareMode = true;
            }
            switch (v.getId()) {
                case R.id.player_playBtn:
                    if (isMaster) {
                        postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "play"));
                    }
                    if (youTubePlayer.isPlaying()) {
                        playBtn.setImageResource(R.drawable.playbtn);
                        youTubePlayer.pause();
                    } else {
                        playBtn.setImageResource(R.drawable.pausebtn);
                        youTubePlayer.play();
                    }
                    break;
                case R.id.player_nextBtn:
                    if (isMaster) {
                        postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "next"));
                    }
                    doTouchPlayer = true;
                    if (playerRecyclerAdapter.getPlayingMusicPostion() < videoIds.size() - 1) {
                        updateData(playerRecyclerAdapter.getPlayingMusicPostion() + 1);
                        youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), 0);
                    } else {
                        updateData(0);
                        youTubePlayer.loadVideos(videoIds, 0, 0);
                    }
                    break;
                case R.id.player_previousBtn:
                    if (isMaster) {
                        postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "previous"));
                    }
                    doTouchPlayer = true;
                    if (playerRecyclerAdapter.getPlayingMusicPostion() > 0) {
                        updateData(playerRecyclerAdapter.getPlayingMusicPostion() - 1);
                        youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), 0);
                    } else {
                        updateData(videoIds.size() - 1);
                        youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), 0);
                    }
                    break;
                case R.id.player_fullscreenBtn:
                    if (isMaster) {
                        postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "fullscreen"));
                    }
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.setFullscreen(true);
                    break;
                case R.id.player_repeatBtn:
                    if (isMaster) {
                        postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "repeat"));
                    }
                    if (stateFlag == ORDINARY_PLAY) {
                        stateFlag = REPEAT_PLAY;
                        repeatBtn.setImageResource(R.drawable.repeatbtn);
                    } else if (stateFlag == REPEAT_PLAY) {
                        stateFlag = ONE_REPEAT_PLAY;
                        repeatBtn.setImageResource(R.drawable.onerepeatbtn);
                    } else {
                        stateFlag = ORDINARY_PLAY;
                        repeatBtn.setImageResource(R.drawable.unrepeatbtn);
                    }
                    break;
                case R.id.player_randomBtn:
                    if (isMaster) {
                        postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "random"));
                    }
                    if (isClickedRandomBtn) {
                        randomBtn.setImageResource(R.drawable.unrandombtn);
                        isClickedRandomBtn = false;
                    } else {
                        randomBtn.setImageResource(R.drawable.randombtn);
                        isClickedRandomBtn = true;
                    }
                    break;
                case R.id.player_sharebtn:
                    if (!isClickedShareBtn) {
                        final EditText roomName = new EditText(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        roomName.setLayoutParams(lp);

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("재생목록을 공유하시겠습니까?")
                                // Add action buttons
                                .setView(roomName)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        myRoom = new Room(roomName.getText().toString(), playlist.getUserEmail(),
                                                playlist.getUserName(), playlist.getTitle(), playlist.getMusics().get(0).getImgUrl());
                                        Log.d("이미지", "" + playlist.getMusics().get(0).getImgUrl());
                                        myRoom.setPlaylist(playlist);
                                        postCreateRoom(myRoom);
                                        FirebaseMessaging.getInstance().subscribeToTopic(myRoom.getUserEmail().substring(0, myRoom.getUserEmail().indexOf('@')) + "chatting");
                                        isClickedShareBtn = true;
                                        isMaster = true;
                                        Toast.makeText(getApplicationContext(), "방을 공유하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Log.d("취소", "ㅊㅊ");
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //어차피 현재상태에서 방목록 못본다.
                    } else {
                        postDeleteRoom(myRoom);
                        postGroupMsg(new FirebaseMessage(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "end"));
                        isClickedShareBtn = false;
                        commonUser = true;
                        isMaster = false;
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(myRoom.getUserEmail().substring(0, myRoom.getUserEmail().indexOf('@')) + "chatting");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(myRoom.getUserEmail().substring(0, myRoom.getUserEmail().indexOf('@')) + "chatting");
                        Toast.makeText(getApplicationContext(), "공유를 해제하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            currentTimeTv.setText(convertTime(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (isMaster) {
            postGroupMsg(new FirebaseMessage("/topics/" + playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')), "seekbar", 0, seekBar.getProgress()));
        }
        youTubePlayer.loadVideos(videoIds, playerRecyclerAdapter.getPlayingMusicPostion(), seekBar.getProgress());

        Log.d("타임", "onStop");
        isSeekBarReseted = true;
        doTouchPlayer = false;
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
            if(isSeekBarReseted) {
                timerTask = timerTaskMaker();
                timer.schedule(timerTask, 500, 300);
                isSeekBarReseted = false;
            } else {
                timerTask = timerTaskMaker();
                if(timer == null) {
                    Log.d("timer", "null");
                }else {
                    Log.d("timer", "notnull");

                }
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
                //랜덤일때 제어해줘야함
            }else if(stateFlag == ONE_REPEAT_PLAY) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        PlayerControlProvider.getInstance().unregister(this);
        super.onDestroy();
        youTubePlayer.release();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(playlist.getUserEmail().substring(0, playlist.getUserEmail().indexOf('@')));
    }
}
