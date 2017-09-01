package com.example.doublejk.laboum.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.doublejk.laboum.tools.NowPlayingPlaylist;
import com.example.doublejk.laboum.sqlite.SQLiteHelper;
import com.example.doublejk.laboum.adapter.TracksRecyclerAdapter;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.tools.SelectedMusicProvider;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.retrofit.youtube.YoutubeRetroClient;
import com.example.doublejk.laboum.util.ViewAnimation;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TracksFragment extends Fragment implements ActionMenuView.OnMenuItemClickListener,
        View.OnClickListener {

    //private OnFragmentInteractionListener mListener;
    private YoutubeRetroClient youtubeRetroClient;
    private RecyclerView recyclerView;
    private LinkedHashMap<Integer, Music> musicMap;
    private Button resetBtn, playBtn, saveMusicBtn;
    private LinearLayout selectingLayout;
    private LinearLayoutManager linearLayoutManager;
    private TracksRecyclerAdapter tracksRecyclerAdapter;
    private ArrayList<Music> musics;
    private static final String API_KEY = "AIzaSyAH-UUr_Y7XKUg7OUy38J1H6paTdbgOqGo";
    private SQLiteHelper sqLiteHelper;
    private String[] titleList;
    private PlaylistsChangedListener mCallback;
    private Boolean getViewSize;
    public static TracksFragment newInstance(ArrayList<Music> musics) {
        TracksFragment tracksFragment = new TracksFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("musics", musics);
        tracksFragment.setArguments(args);
        return tracksFragment;
    }

    public TracksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            musics = getArguments().getParcelableArrayList("musics");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        youtubeRetroClient = YoutubeRetroClient.getInstance(getContext()).createBaseApi();

        recyclerView = (RecyclerView) view.findViewById(R.id.popular_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        tracksRecyclerAdapter = new TracksRecyclerAdapter(getActivity(), musics, selectedMusicProvider);
        recyclerView.setAdapter(tracksRecyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        selectingLayout = (LinearLayout) view.findViewById(R.id.selectLinear);
        resetBtn = (Button) view.findViewById(R.id.resetBtn);
        playBtn = (Button) view.findViewById(R.id.playBtn);
        saveMusicBtn = (Button) view.findViewById(R.id.saveMusic);
        resetBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        saveMusicBtn.setOnClickListener(this);
        musicMap = new LinkedHashMap<>();

        //getPopularSearch();

        sqLiteHelper = new SQLiteHelper(getActivity());
        titleList = sqLiteHelper.selectPlaylistTitle();

        getViewSize = true; //animation
        return view;
    }

    public interface PlaylistsChangedListener {
        public void onChangeMusic(Playlist playlist);
        //public void onBackHome();
    }


    SelectedMusicProvider selectedMusicProvider = new SelectedMusicProvider() {
        @Override
        public void selectedList(int pos, Music music) {
            if(getViewSize) {
                ViewAnimation.init(((MainActivity) getActivity()).getTabLayout(), selectingLayout);
                getViewSize = false;
            }
            Log.d("selected","호출");
            if(musicMap.size() == 0) {
                ViewAnimation.tabToSelectLayout(((MainActivity) getActivity()).getTabLayout(), selectingLayout);
//                ViewAnimation.dropDwon(((MainActivity) getActivity()).getTabLayout());
//                ViewAnimation.riseUp(selectingLayout);
            }
            musicMap.put(pos, music);
        }

        @Override
        public void unSelectedList(int pos) {
            Log.d("unselected","호출");
            musicMap.remove(pos);
            if(musicMap.size() == 0) {
                ViewAnimation.selectLayoutToTab(selectingLayout, ((MainActivity) getActivity()).getTabLayout());
//                ViewAnimation.dropDwon(selectingLayout);
//                ViewAnimation.riseUp(((MainActivity) getActivity()).getTabLayout());
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("onSaveInstanceState", "ㅇㅇ");
        outState.putParcelableArrayList("musics", musics);

    }

    public void setTitleList(String[] titleList) {
        this.titleList = titleList;
    }

    public void initAnimation() {
        tracksRecyclerAdapter.resetMusicList();
        ViewAnimation.selectLayoutToTab(selectingLayout, ((MainActivity) getActivity()).getTabLayout());
        musicMap.clear();
    }

//    public void getPopularSearch() {
//        Toast.makeText(getActivity(), "검색 결과!", Toast.LENGTH_SHORT).show();
//        youtubeRetroClient.getPopularSearch(API_KEY, 30, new RetroCallback() {
//            @Override
//            public void onError(Throwable t) {
//                Log.e("", t.toString());
//            }
//
//            @Override
//            public void onSuccess(int code, Object receivedData) {
//                musics = (ArrayList<Music>) receivedData;
//
//                tracksRecyclerAdapter = new TracksRecyclerAdapter(getActivity(), musics, selectedMusicProvider);
//                recyclerView.setAdapter(tracksRecyclerAdapter);
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
//            }
//            @Override
//            public void onFailure(int code) {
//            }
//        });
//    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetBtn:
                //클릭한 배경색 초기화, selected false
                tracksRecyclerAdapter.resetMusicList();
                ViewAnimation.selectLayoutToTab(selectingLayout, ((MainActivity) getActivity()).getTabLayout());
                musicMap.clear();
                break;
            case R.id.playBtn:
                //현재 플레이리스트에 저장, 디비에도 저장
                //MainActivity.getPlaylist(NowPlayingPlaylist.title).setMusics(musicMap);
                Playlist playlist = MainActivity.getPlaylist(NowPlayingPlaylist.title);
                //처음 클릭한 노래부터 재생하기 위한 변수
                int playingPos = playlist.getMusics().size();
                playlist.add(musicMap);
                sqLiteHelper.insert(musicMap, NowPlayingPlaylist.title);
                //myFragment 갱신, 선택한 곡들 추가
                mCallback.onChangeMusic(playlist);

                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("playlist", playlist);
                intent.putExtra("playingPos", playingPos);
                startActivity(intent); //parcel 시리얼라이즈

                //액티비티 중지되지 않을까?
                initAnimation();
                break;
            case R.id.saveMusic:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    @Override
    public void onActivityCreated(@Nullable  Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("onActivityCreated", "ㅇㅇ");
        if(savedInstanceState != null)
            musics = savedInstanceState.getParcelableArrayList("musics");
    }

    void showDialog() {
        //보낼 정보 있으면 보내자
        Log.d("오오", ""+musicMap.size());
        DialogFragment newFragment = NewPlaylistDialogFragment.newInstance(musicMap);
        newFragment.show(getFragmentManager(), "dialog");
        titleList = sqLiteHelper.selectPlaylistTitle();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (PlaylistsChangedListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString()
                    + " must implement PlaylistChangedListener");
        }
    }

    public LinearLayout getSelectingLayout() {
        return selectingLayout;
    }
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//


    @Override
    public void onStart() {
        super.onStart();
         Log.d("onStart", "ㅇㅇ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "ㅇㅇ");
    }

    @Override
    public void onPause() {
        super.onPause();
/*        ViewAnimation.dropDwon(selectingLayout);
        ViewAnimation.riseUp(((MainActivity) getActivity()).getTabLayout());*/
        Log.d("onPause", "ㅇㅇ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("onStop", "ㅇㅇ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("onDestroyView", "ㅇㅇ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "ㅇㅇ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("onDetach", "ㅇㅇ");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("onViewStateRestored", "ㅇㅇ");
    }

}
