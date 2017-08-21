package com.example.doublejk.laboum;

/**
 * Created by doublejk on 2017-08-21.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.PaletteColor;
import com.example.doublejk.laboum.model.Playlist;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "laboum";
    private static final int DATABASE_VERSION = 1;
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //DB 생성할때 호출
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE restaurant(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "title TEXT, address TEXT, number TEXT, content TEXT);");
        db.execSQL("CREATE TABLE playlist(num INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, useremail TEXT, username TEXT);");
        db.execSQL("CREATE TABLE music(num INTEGER PRIMARY KEY AUTOINCREMENT, videoid TEXT, playlisttitle TEXT, title TEXT, " +
                "imgurl TEXT, vibrant INT, darkvibrant INT, mute INT, darkmute INT);");
        Log.d("TABLE생성", "ㅇㅇ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(Playlist playlist) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO playlist VALUES(null, '" + playlist.getTitle() + "', '" +
                playlist.getUserEmail() + "', '" + playlist.getUserName() + "');");
    }

    public void insert(LinkedHashMap<Integer, Music> musicMap, String playlisttitle) {
        SQLiteDatabase db = getWritableDatabase();
        Iterator<Integer> keys  = musicMap.keySet().iterator();
        Integer index = 0;
        while(keys.hasNext()) {
            index = keys.next();
            db.execSQL("INSERT INTO music VALUES(null, '" + musicMap.get(index).getVideoId() + "', '" +
                    playlisttitle + "', '" + musicMap.get(index).getTitle() + "', '" + musicMap.get(index).getImgUrl() +
                    "', " + musicMap.get(index).getPaletteColor().getVibrantRgb() + ", " + musicMap.get(index).getPaletteColor().getDarkVibrantRgb() +
                    ", " + musicMap.get(index).getPaletteColor().getMutedRgb() + ", " + musicMap.get(index).getPaletteColor().getDarkMutedRgb() + ");");
        }
    }

    //music 하나 추가용
//    public void insert(Music music, String playlisttitle) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("INSERT INTO restaurant VALUES(null, '" + music.getVideoId() + "', '" +
//                music.getUserEmail() + "', '" + music.getTitle() + "', '" + music.getImgUrl() +
//                "', '" + music.getPaletteColor().getVibrantRgb() + "', '" + music.getPaletteColor().getDarkVibrantRgb() +
//                "', '" + music.getPaletteColor().getMutedRgb() + "', '" + music.getPaletteColor().getDarkMutedRgb() + "');");
//    }

    public ArrayList<Music> selectMusicList(String playlisttitle) {
        ArrayList<Music> musics = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM music WHERE playlisttitle = '" + playlisttitle + "';", null);
        while (cursor.moveToNext()) {
            Music music = new Music(cursor.getString(1), cursor.getString(3), cursor.getString(4));
            music.setPaletteColor(new PaletteColor(cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(8)));
            musics.add(music);
        }
        cursor.close();
        db.close();
        if(musics.size() != 0) {
            Log.d("돠돠", "x");
            return musics;
        }else {
            Log.d("돠돠", "null");
            return null;
        }
    }
    public Playlist selectPlaylist(String title) {
        Playlist playlist = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM playlist WHERE title = '" + title + "';", null);
        while (cursor.moveToNext()) {
            playlist = new Playlist(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }

        cursor.close();
        db.close();

        return playlist;
    }
    public String[] selectPlaylistTitle() {
        ArrayList<String> playlists = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title FROM playlist;", null);
        while (cursor.moveToNext()) {
            playlists.add(cursor.getString(0));
        }
        playlists.add("+ 새 재생목록 추가");
        cursor.close();
        db.close();

        return playlists.toArray(new String[playlists.size()]);
    }
    public void select() {
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM playlist;", null);
    String result = " ";
    while(cursor.moveToNext()) {
        Log.d("cursor",  "" + cursor.getString(0));
        result += cursor.getString(0) + " " + cursor.getString(1) + " " +
                cursor.getString(2) + " " + cursor.getString(3) + "\n";
    }
    Log.d("SELECT", result);
    cursor.close();
    db.close();
}

    public void onDrop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS music");
        db.execSQL("DROP TABLE IF EXISTS playlist");
        onCreate(db);
    }
//
//    public void delete(int id) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM restaurant WHERE id ='" + id + "';");
//        db.close();
//    }
//
//    public void select() {
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM restaurant", null);
//        String result = " ";
//        while(cursor.moveToNext()) {
//            result += cursor.getString(0) + " " + cursor.getString(1) + " " +
//                    cursor.getString(2) + " " + cursor.getString(3) + " " +
//                    cursor.getString(4) + "\n";
//
//        }
//        Log.d("SELECT", result);
//        cursor.close();
//        db.close();
//    }
//
//    public ArrayList<Restaurant> getRestaurants(ArrayList<Restaurant> restaurants) {
//        restaurants = new ArrayList<>();
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM restaurant", null);
//        while(cursor.moveToNext()) {
//            restaurants.add(new Restaurant(cursor.getString(1), cursor.getString(2),
//                    cursor.getString(3), cursor.getString(4)));
//        }
//        db.close();
//        return restaurants;
//    }
}
