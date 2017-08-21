package com.example.doublejk.laboum.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-19.
 */

public class User implements Parcelable{
    private  String email;
    private  String name;
    private  String photoUrl;
    private  ArrayList<User> friends;
    private ArrayList<Playlist> playlists;
    //public static String access_token;

    public User() {}
    public User(String email, String name, @Nullable String photoUrl) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.friends = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        photoUrl = in.readString();
        friends = in.createTypedArrayList(User.CREATOR);
        playlists = in.createTypedArrayList(Playlist.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(photoUrl);
        dest.writeTypedList(friends);
        dest.writeTypedList(playlists);
    }
}
