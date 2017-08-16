package com.example.doublejk.laboum.duplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doublejk.laboum.R;

/**
 * Created by doublejk on 2017-08-10.
 */

public class SearchViewHolder extends RecyclerView.ViewHolder {
    private ImageView search_img;
    private TextView search_title;

    public SearchViewHolder(View itemView) {
        super(itemView);
        search_img = (ImageView) itemView.findViewById(R.id.search_img);
        search_title = (TextView) itemView.findViewById(R.id.search_title);
    }

    public ImageView getSearch_img() {
        return search_img;
    }

    public void setSearch_img(ImageView search_img) {
        this.search_img = search_img;
    }

    public TextView getSearch_title() {
        return search_title;
    }

    public void setSearch_title(TextView search_title) {
        this.search_title = search_title;
    }
}