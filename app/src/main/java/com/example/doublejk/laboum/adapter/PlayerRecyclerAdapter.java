package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.viewholder.PlaylistViewHolder;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-14.
 */

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {
    private ArrayList<Music> musics;
    private Context context;
    private PlaylistViewHolder playlistViewHolder;
    private int playingMusicPostion;

    public PlayerRecyclerAdapter(Context context, ArrayList<Music> musics) {
        this.context = context;
        this.musics = musics;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_popular_music, parent, false);
        playlistViewHolder = new PlaylistViewHolder(view, musics);
        return playlistViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, int position) {
        Glide.with(context).load(musics.get(position).getImgUrl()).fitCenter().into(holder.getMusicImg());
        Glide.with(context).load(R.drawable.optionmenu).fitCenter().into(holder.getMusicSettingBtn());
        holder.getMusicTitle().setText(musics.get(position).getTitle());
        if(position == getPlayingMusicPostion())
            holder.getItemView().setBackgroundColor(musics.get(getPlayingMusicPostion()).getPaletteColor().getDarkVibrantRgb());
        else
            holder.getItemView().setBackgroundColor(musics.get(getPlayingMusicPostion()).getPaletteColor().getVibrantRgb());
    }

    @Override
    public int getItemCount() { return musics.size(); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getPlayingMusicPostion() {
        return playingMusicPostion;
    }

    public void setPlayingMusicPostion(int playingMusicPostion) {
        this.playingMusicPostion = playingMusicPostion;
    }
}
