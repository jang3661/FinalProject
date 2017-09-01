package com.example.doublejk.laboum.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.sqlite.SQLiteHelper;
import com.example.doublejk.laboum.model.Music;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.User;

import java.util.LinkedHashMap;

public class NewPlaylistDialogFragment extends DialogFragment implements View.OnClickListener{

    private EditText titleEdit;
    private Button cancel, createPlaylist;
    private LinkedHashMap<Integer, Music> musics;
    private SQLiteHelper sqLiteHelper;
    private onAddPlaylistListener callback;

    public NewPlaylistDialogFragment() {

    }
    // TODO: Rename and change types and number of parameters
    public static NewPlaylistDialogFragment newInstance(LinkedHashMap<Integer, Music> musicMap) {
        NewPlaylistDialogFragment fragment = new NewPlaylistDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("musicMap", musicMap);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (onAddPlaylistListener) context;
        }catch (ClassCastException e) {
            throw new RuntimeException(context.toString() + " must implement onAddPlaylistListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            musics = (LinkedHashMap<Integer, Music>) getArguments().getSerializable("musicMap");
        }
    }

    public interface onAddPlaylistListener {
        public void addNewPlaylist(Playlist playlist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_save_dialog, container, false);
        Log.d("해오", ""+getActivity());

        titleEdit = (EditText) v.findViewById(R.id.dialog_editText);
        createPlaylist = (Button) v.findViewById(R.id.dialog_create);
        cancel = (Button) v.findViewById(R.id.dialog_cancel);

        sqLiteHelper = new SQLiteHelper(getActivity());
        titleEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    createPlaylist.setEnabled(false);
                else
                    createPlaylist.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        createPlaylist.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                getDialog().cancel();
                break;
            case R.id.dialog_create:
                //edit text 제한, 담기만 하면된다, 재생할 필요없다.
                String title = titleEdit.getText().toString();
                User user = MainActivity.getUser();
                Playlist playlist = new Playlist(title, user.getEmail(), user.getName());
                playlist.setMusics(musics);
                Log.d("안왔냐", "d" + musics.size());
                MainActivity.getPlaylists().put(title, playlist);
                //NowPlayingPlaylist.title = title;
                sqLiteHelper.insert(playlist);
                sqLiteHelper.insert(musics, title);
                callback.addNewPlaylist(playlist);

                Toast.makeText(getActivity(), title + "이(가) 생성되었습니다.", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                break;
        }
    }




/*    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] items = {"Basic Playlist", "+ 새 재생목록 만들기"};
        return new AlertDialog.Builder(getActivity())
//                .setIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                .setTitle("재생목록")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }*/
}
