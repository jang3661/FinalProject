package com.example.doublejk.laboum.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.doublejk.laboum.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by doublejk on 2017-08-24.
 */

public class ShareViewHolder  extends RecyclerView.ViewHolder{
    @BindView(R.id.room_name)TextView rommName;
    @BindView(R.id.user_name)TextView userName;
    public ShareViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
