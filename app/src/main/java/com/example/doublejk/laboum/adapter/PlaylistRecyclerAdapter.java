package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.doublejk.laboum.util.GlidePalette;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.viewholder.PlaylistViewHolder;
import com.example.doublejk.laboum.retrofit.SearchItem;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-14.
 */

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {
    private ArrayList<SearchItem> searchItems;
    private Context context;
    private PlaylistViewHolder playlistViewHolder;
    private int playingMusicPostion;

    public PlaylistRecyclerAdapter(Context context,  ArrayList<SearchItem> searchItems) {
        this.context = context;
        this.searchItems = searchItems;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_popular_music, parent, false);
        playlistViewHolder = new PlaylistViewHolder(view, searchItems);
        return playlistViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, int position) {
        Glide.with(context).load(searchItems.get(position).getImgUrl()).fitCenter().into(holder.getMusicImg());
        Glide.with(context).load(R.drawable.optionmenu).fitCenter().into(holder.getMusicSettingBtn());
        holder.getMusicTitle().setText(searchItems.get(position).getTitle());
        if(position == getPlayingMusicPostion())
            holder.getItemView().setBackgroundColor(searchItems.get(getPlayingMusicPostion()).getPaletteColor().getDarkMutedRgb());
        else
            holder.getItemView().setBackgroundColor(searchItems.get(getPlayingMusicPostion()).getPaletteColor().getMutedRgb());
    }

    @Override
    public int getItemCount() { return searchItems.size(); }

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
