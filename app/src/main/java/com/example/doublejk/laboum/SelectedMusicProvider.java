package com.example.doublejk.laboum;

import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.retrofit.SearchItem;

/**
 * Created by doublejk on 2017-08-12.
 */

public interface SelectedMusicProvider {
    public void selectedList(int pos, SearchItem searchItem);
    public void unSelectedList(int pos);
}
