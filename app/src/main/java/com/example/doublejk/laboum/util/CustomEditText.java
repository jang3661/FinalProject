package com.example.doublejk.laboum.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by doublejk on 2017-09-05.
 */

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
    private Context context;
    private KeyImeChange keyImeChangeListener;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        public void onKeyIme(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(keyImeChangeListener != null){
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }

//    @Override
//    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            this.setFocusableInTouchMode(false);
//            Log.d("KeyboardBack", "dd");
//        }
//        return false;
//    }
}
