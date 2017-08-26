package com.example.doublejk.laboum.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.RecyclerItemClickListener;
import com.example.doublejk.laboum.adapter.MyTabRecyclerAdapter;
import com.example.doublejk.laboum.adapter.ShareRecyclerAdpter;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.Room;
import com.example.doublejk.laboum.retrofit.NodeRetroClient;
import com.example.doublejk.laboum.retrofit.RetroCallback;

import java.util.ArrayList;

public class ShareFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShareRecyclerAdpter shareRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Playlist> playlists;
    private ArrayList<Room> rooms;
    private NodeRetroClient nodeRetroClient;
    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        Log.d("프래그먼트", "onCre");

        nodeRetroClient = NodeRetroClient.getInstance(getActivity()).createBaseApi();
        playlists = new ArrayList<>();
        rooms = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.share_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        shareRecyclerAdapter = new ShareRecyclerAdpter(getActivity(), rooms);
        recyclerView.setAdapter(shareRecyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                postEnterRoom(rooms.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        return view;
    }

    public void postEnterRoom(Room room) {
        Toast.makeText(getActivity(), "방 입장", Toast.LENGTH_SHORT).show();
        nodeRetroClient.postEnterRoom(room, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("onError", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d("onSuccess", "" + code + " " + receivedData.toString());
                Playlist playlist = (Playlist)receivedData;
                Log.d("우히히", ""+playlist.getMusics().get(0).getPaletteColor().getVibrantRgb());
                //차후 playlist의 현재 위치도 잡아야함
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("playlist", playlist);
                startActivity(intent);
            }

            @Override
            public void onFailure(int code) {
                Log.d("onFailure", "" + code);
            }
        });
    }

    public ShareRecyclerAdpter getShareRecyclerAdapter() {
        return shareRecyclerAdapter;
    }

    public void updateRoomList(ArrayList<Room> rooms) {
        Log.d("베그", "" + rooms.size());
        this.rooms = rooms;
        shareRecyclerAdapter.setRooms(rooms);
        shareRecyclerAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
}
