package com.example.doublejk.laboum.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.retrofit.SearchItem;
import com.example.doublejk.laboum.util.GlidePalette;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-14.
 */

public class PlaylistViewHolder extends RecyclerView.ViewHolder {
    private ImageView musicImg;
    private TextView musicTitle;
    private ImageButton musicSettingBtn;
    LinearLayout linearLayout;
    private ArrayList<SearchItem> searchItems;
    private View itemView;
    private GlidePalette glidePalette;
    public PlaylistViewHolder(View itemView, ArrayList<SearchItem> searchItems) {
        super(itemView);
        this.searchItems = searchItems;
        this.itemView = itemView;

        musicImg = (ImageView) itemView.findViewById(R.id.popular_img);
        musicTitle = (TextView) itemView.findViewById(R.id.popular_txt);
        musicSettingBtn = (ImageButton) itemView.findViewById(R.id.popularity_imgbtn);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.popular_viewlinear);
        /*glidePalette = new GlidePalette(itemView.getContext(), searchItems.get(getAdapterPosition()).getImgUrl(), musicImg);
        itemView.setBackgroundColor(glidePalette.getVibrantBackgroundColor(glidePalette.setPaletteColor()));*/
    }

    public ImageView getMusicImg() {
        return musicImg;
    }

    public TextView getMusicTitle() {
        return musicTitle;
    }

    public ImageButton getMusicSettingBtn() {
        return musicSettingBtn;
    }

    public View getItemView() {
        return itemView;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }
}
