package com.example.doublejk.laboum;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by doublejk on 2017-08-27.
 */

public class CustomBus extends Bus{
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    CustomBus.super.post(event);
                }
            });
        }
    }
}
