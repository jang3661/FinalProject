package com.example.doublejk.laboum.util;

import android.content.res.Resources;

/**
 * Created by doublejk on 2017-08-17.
 */

public class DimensionConverter {
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
