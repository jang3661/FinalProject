package com.example.doublejk.laboum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SelectedMusicProvider;
import com.example.doublejk.laboum.viewholder.PopularVideoViewHolder;
import com.example.doublejk.laboum.model.SelectItem;
import com.example.doublejk.laboum.retrofit.SearchItem;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-07.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<PopularVideoViewHolder> {
    private ArrayList<SearchItem> searchItems;
    private Context context;
    private SelectedMusicProvider selectedMusicProvider;
    private ArrayList<SelectItem> selectedItems;

    public HomeRecyclerAdapter(Context context, ArrayList<SearchItem> searchItems, SelectedMusicProvider selectedMusicProvider) {
        this.context = context;
        this.searchItems = searchItems;
        this.selectedMusicProvider = selectedMusicProvider;
        selectedItems = new ArrayList<>();
    }

    @Override
    public PopularVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_popular_music, parent, false);
        PopularVideoViewHolder holder = new PopularVideoViewHolder(v, searchItems, selectedMusicProvider, selectedItems);
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
        Glide.with(context).load(searchItems.get(position).getImgUrl()).fitCenter().into(holder.getPopular_img());
        Glide.with(context).load(R.drawable.optionmenu).fitCenter().into(holder.getPopular_imgBtn());
        holder.getPopular_txt().setText(searchItems.get(position).getTitle());
//        holder.getItemView().setBackgroundColor(Color.argb(0, 255, 0, 0));
//        holder.getLinearLayout().setSelected(false);
       // holder.itemView.setBackgroundColor(Color.rgb(48, 48, 48));

       /* holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_index= position;
                if(!searchItems.get(position).isCliked()) {
                    Log.d("클릭", ""+index);
                    getLinearLayout().setBackgroundColor(Color.rgb(255, 255, 255));
                    selectedMusicProvider.unSelectedList(position);
                    searchItems.get(position).setCliked(true);
                }else {
                    Log.d("no클릭", ""+getAdapterPosition());
                    linearLayout.setBackgroundColor(Color.rgb(48, 48, 48));
                    selectedMusicProvider.selectedList(position, new Music(searchItems.get(position).getTitle(),
                            searchItems.get(position).getImgUrl(), searchItems.get(position).getVideoId()));
                    searchItems.get(position).setCliked(false);
                }

            }
        });*/
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
        return searchItems.size();
    }

    public ArrayList<SearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(ArrayList<SearchItem> searchItems) {
        this.searchItems = searchItems;
    }
}
