package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.viewholder.MyTabViewHolder;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-11.
 */

public class MyTabRecyclerAdapter extends RecyclerView.Adapter<MyTabViewHolder> {
    private ArrayList<Playlist> playlistses;
    private Context context;

    public MyTabRecyclerAdapter(Context context, ArrayList<Playlist> playlistses) {
        this.context = context;
        this.playlistses = playlistses;
        Log.d("아아아", playlistses.get(0).getTitle());
    }

    @Override
    public MyTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_mytab, parent, false);
        MyTabViewHolder myTabViewHolder = new MyTabViewHolder(view);
        return myTabViewHolder;
    }

    @Override
    public void onBindViewHolder(MyTabViewHolder holder, int position) {
        Glide.with(context).load(playlistses.get(position).getPlaylistImg()).fitCenter().into(holder.getPlaylistIcon());
        Glide.with(context).load(R.drawable.optionmenu).fitCenter().into(holder.getSettingBtn());
        holder.getPlaylistTitle().setText(playlistses.get(position).getTitle());
        holder.getMusicCount().setText(playlistses.get(position).getMusicCount());
    }

    @Override
    public int getItemCount() { return playlistses.size(); }
}
