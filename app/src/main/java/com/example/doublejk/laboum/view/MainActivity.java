package com.example.doublejk.laboum.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.adapter.ViewPagerAdpater;
import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private ImageButton imageButton;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = (ImageButton) findViewById(R.id.toolbar_imgBtn);
        imageButton.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Raboum");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //화살표
        //getSupportActionBar().setHomeAsUpIndicator(); // 화살표 이미지 변경

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        //collapsingToolbar.setContentScrimColor(Color.GREEN); //툴바가 사라지는 동안 색상 지정

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        ViewPager viewPager =  (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        ViewPagerAdpater viewPagerAdpater = new ViewPagerAdpater(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdpater);
        tabLayout.setupWithViewPager(viewPager);
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_imgBtn:
                Log.d("이미지버튼", "클릭!");
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
        }
    }
}
