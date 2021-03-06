package com.example.doublejk.laboum.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.model.Music;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-08.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private Context context;
    private ArrayList<Music> musics;
    private ItemClickListner itemClickListner;
    public SearchRecyclerAdapter(Context context,  ArrayList<Music> musics) {
        this.context = context;
        this.musics = musics;
    }
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_search, parent, false);
        SearchViewHolder holder = new SearchViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Log.d("aaaa", "" + musics.get(position).getImgUrl());
        Glide.with(context).load(musics.get(position).getImgUrl()).into(holder.getSearch_img());
        holder.getSearch_title().setText(musics.get(position).getTitle());

        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListner.onItemClickListener(musics, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    public interface ItemClickListner {
        void onItemClickListener(ArrayList<Music> musics, int position);
    }

    public void setItemClick(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

/*    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView search_img;
        TextView search_title;

        public ViewHolder(View itemView) {
            super(itemView);
            search_img = (ImageView) itemView.findViewById(R.id.search_img);
            search_title = (TextView) itemView.findViewById(R.id.search_title);
        }
    }*/
}
