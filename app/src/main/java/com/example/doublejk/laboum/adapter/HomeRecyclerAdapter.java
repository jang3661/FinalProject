package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SelectedMusicProvider;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.viewholder.PopularVideoViewHolder;
import com.example.doublejk.laboum.model.SelectItem;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-07.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<PopularVideoViewHolder> {
    private ArrayList<Music> musics;
    private Context context;
    private SelectedMusicProvider selectedMusicProvider;
    private ArrayList<SelectItem> selectedItems;

    public HomeRecyclerAdapter(Context context, ArrayList<Music> musics, SelectedMusicProvider selectedMusicProvider) {
        this.context = context;
        this.musics = musics;
        this.selectedMusicProvider = selectedMusicProvider;
        selectedItems = new ArrayList<>();
    }

    @Override
    public PopularVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_popular_music, parent, false);
        PopularVideoViewHolder holder = new PopularVideoViewHolder(v, musics, selectedMusicProvider, selectedItems);
        return holder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(PopularVideoViewHolder holder, final int position) {
        Log.d("onBindVIewHolder", "" + position + "얼마나오냐");
        Glide.with(context).load(musics.get(position).getImgUrl()).asBitmap()
                .fitCenter().into(new BitmapImageViewTarget(holder.getPopular_img()) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                super.onResourceReady(bitmap, glideAnimation);

                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette p) {

                    }
                });
            }
        });
        Glide.with(context).load(R.drawable.optionmenu).fitCenter().into(holder.getPopular_imgBtn());
        holder.getPopular_txt().setText(musics.get(position).getTitle());
    }

    public void resetMusicList() {
        for(int i = 0; i < selectedItems.size(); i++) {
            selectedItems.get(i).getLayout().setSelected(false);
            selectedItems.get(i).getItemView().setBackgroundColor(Color.argb(0, 255, 0, 0));
        }
       // notifyItemChanged(0, getItemCount());
    }
    @Override
    public int getItemCount() {
        return musics.size();
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }
}
