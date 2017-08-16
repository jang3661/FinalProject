package com.example.doublejk.laboum.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doublejk.laboum.adapter.MyTabRecyclerAdapter;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    private RecyclerView myRecyclerView;
    private MyTabRecyclerAdapter myTabRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Playlist> playlistses;

    public static MyFragment newInstance() {
        MyFragment myFragment = new MyFragment();
        Log.d("프래그먼트", "생성");
/*        Bundle args = new Bundle();
        args.putBoolean("layoutType", layoutType);
        args.putInt("SortType", sortType);
        homeFragment.setArguments(args);*/
        return myFragment;
    }

    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment]
        View view  = inflater.inflate(R.layout.fragment_my, container, false);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(linearLayoutManager);

        playlistses = new ArrayList<>();
        playlistses.add(new Playlist("Basic Playlist", "0"));

        myTabRecyclerAdapter = new MyTabRecyclerAdapter(getActivity(), playlistses);
        myRecyclerView.setAdapter(myTabRecyclerAdapter);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

}
