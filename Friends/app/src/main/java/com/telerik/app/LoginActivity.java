package com.telerik.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveStatus;
import com.telerik.everlive.sdk.core.model.system.AccessToken;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.io.IOException;
import java.util.Arrays;

import model.BaseViewModel;

public class LoginActivity extends Activity implements View.OnClickListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private EditText username;
    private EditText password;
    private ProgressDialog connectionProgressDialog;

    //region Google+
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    //endregion

    //region LiveID
    private LiveAuthClient auth;
    private LiveConnectClient client;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getActionBar().hide();

        connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setMessage("Logging in ...");

        this.username = (EditText) findViewById(R.id.l_userName);
        this.password = (EditText) findViewById(R.id.l_password);

        findViewById(R.id.l_login).setOnClickListener(this);
        findViewById(R.id.l_createNewUser).setOnClickListener(this);
        findViewById(R.id.l_facebookLogin).setOnClickListener(this);
        findViewById(R.id.l_googleLogin).setOnClickListener(this);
        findViewById(R.id.l_liveIDLogin).setOnClickListener(this);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.telerik.app",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        if (savedInstanceState == null) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l_login : {
                this.onLogin(v);
                break;
            }
            case R.id.l_createNewUser :  {
                Intent i = new Intent(this, CreateNewEverliveUserActivity.class);
                startActivity(i);
                break;
            }
            case R.id.l_facebookLogin : {
                this.onFacebookLogin(v);
                break;
            }
            case R.id.l_googleLogin : {
                this.onGoogleLogin(v);
                break;
            }
            case R.id.l_liveIDLogin : {
                this.onLiveIDLogin(v);
                break;
            }
        }
    }

    public void onLiveIDLogin(final View view) {
        String liveAppID = "0000000040110FD6";
        this.auth = new LiveAuthClient(this, liveAppID);
        this.auth.login(this, Arrays.asList(new String[]{"wl.basic"}), new LiveAuthListener() {
            public void onAuthComplete(LiveStatus status, LiveConnectSession session, Object userState) {
                if (status == LiveStatus.CONNECTED) {
//                    resultTextView.setText("Signed in.");
//                    Toast.makeText(view.getContext(), "Signed in with LiveID! and the token is " + session.getAccessToken(), Toast.LENGTH_LONG).show();
                    String liveIdAccessToken = session.getAccessToken();
                    BaseViewModel.EverliveAPP.workWith().authentication().
                            loginWithLiveId(liveIdAccessToken).executeAsync(new RequestResultCallbackAction() {
                        @Override
                        public void invoke(RequestResult requestResult) {
                            if (requestResult.getSuccess()) {
                                AccessToken accessToken = (AccessToken) requestResult.getValue();
                                startListActivity(LoginActivity.this);
                            }
                        }
                    });
                    client = new LiveConnectClient(session);
                } else {
                    Toast.makeText(view.getContext(), "Not Signed in with LiveID!", Toast.LENGTH_LONG).show();
                    client = null;
                }
            }
            public void onAuthError(LiveAuthException exception, Object userState) {
                Toast.makeText(view.getContext(), "Error signing in: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                client = null;
            }
        });
    }

    private void onGoogleLogin(View view) {
        this.mPlusClient = new PlusClient
                    .Builder(this, this, this)
                    .setScopes(Scopes.PLUS_LOGIN + " " + "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email")
                    .build();
        connectionProgressDialog.show();
        this.mPlusClient.connect();
    }

    private void onFacebookLogin(View view) {

        Session activeSession = Session.getActiveSession();

        if (activeSession == null) {
            activeSession = new Session(this);
            Session.setActiveSession(activeSession);
        }

        Session.StatusCallback statusCallback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                if (session.isOpened()) {
                    BaseViewModel.EverliveAPP.workWith().authentication().
                            loginWithFacebook(session.getAccessToken()).
                            executeAsync(new RequestResultCallbackAction<AccessToken>() {
                                @Override
                                public void invoke(RequestResult<AccessToken> requestResult) {
                                    if (requestResult.getSuccess()) {
                                        AccessToken accessToken = requestResult.getValue();
                                        startListActivity(getBaseContext());
                                    }
                                }
                            });
                }
            }
        };

        if (!activeSession.isOpened() && !activeSession.isClosed()){
            activeSession.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        }else{
            activeSession.openActiveSession(this, true, statusCallback);
        }

//        if (Session.getActiveSession().isOpened()) {
//            Toast.makeText(this, "Connected to FB", Toast.LENGTH_LONG).show();
//        } else {
//            Session.openActiveSession(this, true, new Session.StatusCallback() {
//
//                // callback when session changes state
//                @Override
//                public void call(Session session, SessionState state, Exception exception) {
//                    if (session.isOpened()) {
//                        BaseViewModel.EverliveAPP.workWith().authentication().
//                                loginWithFacebook(session.getAccessToken()).
//                                executeAsync(new RequestResultCallbackAction<AccessToken>() {
//                                    @Override
//                                    public void invoke(RequestResult<AccessToken> requestResult) {
//                                        if (requestResult.getSuccess()) {
//                                            AccessToken accessToken = requestResult.getValue();
//                                            startListActivity(getBaseContext());
//                                        }
//                                    }
//                                });
//                    }
//                }
//            });
//        }

//        Session session = Session.getActiveSession();
//
//        Session.StatusCallback statusCallback = new Session.StatusCallback() {
//            @Override
//            public void call(Session session, SessionState sessionState, Exception e) {
//                if (session.isOpened()) {
//                    BaseViewModel.EverliveAPP.workWith().authentication().
//                            loginWithFacebook(session.getAccessToken()).
//                            executeAsync(new RequestResultCallbackAction<AccessToken>() {
//                                @Override
//                                public void invoke(RequestResult<AccessToken> requestResult) {
//                                    if (requestResult.getSuccess()) {
//                                        AccessToken accessToken = requestResult.getValue();
//                                        startListActivity(getBaseContext());
//                                    }
//                                }
//                            });
//                }
//            }
//        };
//
//        if (session == null) {
//            session = new Session(this);
//            Session.setActiveSession(session);
//            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback).setPermissions(Arrays.asList("email")));
//        } else {
//            if (!session.isOpened() && !session.isClosed()) {
//                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//            } else {
//                Session.openActiveSession(this, true, statusCallback);
//            }
//        }

//        Session activeSession = Session.getActiveSession();
//        if (activeSession != null) {
//            activeSession.addCallback(new Session.StatusCallback() {
//                @Override
//                public void call(Session session, SessionState sessionState, Exception e) {
//                    if (session.isOpened()) {
//                    BaseViewModel.EverliveAPP.workWith().authentication().
//                            loginWithFacebook(session.getAccessToken()).
//                            executeAsync(new RequestResultCallbackAction<AccessToken>() {
//                                @Override
//                                public void invoke(RequestResult<AccessToken> requestResult) {
//                                    if (requestResult.getSuccess()) {
//                                        AccessToken accessToken = requestResult.getValue();
//                                        startListActivity(getBaseContext());
//                                    }
//                                }
//                            });
//                }
//                }
//            });
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            Toast.makeText(this, session.getAccessToken(), Toast.LENGTH_LONG).show();
        }
    }

    public void onLogin(final View target) {
        connectionProgressDialog.show();
        BaseViewModel.EverliveAPP.workWith().authentication().
                login(this.username.getText().toString(), this.password.getText().toString()).
                executeAsync(new RequestResultCallbackAction() {
                    @Override
                    public void invoke(RequestResult requestResult) {
                        if (requestResult.getSuccess()) {
                            connectionProgressDialog.dismiss();
                            startListActivity(target.getContext());
                        } else {
                            connectionProgressDialog.dismiss();
                        }
                    }
                });
    }

    private void startListActivity(Context context) {
        Intent i = new Intent(context, ListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Session.getActiveSession() != null) {
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        final String accountName = mPlusClient.getAccountName();
        final Context context = this.getApplicationContext();
        AsyncTask task = new AsyncTask() {
            private String token;

            @Override
            protected Object doInBackground(Object... params) {
                String scope = "oauth2: " + Scopes.PLUS_LOGIN + " " + Scopes.PLUS_PROFILE;
                try {
                    this.token = GoogleAuthUtil.getToken(context, accountName, "oauth2:" + Scopes.PLUS_LOGIN + " " +
                            "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
                    BaseViewModel.EverliveAPP.workWith().authentication().loginWithGoogle(this.token).execute(new RequestResultCallbackAction<AccessToken>() {
                        @Override
                        public void invoke(RequestResult<AccessToken> requestResult) {
                            if (requestResult.getSuccess()) {
                                AccessToken accessToken = requestResult.getValue();
                                startListActivity(context);
//                                connectionProgressDialog.hide();
                            }
                        }
                    });
                } catch (UserRecoverableAuthException e) {
                    // This error is recoverable, so we could fix this
                    // by displaying the intent to the user.
                    Log.e("FriendsApp", e.toString());
                    Intent recover = e.getIntent();
                    startActivityForResult(recover, REQUEST_CODE_RESOLVE_ERR);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute((Void) null);
    }

    @Override
    public void onDisconnected() {
        Log.d("FriendsApp", "disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                mPlusClient.connect();
            }
        }
        mConnectionResult = result;
    }
}
