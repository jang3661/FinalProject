package com.example.doublejk.laboum.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by doublejk on 2017-08-14.
 */

public class PaletteColor implements Parcelable{
    private int vibrantRgb;
    private int vibrantTitle;
    private int vibrantBody;
    private int mutedRgb;
    private int mutedTitle;
    private int mutedBody;
    private int darkMutedRgb;
    private int darkMutedTitle;
    private int darkMutedBody;
    private int darkVibrantRgb;
    private int darkVibrantTitle;
    private int darkVibrantBody;


    public PaletteColor() {}

/*    public PaletteColor(int vibrantRgb, int vibrantTitle, int vibrantBody) {
        this.vibrantRgb = vibrantRgb;
        this.vibrantTitle = vibrantTitle;
        this.vibrantBody = vibrantBody;
    }*/
    public PaletteColor(PaletteColor paletteColor) {
        this.vibrantRgb = paletteColor.getVibrantRgb();
        this.vibrantTitle = paletteColor.getVibrantTitle();
        this.vibrantBody = paletteColor.getVibrantBody();
        this.darkVibrantRgb = paletteColor.getDarkVibrantRgb();
        this.darkVibrantTitle = paletteColor.getDarkVibrantTitle();
        this.darkVibrantBody = paletteColor.getDarkVibrantBody();
        this.mutedRgb = paletteColor.getMutedRgb();
        this.mutedTitle = paletteColor.getMutedTitle();
        this.mutedBody = paletteColor.getMutedBody();
        this.darkMutedRgb = paletteColor.getDarkMutedRgb();
        this.darkMutedTitle = paletteColor.getDarkMutedTitle();
        this.darkMutedBody = paletteColor.getDarkMutedBody();
    }
/*    public PaletteColor(int vibrantRgb, int vibrantTitle, int vibrantBody, int mutedRgb, int mutedTitle, int mutedBody) {
        this.vibrantRgb = vibrantRgb;
        this.vibrantTitle = vibrantTitle;
        this.vibrantBody = vibrantBody;
        this.mutedRgb = mutedRgb;
        this.mutedTitle = mutedTitle;
        this.mutedBody = mutedBody;
    }*/

    protected PaletteColor(Parcel in) {
        vibrantRgb = in.readInt();
        vibrantTitle = in.readInt();
        vibrantBody = in.readInt();
        darkVibrantRgb = in.readInt();
        darkVibrantTitle = in.readInt();
        darkVibrantBody = in.readInt();
        mutedRgb = in.readInt();
        mutedTitle = in.readInt();
        mutedBody = in.readInt();
        darkMutedRgb = in.readInt();
        darkMutedTitle = in.readInt();
        darkMutedBody = in.readInt();
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

    public int getVibrantTitle() {
        return vibrantTitle;
    }

    public int getVibrantBody() {
        return vibrantBody;
    }

    public int getMutedRgb() {
        return mutedRgb;
    }

    public int getMutedTitle() {
        return mutedTitle;
    }

    public int getMutedBody() {
        return mutedBody;
    }

    public void setVibrantRgb(int vibrantRgb) {
        this.vibrantRgb = vibrantRgb;
    }

    public void setVibrantTitle(int vibrantTitle) {
        this.vibrantTitle = vibrantTitle;
    }

    public void setVibrantBody(int vibrantBody) {
        this.vibrantBody = vibrantBody;
    }

    public void setMutedRgb(int mutedRgb) {
        this.mutedRgb = mutedRgb;
    }

    public void setMutedTitle(int mutedTitle) {
        this.mutedTitle = mutedTitle;
    }

    public void setMutedBody(int mutedBody) {
        this.mutedBody = mutedBody;
    }

    public int getDarkMutedRgb() {
        return darkMutedRgb;
    }

    public void setDarkMutedRgb(int darkMutedRgb) {
        this.darkMutedRgb = darkMutedRgb;
    }

    public int getDarkMutedTitle() {
        return darkMutedTitle;
    }

    public void setDarkMutedTitle(int darkMutedTitle) {
        this.darkMutedTitle = darkMutedTitle;
    }

    public int getDarkMutedBody() {
        return darkMutedBody;
    }

    public void setDarkMutedBody(int darkMutedBody) {
        this.darkMutedBody = darkMutedBody;
    }

    public int getDarkVibrantRgb() {
        return darkVibrantRgb;
    }

    public void setDarkVibrantRgb(int darkVibrantRgb) {
        this.darkVibrantRgb = darkVibrantRgb;
    }

    public int getDarkVibrantTitle() {
        return darkVibrantTitle;
    }

    public void setDarkVibrantTitle(int darkVibrantTitle) {
        this.darkVibrantTitle = darkVibrantTitle;
    }

    public int getDarkVibrantBody() {
        return darkVibrantBody;
    }

    public void setDarkVibrantBody(int darkVibrantBody) {
        this.darkVibrantBody = darkVibrantBody;
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
        dest.writeInt(vibrantTitle);
        dest.writeInt(vibrantBody);
        dest.writeInt(darkVibrantRgb);
        dest.writeInt(darkVibrantTitle);
        dest.writeInt(darkVibrantBody);
        dest.writeInt(mutedRgb);
        dest.writeInt(mutedTitle);
        dest.writeInt(darkMutedRgb);
        dest.writeInt(darkMutedTitle);
        dest.writeInt(darkMutedBody);
    }
}
