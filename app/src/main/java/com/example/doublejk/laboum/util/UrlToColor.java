package com.example.doublejk.laboum.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.example.doublejk.laboum.model.PaletteColor;
import com.example.doublejk.laboum.retrofit.SearchItem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by doublejk on 2017-08-15.
 */

public class UrlToColor {
    private static PaletteColor paletteColor;
    public static void setColor(SearchItem searchItem) {
        try {
            paletteColor = new PaletteColor();
            URL url = new URL(searchItem.getImgUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Palette palette = Palette.from(bitmap).generate();
            if (palette != null) {
                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                if (vibrantSwatch != null) {
                    paletteColor.setVibrantRgb(vibrantSwatch.getRgb());
                    paletteColor.setVibrantTitle(vibrantSwatch.getTitleTextColor());
                    paletteColor.setVibrantBody(vibrantSwatch.getBodyTextColor());
                }
                Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                if (darkVibrantSwatch != null) {
                    paletteColor.setDarkVibrantRgb(darkVibrantSwatch.getRgb());
                    paletteColor.setDarkVibrantTitle(darkVibrantSwatch.getTitleTextColor());
                    paletteColor.setDarkVibrantBody(darkVibrantSwatch.getBodyTextColor());
                }
                Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                if (mutedSwatch != null) {
                    paletteColor.setMutedRgb(mutedSwatch.getRgb());
                    paletteColor.setMutedTitle(mutedSwatch.getTitleTextColor());
                    paletteColor.setMutedBody(mutedSwatch.getBodyTextColor());
                }
                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                if (darkMutedSwatch != null) {
                    paletteColor.setDarkMutedRgb(darkMutedSwatch.getRgb());
                    paletteColor.setDarkMutedTitle(darkMutedSwatch.getTitleTextColor());
                    paletteColor.setDarkMutedBody(darkMutedSwatch.getBodyTextColor());
                }
            }

    /*        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (palette != null) {
                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                        if (vibrantSwatch != null) {
                            paletteColor.setVibrantRgb(vibrantSwatch.getRgb());
                            paletteColor.setVibrantTitle(vibrantSwatch.getTitleTextColor());
                            paletteColor.setMutedBody(vibrantSwatch.getBodyTextColor());
                        }
                        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                        if (mutedSwatch != null) {
                            paletteColor.setMutedRgb(mutedSwatch.getRgb());
                            paletteColor.setMutedTitle(mutedSwatch.getTitleTextColor());
                            paletteColor.setMutedBody(mutedSwatch.getBodyTextColor());
                        }
                    }
                }
            });*/
            searchItem.setPaletteColor(paletteColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PaletteColor getPaletteColor() {
        return paletteColor;
    }

}
