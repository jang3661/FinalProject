package com.example.doublejk.laboum.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by doublejk on 2017-08-13.
 */

public class GlidePalette {
    private Palette palette;
    private Context context;
    private String uri;
    private ImageView imageView;

    public GlidePalette() {}

    public GlidePalette(Context context, String uri, ImageView imageView) {
        this.context = context;
        this.uri = uri;
        this.imageView = imageView;
    }

    public Palette setPaletteColor() {
        Glide.with(context).load(uri).asBitmap()
                .fitCenter().into(new BitmapImageViewTarget(imageView) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                super.onResourceReady(bitmap, glideAnimation);

                palette = Palette.from(bitmap).generate();
            }
        });
        return palette;
    }

    public Palette setPaletteColor(Context context, String uri, ImageView imageView) {
        Glide.with(context).load(uri).asBitmap()
                .fitCenter().into(new BitmapImageViewTarget(imageView) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                super.onResourceReady(bitmap, glideAnimation);

                palette = Palette.from(bitmap).generate();

            }
        });
        return palette;
    }

    public int getVibrantBackgroundColor(Palette palette) {

        if(palette == null)
            return Color.rgb(48, 48, 48);
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
        if(vibrantSwatch != null)
            return vibrantSwatch.getRgb();
        else
            return Color.rgb(48, 48, 48);

    }

    public int getMutedBackgroundColor(Palette palette) {

        if(palette == null)
            return Color.rgb(48, 48, 48);;
        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
        if(mutedSwatch != null)
            return mutedSwatch.getRgb();
        else
            return Color.rgb(48, 48, 48);

    }

    public int getDarkMutedBackgroundColor(Palette palette) {

        if(palette == null)
            return Color.rgb(48, 48, 48);;
        Palette.Swatch mutedSwatch = palette.getDarkMutedSwatch();
        if(mutedSwatch != null)
            return mutedSwatch.getRgb();
        else
            return Color.rgb(48, 48, 48);

    }

    public int getVibrantBodyTextColor(Palette palette) {
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
        return vibrantSwatch.getBodyTextColor();

    }

    public int getVibrantTitleTextColor(Palette palette) {
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
        return vibrantSwatch.getTitleTextColor();

    }
   /* private void setPalette(Palette palette){

        if(palette==null){
            return;
        }



        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        if(vibrantSwatch!=null){
            viewVibrant.setBackgroundColor(vibrantSwatch.getRgb());
            tvVibrantTitle.setTextColor(vibrantSwatch.getTitleTextColor());
            tvVibrantBody.setTextColor(vibrantSwatch.getBodyTextColor());
        }



        Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
        if(darkVibrantSwatch!=null){
            viewDarkVibrant.setBackgroundColor(darkVibrantSwatch.getRgb());
            tvDarkVibrantTitle.setTextColor(darkVibrantSwatch.getTitleTextColor());
            tvDarkVibrantBody.setTextColor(darkVibrantSwatch.getBodyTextColor());
        }


        Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
        if(lightVibrantSwatch!=null){
            viewLightVibrant.setBackgroundColor(lightVibrantSwatch.getRgb());
            tvLightVibrantTitle.setTextColor(lightVibrantSwatch.getTitleTextColor());
            tvLightVibrantBody.setTextColor(lightVibrantSwatch.getBodyTextColor());
        }





        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
        if(mutedSwatch!=null){
            viewMuted.setBackgroundColor(mutedSwatch.getRgb());
            tvMutedTitle.setTextColor(mutedSwatch.getTitleTextColor());
            tvMutedBody.setTextColor(mutedSwatch.getBodyTextColor());
        }



        Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
        if(darkMutedSwatch!=null){
            viewDarkMuted.setBackgroundColor(darkMutedSwatch.getRgb());
            tvDarkMutedTitle.setTextColor(darkMutedSwatch.getTitleTextColor());
            tvDarkMutedBody.setTextColor(darkMutedSwatch.getBodyTextColor());

        }

        Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
        if(lightMutedSwatch!=null){
            viewLightMuted.setBackgroundColor(lightMutedSwatch.getRgb());
            tvLightMutedTitle.setTextColor(lightMutedSwatch.getTitleTextColor());
            tvLightMutedBody.setTextColor(lightMutedSwatch.getBodyTextColor());
        }



    }*/
}
