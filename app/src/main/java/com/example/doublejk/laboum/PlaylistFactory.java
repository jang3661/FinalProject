package com.example.doublejk.laboum;

import android.view.View;

import com.example.doublejk.laboum.model.MyPlaylist;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.YourPlaylist;

/**
 * Created by doublejk on 2017-08-19.
 */

public class PlaylistFactory {
    public static Playlist createPlaylist (String type, String title, String userName) {
        if(type.equals("My")) {
            return new MyPlaylist(title, userName);
        }
        else if(type.equals("Your")) {
            return new YourPlaylist(title, userName);
        }
        else
            return null;
    }
}
