package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.provider.MediaStore;
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
    private ArrayList<Playlist> playlists;
    private Context context;

    public MyTabRecyclerAdapter(Context context, ArrayList<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @Override
    public MyTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_mytab, parent, false);
        MyTabViewHolder myTabViewHolder = new MyTabViewHolder(view);
        return myTabViewHolder;
    }

    @Override
    public void onBindViewHolder(MyTabViewHolder holder, int position) {
       // Log.d("왔썹", "ㅇ"+playlists.size() + " " + position + " " + playlists.get(position).getTitle() + " " + playlists.get(position).getMusics().size());
        if(playlists.get(position).getMusics().size() != 0) {
            Glide.with(context).load(playlists.get(position).getMusics().get(0).getImgUrl()).fitCenter().into(holder.getPlaylistIcon());
        }else {
            Glide.with(context).load(R.drawable.music_default).fitCenter().into(holder.getPlaylistIcon());
        }
        Glide.with(context).load(R.drawable.optionmenu).fitCenter().into(holder.getSettingBtn());
        holder.getPlaylistTitle().setText(playlists.get(position).getTitle());
        holder.getMusicCount().setText(playlists.get(position).getMusics().size() + " 곡");
    }

    @Override
    public int getItemCount() {
        return playlists.size(); }
}
