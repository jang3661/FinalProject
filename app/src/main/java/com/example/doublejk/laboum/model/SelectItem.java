package com.example.doublejk.laboum.model;

import android.view.View;

/**
 * Created by doublejk on 2017-08-13.
 */

public class SelectItem {
    private View itemView;
    private View layout;

    public SelectItem(View itemView, View layout) {
        this.itemView = itemView;
        this.layout = layout;
    }

    public View getItemView() {
        return itemView;
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
    }

    public View getLayout() {
        return layout;
    }

    public void setLayout(View layout) {
        this.layout = layout;
    }
}
