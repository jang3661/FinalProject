package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.Room;
import com.example.doublejk.laboum.viewholder.ShareViewHolder;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-24.
 */

public class ShareRecyclerAdpter extends RecyclerView.Adapter<ShareViewHolder> {
    private Context context;
    private ArrayList<Room> rooms;

    public ShareRecyclerAdpter(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_share, parent, false);
        ShareViewHolder shareViewHolder = new ShareViewHolder(view);
        return shareViewHolder;
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, int position) {
       // Glide.with(context).load(R.drawable.music_placeholder).fitCenter().into(holder.getRoomImg());
//        if(rooms.size() != 0) {
//            if (rooms.get(position).getPlaylist().getMusics().size() == 0) {
//                Glide.with(context).load(R.drawable.music_placeholder).fitCenter().into(holder.getRoomImg());
//            } else {
                        Glide.with(context).load(rooms.get(position).getThumbnail()).fitCenter().into(holder.getRoomImg());
//            }
//        }
        holder.getRoomName().setText(rooms.get(position).getTitle());
        holder.getUserName().setText(rooms.get(position).getUserName());
        holder.getUserCount().setText("" + rooms.get(position).getListenerCount() + "명");
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

//    public void updateRoomList(ArrayList<Room> rooms) {
//        this.rooms = rooms;
//        notifyDataSetChanged();
//        Log.d("왜안와", "" + rooms.size());
//    }
}
