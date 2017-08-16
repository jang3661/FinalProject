package com.example.doublejk.laboum.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.doublejk.laboum.adapter.HomeRecyclerAdapter;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SelectedMusicProvider;
import com.example.doublejk.laboum.model.PaletteColor;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.example.doublejk.laboum.retrofit.RetroClient;
import com.example.doublejk.laboum.retrofit.SearchItem;
import com.example.doublejk.laboum.util.UrlToColor;
import com.example.doublejk.laboum.util.ViewAnimation;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements ActionMenuView.OnMenuItemClickListener,
        View.OnClickListener{

    //private OnFragmentInteractionListener mListener;
    private RetroClient retroClient;
    private RecyclerView recyclerView;
    private LinkedHashMap<Integer, SearchItem> musicMap;
    private Button resetBtn, playBtn;
    private LinearLayout selectingLayout;
    private LinearLayoutManager linearLayoutManager;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private ArrayList<SearchItem> searchItems;
    private Animation animation;
    private static final String API_KEY = "AIzaSyAH-UUr_Y7XKUg7OUy38J1H6paTdbgOqGo";
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
        retroClient = RetroClient.getInstance(getContext()).createBaseApi();

        recyclerView = (RecyclerView) view.findViewById(R.id.popular_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        selectingLayout = (LinearLayout) view.findViewById(R.id.selectLinear);
        resetBtn = (Button) view.findViewById(R.id.resetBtn);
        playBtn = (Button) view.findViewById(R.id.playBtn);
        resetBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        musicMap = new LinkedHashMap<>();

        getPopularSearch();


        return view;
    }

    SelectedMusicProvider selectedMusicProvider = new SelectedMusicProvider() {
        @Override
        public void selectedList(int pos, SearchItem searchItem) {
            Log.d("selected","호출");
/*            if(selectingLayout.getVisibility() == View.GONE) {public static void
                selectingLayout.setVisibility(View.VISIBLE);
            }*/
            if(musicMap.size() == 0) {
                ViewAnimation.dropDwon(((MainActivity) getActivity()).getTabLayout());
                ViewAnimation.riseUp(selectingLayout);
            }
            musicMap.put(pos, searchItem);
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
        outState.putParcelableArrayList("musics", searchItems);

    }

    void getPopularSearch() {
        Toast.makeText(getActivity(), "검색 결과!", Toast.LENGTH_SHORT).show();
        retroClient.getPopularSearch(API_KEY, 30, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                searchItems = (ArrayList<SearchItem>) receivedData;
                //멀티쓰레드 돌린다.
                new UriToPalette().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchItems);
                //new UriToPalette().execute(searchItems);
                homeRecyclerAdapter = new HomeRecyclerAdapter(getActivity(), searchItems, selectedMusicProvider);
                recyclerView.setAdapter(homeRecyclerAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
/*                recyclerAdapter.setItemClick(new SearchRecyclerAdapter.ItemClickListner() {
                    @Override
                    public void onItemClickListener(ArrayList<SearchItem> items, int position) {
                        Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
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

     public class UriToPalette extends AsyncTask<ArrayList<SearchItem>, Void, ArrayList<SearchItem>> {

        @Override
        protected ArrayList<SearchItem> doInBackground(ArrayList<SearchItem>... params) {
            ArrayList<SearchItem> items = params[0];
            for(int i = 0; i < items.size(); i++)
                    UrlToColor.setColor(items.get(i));
            return items;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

         @Override
         protected void onPostExecute(ArrayList<SearchItem> items) {
             super.onPostExecute(items);
             searchItems = items;
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
                musicMap.clear();
                break;
            case R.id.playBtn:
                Gson gson = new Gson();
                String musicList = gson.toJson(musicMap);
                Intent intent = new Intent(getActivity(), PlaylistActivity.class);
                intent.putExtra("musicInfo", musicList);
                startActivity(intent); //parcel 시리얼라이즈
                homeRecyclerAdapter.resetMusicList();
                ViewAnimation.dropDwon(selectingLayout);
                ViewAnimation.riseUp(((MainActivity) getActivity()).getTabLayout());
                musicMap.clear();
                break;
        }
    }
    @Override
    public void onActivityCreated(@Nullable  Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("onActivityCreated", "ㅇㅇ");
        if(savedInstanceState != null)
            searchItems = savedInstanceState.getParcelableArrayList("musics");
    }


/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("onAttach", "ㅇㅇ");
    }

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