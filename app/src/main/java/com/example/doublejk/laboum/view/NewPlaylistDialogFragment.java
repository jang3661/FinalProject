package com.example.doublejk.laboum.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doublejk.laboum.R;

import org.w3c.dom.Text;

public class NewPlaylistDialogFragment extends DialogFragment implements View.OnClickListener{

    private EditText title;
    private Button cancel, createPlaylist;

    public NewPlaylistDialogFragment() {

    }
    // TODO: Rename and change types and number of parameters
    public static NewPlaylistDialogFragment newInstance() {
        NewPlaylistDialogFragment fragment = new NewPlaylistDialogFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_save_dialog, container, false);

        title = (EditText) v.findViewById(R.id.dialog_editText);
        createPlaylist = (Button) v.findViewById(R.id.dialog_create);
        cancel = (Button) v.findViewById(R.id.dialog_cancel);

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    createPlaylist.setEnabled(false);
                else
                    createPlaylist.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
                Toast.makeText(getContext(), "만들기", Toast.LENGTH_SHORT);
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
