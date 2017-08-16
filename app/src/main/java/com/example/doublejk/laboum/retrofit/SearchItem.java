package com.example.doublejk.laboum.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.doublejk.laboum.model.PaletteColor;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-08.
 */

public class SearchItem implements Parcelable{
    private String videoId;
    private String title;
    private String imgUrl;
    boolean isSelected;
    private PaletteColor paletteColor;

    public SearchItem() {}

    public SearchItem(String videoId, String title, String imgUrl) {
        this.videoId = videoId;
        this.title = title;
        this.imgUrl = imgUrl;
        this.isSelected = false;
    }

    protected SearchItem(Parcel in) {
        videoId = in.readString();
        title = in.readString();
        imgUrl = in.readString();
        isSelected = in.readByte() != 0;
        paletteColor = in.readParcelable(PaletteColor.class.getClassLoader());
    }

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };

    public void setSelected(boolean selected) { isSelected = selected; }

    public boolean isSelected() {
        return isSelected;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public PaletteColor getPaletteColor() {
        return paletteColor;
    }

    public void setPaletteColor(PaletteColor paletteColor) {

        this.paletteColor = new PaletteColor(paletteColor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
        dest.writeString(title);
        dest.writeString(imgUrl);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeParcelable(paletteColor , flags);
    }

}
