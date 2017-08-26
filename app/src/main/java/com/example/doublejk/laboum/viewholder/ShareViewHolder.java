package com.example.doublejk.laboum.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doublejk.laboum.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by doublejk on 2017-08-24.
 */

public class ShareViewHolder  extends RecyclerView.ViewHolder{
    @BindView(R.id.share_room_title) TextView roomName;
    @BindView(R.id.share_user_name) TextView userName;
    @BindView(R.id.share_usercount) TextView userCount;
    @BindView(R.id.share_room_img) ImageView roomImg;

    public ShareViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getRoomName() {
        return roomName;
    }

    public TextView getUserName() {
        return userName;
    }

    public TextView getUserCount() {
        return userCount;
    }

    public ImageView getRoomImg() {
        return roomImg;
    }
}
