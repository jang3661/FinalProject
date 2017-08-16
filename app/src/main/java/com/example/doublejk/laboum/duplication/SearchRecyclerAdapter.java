package com.example.doublejk.laboum.duplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.duplication.SearchViewHolder;
import com.example.doublejk.laboum.retrofit.SearchItem;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-08.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private Context context;
    private ArrayList<SearchItem> searchItems;
    private ItemClickListner itemClickListner;
    public SearchRecyclerAdapter(Context context,  ArrayList<SearchItem> searchItems) {
        this.context = context;
        this.searchItems = searchItems;
    }
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_search, parent, false);
        SearchViewHolder holder = new SearchViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Log.d("aaaa", "" + searchItems.get(position).getImgUrl());
        Glide.with(context).load(searchItems.get(position).getImgUrl()).into(holder.getSearch_img());
        holder.getSearch_title().setText(searchItems.get(position).getTitle());

        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListner.onItemClickListener(searchItems, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public interface ItemClickListner {
        void onItemClickListener(ArrayList<SearchItem> searchItems, int position);
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
