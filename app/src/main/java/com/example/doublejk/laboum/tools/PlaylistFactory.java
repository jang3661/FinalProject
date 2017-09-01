package com.example.doublejk.laboum.tools;

import android.view.View;

import com.example.doublejk.laboum.model.MyPlaylist;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.YourPlaylist;

/**
 * Created by doublejk on 2017-08-19.
 */

public class PlaylistFactory {
    public static Playlist createPlaylist (String type, String title, String userEmail, String userName) {
        if(type.equals("My")) {
            return new MyPlaylist(title, userEmail, userName);
        }
        else if(type.equals("Your")) {
            return new YourPlaylist(title, userEmail, userName);
        }
        else
            return null;
    }
}
