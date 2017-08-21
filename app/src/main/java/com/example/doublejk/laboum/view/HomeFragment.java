package com.example.doublejk.laboum.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.doublejk.laboum.NowPlayingPlaylist;
import com.example.doublejk.laboum.SQLiteHelper;
import com.example.doublejk.laboum.adapter.HomeRecyclerAdapter;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SelectedMusicProvider;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.example.doublejk.laboum.retrofit.YoutubeRetroClient;
import com.example.doublejk.laboum.util.UrlToColor;
import com.example.doublejk.laboum.util.ViewAnimation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HomeFragment extends Fragment implements ActionMenuView.OnMenuItemClickListener,
        View.OnClickListener{

    //private OnFragmentInteractionListener mListener;
    private YoutubeRetroClient youtubeRetroClient;
    private RecyclerView recyclerView;
    private LinkedHashMap<Integer, Music> musicMap;
    private Button resetBtn, playBtn, saveMusicBtn;
    private LinearLayout selectingLayout;
    private LinearLayoutManager linearLayoutManager;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private ArrayList<Music> musics;
    private static final String API_KEY = "AIzaSyAH-UUr_Y7XKUg7OUy38J1H6paTdbgOqGo";
    private SQLiteHelper sqLiteHelper;
    private String[] titleList;
    private PlaylistsChangedListener mCallback;
    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
/*        Bundle args = new Bundle();
        args.putBoolean("layoutType", layoutType);
        args.putInt("SortType", sortType);
        homeFragment.setArguments(args);*/
        return homeFragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        youtubeRetroClient = YoutubeRetroClient.getInstance(getContext()).createBaseApi();

        recyclerView = (RecyclerView) view.findViewById(R.id.popular_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        selectingLayout = (LinearLayout) view.findViewById(R.id.selectLinear);
        resetBtn = (Button) view.findViewById(R.id.resetBtn);
        playBtn = (Button) view.findViewById(R.id.playBtn);
        saveMusicBtn = (Button) view.findViewById(R.id.saveMusic);
        resetBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        saveMusicBtn.setOnClickListener(this);
        musicMap = new LinkedHashMap<>();

        getPopularSearch();

        sqLiteHelper = new SQLiteHelper(getActivity());
        titleList = sqLiteHelper.selectPlaylistTitle();
        return view;
    }
    public interface PlaylistsChangedListener {
        public void onChangeMusic(Playlist playlist);
        public void addPlaylist(Playlist playlist);
    }

    SelectedMusicProvider selectedMusicProvider = new SelectedMusicProvider() {
        @Override
        public void selectedList(int pos, Music music) {
            Log.d("selected","호출");
/*            if(selectingLayout.getVisibility() == View.GONE) {public static void
                selectingLayout.setVisibility(View.VISIBLE);
            }*/
            if(musicMap.size() == 0) {
                ViewAnimation.dropDwon(((MainActivity) getActivity()).getTabLayout());
                ViewAnimation.riseUp(selectingLayout);
            }
            musicMap.put(pos, music);
        }

        @Override
        public void unSelectedList(int pos) {
            Log.d("unselected","호출");
            musicMap.remove(pos);
            if(musicMap.size() == 0) {
                ViewAnimation.dropDwon(selectingLayout);
                ViewAnimation.riseUp(((MainActivity) getActivity()).getTabLayout());
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("onSaveInstanceState", "ㅇㅇ");
        outState.putParcelableArrayList("musics", musics);

    }

    void getPopularSearch() {
        Toast.makeText(getActivity(), "검색 결과!", Toast.LENGTH_SHORT).show();
        youtubeRetroClient.getPopularSearch(API_KEY, 30, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                musics = (ArrayList<Music>) receivedData;
                //멀티쓰레드 돌린다.
                new UriToPalette().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, musics);
                //new UriToPalette().execute(musics);
                homeRecyclerAdapter = new HomeRecyclerAdapter(getActivity(), musics, selectedMusicProvider);
                recyclerView.setAdapter(homeRecyclerAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
/*                recyclerAdapter.setItemClick(new SearchRecyclerAdapter.ItemClickListner() {
                    @Override
                    public void onItemClickListener(ArrayList<Music> items, int position) {
                        Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                        intent.putExtra("videoId", items.get(position).getVideoId());
                        startActivity(intent);
                        //타이틀도 보내자
                    }
                });*/
            }
            @Override
            public void onFailure(int code) {
            }
        });
    }

     public class UriToPalette extends AsyncTask<ArrayList<Music>, Void, ArrayList<Music>> {

        @Override
        protected ArrayList<Music> doInBackground(ArrayList<Music>... params) {
            ArrayList<Music> items = params[0];
            for(int i = 0; i < items.size(); i++)
                    UrlToColor.setColor(items.get(i));
            return items;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

         @Override
         protected void onPostExecute(ArrayList<Music> items) {
             super.onPostExecute(items);
             musics = items;
             //homeRecyclerAdapter.notifyDataSetChanged();
             Log.d("ㅇㅇ", "성공");
         }
     }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetBtn:
                //클릭한 배경색 초기화, selected false
                homeRecyclerAdapter.resetMusicList();
                ViewAnimation.dropDwon(selectingLayout);
                ViewAnimation.riseUp(((MainActivity) getActivity()).getTabLayout());
                musicMap.clear();
                break;
            case R.id.playBtn:
                //현재 플레이리스트에 저장, 디비에도 저장
                //MainActivity.getPlaylist(NowPlayingPlaylist.title).setMusics(musicMap);
                Playlist playlist = MainActivity.getPlaylist(NowPlayingPlaylist.title);
                playlist.add(musicMap);
                sqLiteHelper.insert(musicMap, NowPlayingPlaylist.title);
                //Fragment 통신
                mCallback.onChangeMusic(playlist);

//                Gson gson = new Gson();
//                String musicList = gson.toJson(musicMap);
//                Intent intent = new Intent(getActivity(), PlayerActivity.class);
//                intent.putExtra("musicInfo", musicList);
//                startActivity(intent); //parcel 시리얼라이즈

                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putParcelableArrayListExtra("musicInfo", playlist.getMusics());
                startActivity(intent); //parcel 시리얼라이즈


                //액티비티 중지되지 않을까?
                homeRecyclerAdapter.resetMusicList();
                ViewAnimation.dropDwon(selectingLayout);
                ViewAnimation.riseUp(((MainActivity) getActivity()).getTabLayout());
                musicMap.clear();
                break;
            case R.id.saveMusic:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("재생목록")
                        .setItems(titleList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(titleList.length -1 == which) {
                                    showDialog();
                                }else {
                                    //musicMap을 playlist에 저장, 디비에도 저장
                                    //현재 플레이리스트가 아닌 다른곳에 저장하면 디비에서 음악 가져오고 저장해야함
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
        DialogFragment newFragment = NewPlaylistDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "dialog");
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", "ㅇㅇ");
    }

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
