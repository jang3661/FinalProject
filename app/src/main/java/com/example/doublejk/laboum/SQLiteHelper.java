package com.example.doublejk.laboum;

/**
 * Created by doublejk on 2017-08-21.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.PaletteColor;
import com.example.doublejk.laboum.model.Playlist;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
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
        db.execSQL("CREATE TABLE nowplaylist(num INTEGER PRIMARY KEY AUTOINCREMENT, useremail TEXT, title TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(Playlist playlist) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO playlist VALUES(null, '" + playlist.getTitle() + "', '" +
                playlist.getUserEmail() + "', '" + playlist.getUserName() + "');");
    }
    public void insert(String userEmail, String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO nowplaylist VALUES(null, '" + userEmail + "', '" + title + "');");
    }

    public void insert(LinkedHashMap<Integer, Music> musicMap, String playlistTitle) {
        SQLiteDatabase db = getWritableDatabase();
        Iterator<Integer> keys  = musicMap.keySet().iterator();
        Integer index;
        while(keys.hasNext()) {
            index = keys.next();
            db.execSQL("INSERT INTO music VALUES(null, '" + musicMap.get(index).getVideoId() + "', '" +
                    playlistTitle + "', '" + musicMap.get(index).getTitle()
                    + "', '" + musicMap.get(index).getImgUrl() + "', " + musicMap.get(index).getPaletteColor().getVibrantRgb()
                    + ", " + musicMap.get(index).getPaletteColor().getDarkVibrantRgb() + ", " + musicMap.get(index).getPaletteColor().getMutedRgb() + ", "
                    + musicMap.get(index).getPaletteColor().getDarkMutedRgb() + ");");
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

    public void uploadPlaylists(HashMap<String, Playlist> playlists) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM playlist;", null);
        Playlist playlist;
        Music music;
        while (cursor.moveToNext()) {
            playlist = new Playlist(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            Cursor musicCursor = db.rawQuery("SELECT * FROM music WHERE playlisttitle = '" + cursor.getString(1) + "';", null);
            while (musicCursor.moveToNext()) {
                music = new Music(musicCursor.getString(1), musicCursor.getString(3), musicCursor.getString(4));
                music.setPaletteColor(new PaletteColor(musicCursor.getInt(5), musicCursor.getInt(6), musicCursor.getInt(7), musicCursor.getInt(8)));
                playlist.add(music);
            }
            playlists.put(cursor.getString(1), playlist);
        }
        cursor.close();
        db.close();
    }
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
            return musics;
        }else {
            return null;
        }
    }

    public String nowPlaylitSelect(String useremail) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM nowplaylist WHERE useremail = '" + useremail + "';", null);
        String playlistName = "";
        while (cursor.moveToNext()) {
            playlistName = cursor.getString(2);
        }
        cursor.close();
        db.close();
        return playlistName;
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

    public boolean isPlaylistSelect(String userEmail) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM playlist WHERE useremail = '" + userEmail + "' and title = 'Basic Playlist'", null);

        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return false;
        } else {
            cursor.close();
            db.close();
            return true;
        }
    }

    public void select() {
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM playlist;", null);
    String result = " ";
    while(cursor.moveToNext()) {
        result += cursor.getString(0) + " " + cursor.getString(1) + " " +
                cursor.getString(2) + " " + cursor.getString(3) + "\n";
    }
    cursor.close();
    db.close();
}

    public void onDrop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS music");
        db.execSQL("DROP TABLE IF EXISTS playlist");
        db.execSQL("DROP TABLE IF EXISTS nowplaylist");
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
