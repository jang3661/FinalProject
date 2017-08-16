package com.example.doublejk.laboum.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doublejk.laboum.R;

/**
 * Created by doublejk on 2017-08-11.
 */

public class MyTabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private ImageView playlistIcon;
    private TextView playlistTitle;
    private TextView musicCount;
    private ImageButton settingBtn;

    public MyTabViewHolder(View itemView) {
        super(itemView);
        playlistIcon = (ImageView) itemView.findViewById(R.id.mytab_img);
        playlistTitle = (TextView) itemView.findViewById(R.id.mytab_title);
        musicCount = (TextView) itemView.findViewById(R.id.mytab_music_conunt);
        settingBtn = (ImageButton) itemView.findViewById(R.id.mytab_imgbtn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public ImageView getPlaylistIcon() {
        return playlistIcon;
    }

    public void setPlaylistIcon(ImageView playlistIcon) {
        this.playlistIcon = playlistIcon;
    }

    public TextView getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(TextView playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public TextView getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(TextView musicCount) {
        this.musicCount = musicCount;
    }

    public ImageButton getSettingBtn() {
        return settingBtn;
    }

    public void setSettingBtn(ImageButton settingBtn) {
        this.settingBtn = settingBtn;
    }
}
