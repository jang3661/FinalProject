package com.example.doublejk.laboum.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SelectedMusicProvider;
import com.example.doublejk.laboum.model.SelectItem;
import com.example.doublejk.laboum.retrofit.SearchItem;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-10.
 */

public class PopularVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener{
    private ImageView popular_img;
    private TextView popular_txt;
    private ImageButton popular_imgBtn;
    private LinearLayout linearLayout;
    private LinearLayout viewLinearLayout;
    private Context context;
    private ArrayList<SearchItem> searchItems;
    private SelectedMusicProvider selectedMusicProvider;
    private ArrayList<SelectItem> selectedItems;
    private View itemView;

    public PopularVideoViewHolder(View itemView, ArrayList<SearchItem> searchItems, SelectedMusicProvider selectedMusicProvider, ArrayList<SelectItem> selectedItems) {
        super(itemView);
        this.context = itemView.getContext();
        this.searchItems = searchItems;
        this.selectedMusicProvider = selectedMusicProvider;
        this.selectedItems = selectedItems;
        this.itemView = itemView;

        popular_img = (ImageView) itemView.findViewById(R.id.popular_img);
        popular_txt = (TextView) itemView.findViewById(R.id.popular_txt);
        popular_imgBtn = (ImageButton) itemView.findViewById(R.id.popularity_imgbtn);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.popular_linear);
        viewLinearLayout = (LinearLayout) itemView.findViewById(R.id.popular_viewlinear);

        popular_imgBtn.setOnClickListener(this);
        linearLayout.setOnClickListener(this);


/*        linearLayout.setSelected(false);
        itemView.setBackgroundColor(Color.argb(0, 255, 0, 0));*/
        itemView.setOnCreateContextMenuListener(this);
    }

    public View getItemView() {
        return itemView;
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
    }

    public LinearLayout getViewLinearLayout() {
        return viewLinearLayout;
    }

    public void setViewLinearLayout(LinearLayout viewLinearLayout) {
        this.viewLinearLayout = viewLinearLayout;
    }

    public ImageView getPopular_img() {
        return popular_img;
    }

    public void setPopular_img(ImageView popular_img) {
        this.popular_img = popular_img;
    }

    public TextView getPopular_txt() {
        return popular_txt;
    }

    public void setPopular_txt(TextView popular_txt) {
        this.popular_txt = popular_txt;
    }

    public ImageButton getPopular_imgBtn() {
        return popular_imgBtn;
    }

    public void setPopular_imgBtn(ImageButton popular_imgBtn) {
        this.popular_imgBtn = popular_imgBtn;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        switch (v.getId()) {
            case R.id.popular_linear:{
                if (!v.isSelected()) {
                    Log.d("클릭", "" + getAdapterPosition());
                    itemView.setBackgroundColor(Color.rgb(238, 238, 238));
                    selectedMusicProvider.selectedList(position, searchItems.get(position));
                    selectedItems.add(new SelectItem(itemView, v));
                    v.setSelected(true);
                } else {
                    Log.d("no클릭", "" + getAdapterPosition());
                    //itemView.setBackgroundColor(Color.rgb(48, 48, 48));
                    itemView.setBackgroundColor(Color.argb(0, 255, 0, 0));
                    selectedMusicProvider.unSelectedList(position);
                    v.setSelected(false);
                }
            }
            break;
            //설정 팝업메뉴
            case R.id.popularity_imgbtn: {
                Log.d("이미지버튼", "이미지");
                PopupMenu popup = new PopupMenu(context, v);
                popup.inflate(R.menu.menu_setting);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_setting_Listening:
                                break;
                            case R.id.menu_setting_add:
                                break;
                        }

                        return false;
                    }
                });
                popup.show();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(menu.NONE, 0, menu.NONE, "ㅇㅇ");
    }
}
