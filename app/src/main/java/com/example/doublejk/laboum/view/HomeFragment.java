package com.example.doublejk.laboum.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.tools.ServerKey;
import com.example.doublejk.laboum.adapter.HomeRecyclerAdapter;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.example.doublejk.laboum.retrofit.youtube.YoutubeRetroClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    //@BindView(R.id.home_dayrank_layout) LinearLayout dayRankLayout;
    @BindView(R.id.home_dayrank_img) ImageView dayRankImg;
    @BindView(R.id.home_weekbest_img) ImageView weekBestImg;
    @BindView(R.id.home_dayrank_txt) TextView dayRankTv;
    @BindView(R.id.home_weekbest_layout) LinearLayout weekBestLayout;
    private YoutubeRetroClient youtubeRetroClient;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private ReplaceFrgmentListener mCallback;
    private LinearLayout hideLayout;
    private LinearLayout dayRankLayout;
    private ArrayList<Music> musics;
    private ArrayList<String> genreBanner;
    private String banner;
    private int pos;
    public final String KOREA_DAYRANK_ID = "PLFgquLnL59amBMs8p7NyAiHK40Mmn3ect";
    public final String KOREA_WEEKBESE_ID = "PLFgquLnL59akp3Cc6cj1S_4fQxPhdetsO";
//    public final String[] bannerId = {"PLDcnymzs18LWrKzHmzrGH1JzLBqrHi3xQ", "PLFgquLnL59anNXuf1M87FT1O169Qt6-Lp", "PLH6pfBXQXHEC2uDmDy5oi3tHW6X8kZ2Jo",
//    "PLVXq77mXV53-Np39jM456si2PeTrEm9Mj", "PLr8RdoI29cXIlkmTAQDgOuwBhDh3yJDBQ", "PLQog_FHUHAFUDDQPOTeAWSHwzFV1Zz5PZ"};
//    public final String[] bannertxt = {"팝", "최신 뮤직 비디오", "힙합", "클래식", "락", "소울"};

    public HomeFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public interface ReplaceFrgmentListener {
        public void onReplace(ArrayList<Music> musics, String title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);

        youtubeRetroClient = YoutubeRetroClient.getInstance(getActivity()).createBaseApi();

        recyclerView = (RecyclerView) v.findViewById(R.id.home_recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
//                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                pos = position;
//                getPopularTraks(bannerId[position]);
//            }
//            @Override
//            public void onLongItemClick(View view, int position) { }
//        }));


        dayRankLayout = (LinearLayout) v.findViewById(R.id.home_dayrank_layout);
        hideLayout = (LinearLayout) v.findViewById(R.id.home_hide_layout);
        dayRankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPopularTraks(KOREA_DAYRANK_ID);
            }
        });

        genreBanner = new ArrayList<>();
        getHomeBanner(KOREA_DAYRANK_ID, dayRankImg);
        getHomeBanner(KOREA_WEEKBESE_ID, weekBestImg);
//        getHomeBanner(bannerId[0]);
        return v;
    }

    @OnClick(R.id.home_weekbest_layout)
    public void onBannerClick(View v) {
        switch (v.getId()) {
            case R.id.home_weekbest_layout:
                getPopularTraks(KOREA_WEEKBESE_ID);
                break;
        }
    }
    public void getHomeBanner(String id, final ImageView bannerImg) {
        youtubeRetroClient.getPopularTrack(id, ServerKey.Key, 1, new RetroCallback() {
            @Override
            public void onError(Throwable t) { }

            @Override
            public void onSuccess(int code, Object receivedData) {
                banner = (String) receivedData;
                Glide.with(getActivity()).load(banner).fitCenter().into(bannerImg);
            }

            @Override
            public void onFailure(int code) { }
        });
    }
    public void getHomeBanner(String id) {
        youtubeRetroClient.getPopularTrack(id, ServerKey.Key, 1, new RetroCallback() {
            @Override
            public void onError(Throwable t) { }

            @Override
            public void onSuccess(int code, Object receivedData) {
                banner = (String) receivedData;
                genreBanner.add(banner);
//                if(genreBanner.size() == 6) {
//                    homeRecyclerAdapter = new HomeRecyclerAdapter(getContext(), genreBanner, bannertxt);
//                    recyclerView.setAdapter(homeRecyclerAdapter);
//                    recyclerView.setItemAnimator(new DefaultItemAnimator());
//                }else {
//                    getHomeBanner(bannerId[genreBanner.size()]);
//                }
            }

            @Override
            public void onFailure(int code) { }
        });
    }

    public void getPopularTraks(final String id) {
        youtubeRetroClient.getPopularTrack(id, ServerKey.Key, 40, new RetroCallback() {
            @Override
            public void onError(Throwable t) { }

            @Override
            public void onSuccess(int code, Object receivedData) {
                musics = (ArrayList<Music>) receivedData;
                if(id.equals(KOREA_DAYRANK_ID)) {
                    mCallback.onReplace(musics, "일간순위 40 KR");
                }
                else if(id.equals(KOREA_WEEKBESE_ID)) {
                    mCallback.onReplace(musics, "주간베스트 40 KR");
                }
//                }else {
//                    mCallback.onReplace(musics, bannertxt[pos]);
//                }

            }

            @Override
            public void onFailure(int code) { }
        });
    }

    public LinearLayout getDayRankLayout() {
        return dayRankLayout;
    }

    public LinearLayout getHideLayout() {
        return hideLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReplaceFrgmentListener) {
            mCallback = (ReplaceFrgmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ReplaceFrgmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
//    @OnClick(R.id.home_dayrank_img)
//    public void onItemClick(View v) {
//        switch (v.getId()) {
//            case R.id.home_dayrank_layout:
//                Log.d("올까", "");
//                TracksFragment tracksFragment = new TracksFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.home_layout, tracksFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//        }
//    }
}
