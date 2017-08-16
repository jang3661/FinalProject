package com.example.doublejk.laboum.util;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by doublejk on 2017-08-16.
 */

public class ViewAnimation {
    public static void riseUp(View view){
        float startY = view.getBottom();
        float endY = view.getTop();

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", startY, endY)
                .setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }
    public static void dropDwon(View view) {
        float startY = view.getTop();
        float endY = view.getBottom();

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", startY, endY)
                .setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }
}
