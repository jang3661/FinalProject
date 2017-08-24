package com.example.doublejk.laboum.util;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by doublejk on 2017-08-16.
 */

public class ViewAnimation {
    public static float tabTopY;
    public static float tabBottomY;
    public static float selectTopY;
    public static float selectBottomY;

    public static void init(View tab, View selectLayout) {
        tabTopY = tab.getTop();
        tabBottomY = tab.getBottom();
        selectTopY = selectLayout.getTop();
        selectBottomY = selectLayout.getBottom();
        Log.d("왕왕", "" + tabTopY + " " + tabBottomY + " " + selectTopY + " " + selectBottomY);
    }

    public static void tabToSelectLayout(View tab, View seletLayout) {
        ObjectAnimator tabAnimator = ObjectAnimator.ofFloat(tab, "y", tabTopY, tabBottomY)
                .setDuration(300);
        tabAnimator.setInterpolator(new AccelerateInterpolator());
        tabAnimator.start();

        ObjectAnimator selectLayoutAnimator = ObjectAnimator.ofFloat(seletLayout, "y", selectBottomY, selectTopY)
                .setDuration(300);
        selectLayoutAnimator.setInterpolator(new AccelerateInterpolator());
        selectLayoutAnimator.start();
    }

    public static void selectLayoutToTab(View seletLayout, View tab) {
        ObjectAnimator selectLayoutAnimator = ObjectAnimator.ofFloat(seletLayout, "y", selectTopY, selectBottomY)
                .setDuration(300);
        selectLayoutAnimator.setInterpolator(new AccelerateInterpolator());
        selectLayoutAnimator.start();

        ObjectAnimator tabAnimator = ObjectAnimator.ofFloat(tab, "y", tabBottomY, tabTopY)
                .setDuration(300);
        tabAnimator.setInterpolator(new AccelerateInterpolator());
        tabAnimator.start();
    }


    public static void riseUp(View view){
        float startY = view.getBottom();
        float endY = view.getTop();

        Log.d("왕왕", "" + startY + " " + endY );

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

    public static void initPos(View view, int pos) {
        Log.d("머냐", ""+pos);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", pos, pos);
        animator.start();
    }
}
