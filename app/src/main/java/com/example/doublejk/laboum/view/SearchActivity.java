package com.example.doublejk.laboum.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SelectedMusicProvider;
import com.example.doublejk.laboum.adapter.HomeRecyclerAdapter;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.example.doublejk.laboum.retrofit.RetroClient;
import com.example.doublejk.laboum.retrofit.SearchItem;
import com.example.doublejk.laboum.util.UrlToColor;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
   // private SearchRecyclerAdapter recyclerAdapter;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private LinearLayoutManager linearLayout;
    private Toolbar toolbar;
    private RetroClient retroClient;
    private EditText searchEdit;
    private InputMethodManager imm;
    private HashMap<Integer, SearchItem> musicMap;
    private LinearLayout selectingLayout;
    private Button resetBtn, playBtn;
    private  ArrayList<SearchItem> searchItems;
    private static final String API_KEY = "AIzaSyAH-UUr_Y7XKUg7OUy38J1H6paTdbgOqGo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //키보드 내리기
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Raboum");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //화살표

        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        selectingLayout = (LinearLayout) findViewById(R.id.search_selectLinear);
        resetBtn = (Button) findViewById(R.id.search_resetBtn);
        playBtn = (Button) findViewById(R.id.search_playBtn);
        resetBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        musicMap = new HashMap<Integer, SearchItem>();

        retroClient = RetroClient.getInstance(this).createBaseApi();

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

    SelectedMusicProvider selectedMusicProvider = new SelectedMusicProvider() {
        @Override
        public void selectedList(int pos, SearchItem searchItem) {
            Log.d("selected","호출");
            if(selectingLayout.getVisibility() == View.GONE)
                selectingLayout.setVisibility(View.VISIBLE);
            musicMap.put(pos, searchItem);
        }

        @Override
        public void unSelectedList(int pos) {
            Log.d("unselected","호출");
            musicMap.remove(pos);
            if(musicMap.size() == 0)
                selectingLayout.setVisibility(View.GONE);
        }
    };

    void showSearchList() {
        Toast.makeText(this, "GET 1 Clicked", Toast.LENGTH_SHORT).show();
        retroClient.getSearch(searchEdit.getText().toString(),API_KEY ,20, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("", t.toString());
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                searchItems = (ArrayList<SearchItem>) receivedData;
                new UriToPalette().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchItems);
                homeRecyclerAdapter = new HomeRecyclerAdapter(getApplicationContext(), searchItems, selectedMusicProvider);
                recyclerView.setAdapter(homeRecyclerAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

 /*               recyclerAdapter = new SearchRecyclerAdapter(getApplicationContext(), searchItems);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerAdapter.setItemClick(new SearchRecyclerAdapter.ItemClickListner() {
                    @Override
                    public void onItemClickListener(ArrayList<SearchItem> items, int position) {
                        Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
                        intent.putExtra("videoId", items.get(position).getVideoId());
                        startActivity(intent);
                        //타이틀도 보내자
                    }
                });*/

/*                codeResultTextView.setText(String.valueOf(code));
                idResultTextView.setText(searchItems.get(0).getTitle());*/
                //titleResultTextView.setText(searchData);
/*                useridResultTextView.setText(String.valueOf(data.userId));
                bodyResultTextView.setText(data.body);*/
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
            homeRecyclerAdapter.notifyDataSetChanged();
            Log.d("ㅇㅇ", "성공");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_resetBtn:
                homeRecyclerAdapter.resetMusicList();
                musicMap.clear();
                break;
            case R.id.search_playBtn:
                Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
                intent.putExtra("musicInfo", musicMap);
                startActivity(intent); //parcel 시리얼라이즈
                homeRecyclerAdapter.resetMusicList();
                selectingLayout.setVisibility(View.GONE);
                musicMap.clear();
                break;
        }
    }
}
