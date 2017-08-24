package com.example.doublejk.laboum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doublejk.laboum.RecyclerItemClickListener;
import com.example.doublejk.laboum.adapter.MyTabRecyclerAdapter;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    private RecyclerView myRecyclerView;
    private MyTabRecyclerAdapter myTabRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Playlist> playlists;

    public static MyFragment newInstance(HashMap<String, Playlist> playlistHashMap) {
        MyFragment myFragment = new MyFragment();
        Log.d("프래그먼트", "생성");
        Bundle args = new Bundle();
        args.putSerializable("playlistHashMap", playlistHashMap);
        myFragment.setArguments(args);
        return myFragment;
    }

    public MyFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playlists = new ArrayList<>();
            HashMap<String, Playlist> playlistHashMap;
            playlistHashMap = (HashMap<String, Playlist>) getArguments().getSerializable("playlistHashMap");
            Iterator<String> keys = playlistHashMap.keySet().iterator();
            while(keys.hasNext()) {
                playlists.add(playlistHashMap.get(keys.next()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment]
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        Log.d("프래그먼트", "onCre");

        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(linearLayoutManager);

        myTabRecyclerAdapter = new MyTabRecyclerAdapter(getActivity(), playlists);
        myRecyclerView.setAdapter(myTabRecyclerAdapter);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        myRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                myRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("playlist", playlists.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    public void addPlaylist(HashMap<String, Playlist> a) {
        Iterator<String> keys = a.keySet().iterator();
        while(keys.hasNext()) {
            Log.d("select", a.get(keys.next()).getTitle());
            this.playlists.add(a.get(keys.next()));
        }
        myTabRecyclerAdapter.notifyDataSetChanged();
    }

    public void addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
        Log.d("안갔냐", "ㅇㅇ" + playlist.getTitle() + playlist.getMusics().size());
        myTabRecyclerAdapter.notifyDataSetChanged();
    }

    public void updatePlaylist(Playlist playlist){
        boolean isPlaylist = false;
        for(int i = 0; i < playlists.size(); i++) {
            if(playlists.get(i).getTitle().equals(playlist.getTitle())) {
                playlists.get(i).setMusics(playlist.getMusics());
                isPlaylist = true;
                break;
            }
        }
        if(!isPlaylist) {
            playlists.add(playlist);
        }
        myTabRecyclerAdapter.notifyDataSetChanged();
    }

    public MyTabRecyclerAdapter getMyTabRecyclerAdapter() {
        return myTabRecyclerAdapter;
    }
}
