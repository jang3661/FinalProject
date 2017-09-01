package com.example.doublejk.laboum.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doublejk.laboum.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by doublejk on 2017-08-31.
 */

public class GenreViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.genre_img)
    ImageView genreImg;
    @BindView(R.id.genre_txt)
    TextView genreTxt;
    public GenreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getGenreImg() {
        return genreImg;
    }

    public TextView getGenreTxt() {
        return genreTxt;
    }
}
