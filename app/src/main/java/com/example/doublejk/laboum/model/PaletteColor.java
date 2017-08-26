package com.example.doublejk.laboum.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by doublejk on 2017-08-14.
 */

public class PaletteColor implements Parcelable{
    @SerializedName("vibrant")
    private int vibrantRgb;
    @SerializedName("darkvibrant")
    private int darkVibrantRgb;
    @SerializedName("mute")
    private int mutedRgb;
    @SerializedName("darkmute")
    private int darkMutedRgb;

    public PaletteColor() {
        this.vibrantRgb = 0;
        this.darkVibrantRgb = 0;
        this.mutedRgb = 0;
        this.darkMutedRgb = 0;
    }

    public PaletteColor(PaletteColor paletteColor) {
        this.vibrantRgb = paletteColor.getVibrantRgb();
        this.darkVibrantRgb = paletteColor.getDarkVibrantRgb();
        this.mutedRgb = paletteColor.getMutedRgb();
        this.darkMutedRgb = paletteColor.getDarkMutedRgb();
    }

/*    public PaletteColor(int vibrantRgb, int vibrantTitle, int vibrantBody) {
        this.vibrantRgb = vibrantRgb;
        this.vibrantTitle = vibrantTitle;
        this.vibrantBody = vibrantBody;
    }*/

    public PaletteColor(int vibrantRgb, int darkVibrantRgb, int mutedRgb, int darkMutedRgb) {
        this.vibrantRgb = vibrantRgb;
        this.darkVibrantRgb = darkVibrantRgb;
        this.mutedRgb = mutedRgb;
        this.darkMutedRgb = darkMutedRgb;
    }

    protected PaletteColor(Parcel in) {
        vibrantRgb = in.readInt();
        darkVibrantRgb = in.readInt();
        mutedRgb = in.readInt();
        darkMutedRgb = in.readInt();
    }

    public static final Creator<PaletteColor> CREATOR = new Creator<PaletteColor>() {
        @Override
        public PaletteColor createFromParcel(Parcel in) {
            return new PaletteColor(in);
        }

        @Override
        public PaletteColor[] newArray(int size) {
            return new PaletteColor[size];
        }
    };

    public int getVibrantRgb() {
        return vibrantRgb;
    }

    public int getMutedRgb() {
        return mutedRgb;
    }

    public void setVibrantRgb(int vibrantRgb) {
        this.vibrantRgb = vibrantRgb;
    }

    public void setMutedRgb(int mutedRgb) {
        this.mutedRgb = mutedRgb;
    }

    public int getDarkMutedRgb() {
        return darkMutedRgb;
    }

    public void setDarkMutedRgb(int darkMutedRgb) {
        this.darkMutedRgb = darkMutedRgb;
    }

    public int getDarkVibrantRgb() {
        return darkVibrantRgb;
    }

    public void setDarkVibrantRgb(int darkVibrantRgb) {
        this.darkVibrantRgb = darkVibrantRgb;
    }

    public static Creator<PaletteColor> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(vibrantRgb);
        dest.writeInt(darkVibrantRgb);
        dest.writeInt(mutedRgb);
        dest.writeInt(darkMutedRgb);
    }
}
