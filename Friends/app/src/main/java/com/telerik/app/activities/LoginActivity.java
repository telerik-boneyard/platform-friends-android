package com.telerik.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.google.android.gms.auth.GoogleAuthUtil;
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
import com.telerik.app.utils.AccountChooser;
import com.telerik.app.R;
import com.telerik.app.tasks.GoogleLoginTask;
import com.telerik.app.tasks.LoginRequestResultCallbackAction;
import com.telerik.everlive.sdk.core.EverliveApp;

import java.net.URISyntaxException;
import java.util.Arrays;

import eqatec.analytics.monitor.AnalyticsMonitorFactory;
import eqatec.analytics.monitor.IAnalyticsMonitor;
import eqatec.analytics.monitor.Version;
import com.telerik.app.model.BaseViewModel;

public class LoginActivity extends Activity implements View.OnClickListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private EditText username;
    private EditText password;
    private ProgressDialog connectionProgressDialog;

    //region Google+
    public static final int REQUEST_CODE_RESOLVE_ERR = 9000;
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

        this.checkAppSettings(getIntent().getAction() != null);

        connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setMessage("Logging in ...");

        this.username = (EditText) findViewById(R.id.l_userName);
        this.password = (EditText) findViewById(R.id.l_password);

        findViewById(R.id.l_login).setOnClickListener(this);
        findViewById(R.id.l_createNewUser).setOnClickListener(this);
        findViewById(R.id.l_googleLogin).setOnClickListener(this);
        findViewById(R.id.l_ADFSLogin).setOnClickListener(this);

    }

    private IAnalyticsMonitor initializeAnalyticsService(String analytics_app_id) {
        Version version = new Version("1.2.3");
        IAnalyticsMonitor monitor = null;

        try {
            monitor = AnalyticsMonitorFactory.createMonitor(this, analytics_app_id, version);
            monitor.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return monitor;
    }

    private void checkAppSettings(boolean showMessage) {
        StringBuilder sb = new StringBuilder();
        String EOL = "\r\n";

        String backend_services_api_key = getString(R.string.backend_services_api_key);
        if (backend_services_api_key != null && backend_services_api_key.equals("your Backend Services api key")) {
            sb.append("Backend Services API Key is not set." + EOL);
        } else {
            BaseViewModel.EverliveAPP = new EverliveApp(backend_services_api_key);
        }

        String facebook_app_id = getString(R.string.facebook_app_id);
        if (facebook_app_id != null && facebook_app_id.equals("your facebook app id")) {
//            Log.i(getString(R.string.app_name), "Facebook App ID is not set. You cannot use Facebook login.");
            sb.append("Facebook App ID is not set. You cannot use Facebook login." + EOL);
            setImageButtonEnabled(this, false, (ImageButton) findViewById(R.id.l_facebookLogin), R.drawable.icon_facebook);
        } else {
            findViewById(R.id.l_facebookLogin).setOnClickListener(this);
        }

        String live_id_client_id = getString(R.string.live_id_client_id);
        if (live_id_client_id != null && live_id_client_id.equals("your live id client id")) {
            sb.append("LiveID Client ID is not set. You cannot use LiveID login." + EOL);
            setImageButtonEnabled(this, false, (ImageButton) findViewById(R.id.l_liveIDLogin), R.drawable.icon_liveid);
        } else {
            findViewById(R.id.l_liveIDLogin).setOnClickListener(this);
        }

        String analytics_app_id = getString(R.string.analytics_app_id);
        if (analytics_app_id != null && analytics_app_id.equals("your Analytics Services app id")) {
            sb.append("Analytics Services product key is not set. You cannot use Analytics Services." + EOL);
        } else {
            IAnalyticsMonitor monitor = initializeAnalyticsService(analytics_app_id);
            BaseViewModel.getInstance().setMonitor(monitor);
        }

        if (showMessage && sb.length() > 0) {
            this.showAlertMessage(this, sb.toString().substring(0, sb.length() - 2), null);
        }
    }

    private AccountManager mAccountManager;

    private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(
                GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    public static void setImageButtonEnabled(Context ctxt, boolean enabled, ImageButton item,
                                             int iconResId) {
        item.setEnabled(enabled);
        Drawable originalIcon = ctxt.getResources().getDrawable(iconResId);
        Drawable icon = enabled ? originalIcon : convertDrawableToGrayScale(originalIcon);
        item.setImageDrawable(icon);
        if (!enabled) {
            item.setBackgroundColor(Color.argb(255, 47, 93, 128));
        }
    }

    public static Drawable convertDrawableToGrayScale(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Drawable res = drawable.mutate();
        res.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IAnalyticsMonitor monitor = BaseViewModel.getInstance().getMonitor();
        if (monitor != null) {
            monitor.stop();
        }
        Session activeSession = Session.getActiveSession();
        if (activeSession != null) {
            activeSession.closeAndClearTokenInformation();
        }
    }

    public static void showAlertMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(R.string.app_name);
        alertDialogBuilder.setPositiveButton("OK", listener);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l_login : {
                this.onLogin();
                break;
            }
            case R.id.l_createNewUser :  {
                Intent i = new Intent(this, CreateNewUserActivity.class);
                startActivity(i);
                break;
            }
            case R.id.l_facebookLogin : {
                this.onFacebookLogin();
                break;
            }
            case R.id.l_googleLogin : {
                this.onGoogleLogin();
                break;
            }
            case R.id.l_liveIDLogin : {
                this.onLiveIDLogin();
                break;
            }
            case R.id.l_ADFSLogin : {
                this.onAdfsLogin();
                break;
            }
        }
    }

    private void onAdfsLogin() {
        connectionProgressDialog.show();
        // https is required for ADFS login.
        BaseViewModel.EverliveAPP.getConnectionSettings().setUseHttps(true);
        BaseViewModel.EverliveAPP.workWith().authentication().
                loginWithADFS(this.username.getText().toString(), this.password.getText().toString()).
                executeAsync(new LoginRequestResultCallbackAction(this, "ADFS", connectionProgressDialog));
    }

    public void onLiveIDLogin() {
        connectionProgressDialog.show();
        String liveAppID = getString(R.string.live_id_client_id);
        this.auth = new LiveAuthClient(this, liveAppID);
        this.auth.login(this, Arrays.asList(new String[]{"wl.basic"}), new LiveAuthListener() {
            public void onAuthComplete(LiveStatus status, LiveConnectSession session, Object userState) {
                if (status == LiveStatus.CONNECTED) {
                    String liveIdAccessToken = session.getAccessToken();
                    BaseViewModel.EverliveAPP.workWith().
                            authentication().
                            loginWithLiveId(liveIdAccessToken).executeAsync(new LoginRequestResultCallbackAction(LoginActivity.this, "LiveID", connectionProgressDialog));
                    client = new LiveConnectClient(session);
                } else {
                    Toast.makeText(getBaseContext(), "Not Signed in with LiveID!", Toast.LENGTH_LONG).show();
                    client = null;
                }
            }
            public void onAuthError(LiveAuthException exception, Object userState) {
                Toast.makeText(getBaseContext(), "Error signing in: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                client = null;
            }
        });
    }

    private void onGoogleLogin() {
        this.mPlusClient = new PlusClient
                    .Builder(this, this, this)
                    .setScopes(Scopes.PLUS_LOGIN + " " + "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email")
                    .build();
        connectionProgressDialog.show();
        this.mPlusClient.connect();
    }

    private void onFacebookLogin() {
        connectionProgressDialog.show();
        Session activeSession = Session.getActiveSession();

        if (activeSession == null) {
            activeSession = new Session(this);
            Session.setActiveSession(activeSession);
        }

        Session.StatusCallback statusCallback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                if (session.isOpened()) {
                    BaseViewModel.EverliveAPP.workWith().
                            authentication().
                            loginWithFacebook(session.getAccessToken()).
                            executeAsync(new LoginRequestResultCallbackAction(LoginActivity.this, "Facebook", connectionProgressDialog));
                }
            }
        };

        if (!activeSession.isOpened() && !activeSession.isClosed()){
            activeSession.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        }else{
            activeSession.openActiveSession(this, true, statusCallback);
        }
    }

    public void onLogin() {
        connectionProgressDialog.show();
        BaseViewModel.EverliveAPP.workWith().
                authentication().
                login(this.username.getText().toString(), this.password.getText().toString()).
                executeAsync(new LoginRequestResultCallbackAction(this, "Regular", connectionProgressDialog));
    }

    public static void startListActivity(Activity activity) {
        Intent i = new Intent(activity, ListActivity.class);
        activity.startActivity(i);
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
//        final String accountName = mPlusClient.getAccountName();
        final String accountName;
        final Context context = this.getApplicationContext();
        GoogleLoginTask task;
        String[] accounts = getAccountNames();
        if (accounts.length > 1) {
            new AccountChooser(this, accounts, connectionProgressDialog).show();
//            accountName = BaseViewModel.getInstance().getSelectedAccount();
//            task = new GoogleLoginTask(context, accountName);
//            task.execute((Void) null);
        } else {
            accountName = accounts[0];
            task = new GoogleLoginTask(this, accountName, connectionProgressDialog);
            task.execute((Void) null);
        }
    }

    @Override
    public void onDisconnected() {
        Log.d(getString(R.string.app_name), "disconnected");
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
