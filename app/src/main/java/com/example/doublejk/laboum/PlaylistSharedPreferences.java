package com.example.doublejk.laboum;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by doublejk on 2017-08-20.
 */

public class PlaylistSharedPreferences {

    public static void savePlayingPlaylist(Context context, String title) {
        SharedPreferences sp = context.getSharedPreferences("playlist", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("title", title);
        editor.commit();
    }
    public static String loadPlayingPlaylist(Context context) {
        SharedPreferences sp = context.getSharedPreferences("playlist", 0);
        return sp.getString("title", "");
    }

}
