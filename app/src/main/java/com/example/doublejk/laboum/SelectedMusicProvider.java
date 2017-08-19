package com.example.doublejk.laboum;

import com.example.doublejk.laboum.model.Music;

/**
 * Created by doublejk on 2017-08-12.
 */

public interface SelectedMusicProvider {
    public void selectedList(int pos, Music music);
    public void unSelectedList(int pos);
}
