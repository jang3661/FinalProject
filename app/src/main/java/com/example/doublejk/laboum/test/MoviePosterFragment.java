package com.example.doublejk.laboum.test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviePosterFragment extends Fragment {

    @BindView(R.id.iv_thumbnail)
    ImageView thumbnailImageView;

    private String videoPosterThumbnail;
    private String posterTitle;

    /**
     * Override method used to initialize the fragment.
     */
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_poster, container, false);
        ButterKnife.bind(this, view);
        Log.d("확인", "" + getActivity());
        Glide.with(getActivity())
                .load(videoPosterThumbnail)
                .into(thumbnailImageView);
        return view;
    }

    /**
     * Show the poster image in the thumbnailImageView widget.
     */
    public void setPoster(String videoPosterThumbnail) {
        this.videoPosterThumbnail = videoPosterThumbnail;
    }

    /**
     * Store the poster title to show it when the thumbanil view is clicked.
     */
    public void setPosterTitle(String posterTitle) {
        this.posterTitle = posterTitle;
    }

    /**
     * Method triggered when the iv_thumbnail widget is clicked. This method shows a toast with the
     * poster information.
     */
    @OnClick(R.id.iv_thumbnail) void onThubmnailClicked() {
        Toast.makeText(getActivity(), posterTitle, Toast.LENGTH_SHORT).show();
    }
}
