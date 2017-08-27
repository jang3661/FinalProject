package com.example.doublejk.laboum.util;

import android.graphics.Color;

/**
 * Created by doublejk on 2017-08-28.
 */

public class ColorConverter {
    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }
    public static int darker (int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }

//    public static int setAlpha(int color, float alpha){
//        int alpha = Color.alpha(color);
//        int red = Color.red(color);
//        int green = Color.green(color);
//        int blue = Color.blue(color);
//
//        // Set alpha based on your logic, here I'm making it 25% of it's initial value.
//        alpha *= 0.25;
//
//        return Color.argb(alpha, red, green, blue);
//    }
}
