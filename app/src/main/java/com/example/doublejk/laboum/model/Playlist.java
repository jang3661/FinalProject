package com.example.doublejk.laboum.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.doublejk.laboum.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by doublejk on 2017-08-11.
 */

public class Playlist implements Parcelable{
    @SerializedName("title")
    private String title;
    @SerializedName("useremail")
    private String userEmail;
    @SerializedName("username")
    private String userName;
    @SerializedName("token")
    private String token;
    @SerializedName("musics")
    private ArrayList<Music> musics;

    public Playlist(String title, String userEmail, String userName) {
        this.title = title;
        this.userEmail = userEmail;
        this.userName = userName;
        this.token = FirebaseInstanceId.getInstance().getToken();
        this.musics = new ArrayList<>();
    }

    protected Playlist(Parcel in) {
        title = in.readString();
        userEmail = in.readString();
        userName = in.readString();
        token = in.readString();
        musics = in.createTypedArrayList(Music.CREATOR);
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    public void add(Music music) {
        this.musics.add(music);
    }
    public void add(LinkedHashMap<Integer, Music> musicMap) {
        Iterator<Integer> keys  = musicMap.keySet().iterator();
        while(keys.hasNext()) {
            this.musics.add(musicMap.get(keys.next()));
        }
    }

    public void remove() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }

    public void setMusics(LinkedHashMap<Integer, Music> musicMap)
    {
        Iterator<Integer> keys  = musicMap.keySet().iterator();
        while(keys.hasNext()) {
            this.musics.add(musicMap.get(keys.next()));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(userEmail);
        dest.writeString(userName);
        dest.writeString(token);
        dest.writeTypedList(musics);
    }
}
