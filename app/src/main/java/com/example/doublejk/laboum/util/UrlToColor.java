package com.example.doublejk.laboum.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;

import com.example.doublejk.laboum.model.PaletteColor;
import com.example.doublejk.laboum.model.Music;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by doublejk on 2017-08-15.
 */

public class UrlToColor {
    private static PaletteColor paletteColor;
    public static void setColor(Music music) {
        try {
            paletteColor = new PaletteColor();
            URL url = new URL(music.getImgUrl());
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
                }
//                Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
//                if (lightVibrantSwatch != null) {
//                    paletteColor.setLightVibrantRgb(lightVibrantSwatch.getRgb());
//                }
                Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                if (darkVibrantSwatch != null) {
                    paletteColor.setDarkVibrantRgb(darkVibrantSwatch.getRgb());
                }
                Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                if (mutedSwatch != null) {
                    paletteColor.setMutedRgb(mutedSwatch.getRgb());
                }
//                Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
//                if (lightMutedSwatch != null) {
//                    paletteColor.setLightMutedRgb(lightMutedSwatch.getRgb());
//                }
                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                if (darkMutedSwatch != null) {
                    paletteColor.setDarkMutedRgb(darkMutedSwatch.getRgb());
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
            music.setPaletteColor(paletteColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PaletteColor getPaletteColor() {
        return paletteColor;
    }

}
