package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.viewholder.ShareViewHolder;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-24.
 */

public class ShareRecyclerAdpter extends RecyclerView.Adapter<ShareViewHolder> {
    private Context context;
    private ArrayList<Playlist> playlists;

    public ShareRecyclerAdpter(Context context, ArrayList<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_share, parent, false);
        ShareViewHolder shareViewHolder = new ShareViewHolder(view);
        return shareViewHolder;
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
