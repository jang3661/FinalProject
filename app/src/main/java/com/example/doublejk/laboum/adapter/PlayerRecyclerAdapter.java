package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.util.ColorConverter;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tracks, parent, false);
        playlistViewHolder = new PlaylistViewHolder(view, musics);
        return playlistViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, int position) {
        int color = Color.rgb(69, 90, 100);
        int lightColor = ColorConverter.lighter(color, 0.15f);
        if(musics.get(getPlayingMusicPostion()).getPaletteColor().getDarkVibrantRgb() != 0) {
            color = musics.get(getPlayingMusicPostion()).getPaletteColor().getDarkVibrantRgb();
            lightColor = ColorConverter.lighter(musics.get(getPlayingMusicPostion()).getPaletteColor().getDarkVibrantRgb(), 0.15f);
        }else if(musics.get(getPlayingMusicPostion()).getPaletteColor().getDarkMutedRgb() != 0) {
            color = musics.get(getPlayingMusicPostion()).getPaletteColor().getDarkMutedRgb();
            lightColor = ColorConverter.lighter(musics.get(getPlayingMusicPostion()).getPaletteColor().getDarkMutedRgb(), 0.15f);
        }
        Glide.with(context).load(musics.get(position).getImgUrl()).diskCacheStrategy(DiskCacheStrategy.RESULT).fitCenter().into(holder.getMusicImg());
        Glide.with(context).load(R.drawable.optionmenu).diskCacheStrategy(DiskCacheStrategy.RESULT).fitCenter().into(holder.getMusicSettingBtn());
        holder.getMusicTitle().setText(musics.get(position).getTitle());
        if(position == getPlayingMusicPostion()) {
            holder.getItemView().setBackgroundColor(lightColor);
        } else {
            holder.getItemView().setBackgroundColor(color);
        }
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
