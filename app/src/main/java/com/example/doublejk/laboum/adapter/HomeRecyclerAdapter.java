package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.viewholder.GenreViewHolder;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-31.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<GenreViewHolder> {
    private ArrayList<String> bannerImg;
    private String[] bannerTxt;
    private Context context;

    public HomeRecyclerAdapter(Context context, ArrayList<String> bannerImg, String[] bannerTxt) {
        this.context = context;
        this.bannerImg = bannerImg;
        this.bannerTxt = bannerTxt;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_genre, parent, false);
        return new GenreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        Glide.with(context).load(bannerImg.get(position)).fitCenter().into(holder.getGenreImg());
        holder.getGenreTxt().setText(bannerTxt[position]);
    }

    @Override
    public int getItemCount() {
        return bannerImg.size();
    }
}
