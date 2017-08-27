package com.example.doublejk.laboum.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doublejk.laboum.NowPlayingPlaylist;
import com.example.doublejk.laboum.PlaylistSharedPreferences;
import com.example.doublejk.laboum.R;
import com.example.doublejk.laboum.SQLiteHelper;
import com.example.doublejk.laboum.model.MyPlaylist;
import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.User;
import com.example.doublejk.laboum.retrofit.NodeRetroClient;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final String YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private NodeRetroClient nodeRetroClient;
    private SignInButton signInButton;
    private SQLiteHelper sqliteHelper;
    private GoogleSignInResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sqliteHelper = new SQLiteHelper(this);
        //디비삭제용
//        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
//        sqliteHelper.onDrop(db);


        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
//        findViewById(R.id.sign_out_button).setOnClickListener(this);
//        findViewById(R.id.disconnect_button).setOnClickListener(this);

        signInButton.setOnClickListener(this);

        //구글계정연동 Oauth client id 보냄
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestServerAuthCode(getString(R.string.default_web_client_id), false)
                .requestScopes(new Scope(YOUTUBE_SCOPE))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //firebase 개체 가져오기
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //여기서 정보를 받는다.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //구글인증 성공시
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                sqliteHelper = new SQLiteHelper(this);
                //유저 등록
                User user = new User(account.getEmail(), account.getDisplayName(), account.getPhotoUrl().toString());
                if (sqliteHelper.isPlaylistSelect(account.getEmail())) {
                    //플레이리스트, 음악 디비정보 가져온다
                    Log.d("Login", "기존");
                    NowPlayingPlaylist.title = sqliteHelper.nowPlaylitSelect(account.getEmail());
                    Log.d("Select!!", "멍미" + NowPlayingPlaylist.title);
                } else {
                    //첫 방문
                    Log.d("Login", "첫방문");
                    NowPlayingPlaylist.title = "Basic Playlist";
                    sqliteHelper.insert(new Playlist(NowPlayingPlaylist.title, account.getEmail(), account.getDisplayName()));
                    sqliteHelper.insert(account.getEmail(), NowPlayingPlaylist.title);

                }


                //user.getPlaylists().add(new MyPlaylist("asdf", account.getEmail(), account.getDisplayName()));

//                nodeRetroClient = NodeRetroClient.getInstance(getApplicationContext()).createBaseApi();
//                nodeRetroClient.postLogin(user, new RetroCallback() {
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.e("err", "" + t.toString());
//                    }
//                    @Override
//                    public void onSuccess(int code, Object receivedData) {
//                        Log.d("postLogin", "" + receivedData);
//                    }
//                    @Override
//                    public void onFailure(int code) {
//                        Log.e("Fail", "" + code);
//                    }
//                });

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);

            } else {
                // Google Sign In failed, update UI appropriately
                Log.d("aaaa", "Fail");
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("순서보자", "사");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText(result.getSignInAccount().getEmail() + "(으)로 로그인");
        mAuth.signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        // hideProgressDialog();
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

        } else {
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
//        } else if (i == R.id.sign_out_button) {
//
//            signOut();
//        } else if (i == R.id.disconnect_button) {
//            revokeAccess();
//        }
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
