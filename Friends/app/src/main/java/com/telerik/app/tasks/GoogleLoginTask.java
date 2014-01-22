package com.telerik.app.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.telerik.app.activities.LoginActivity;

import java.io.IOException;

import com.telerik.app.model.BaseViewModel;

public class GoogleLoginTask extends AsyncTask {
    private String token;
    private String accountName;
    private Activity activity;
    private ProgressDialog progressDialog;

    public GoogleLoginTask(Activity activity, String accountName, ProgressDialog progressDialog) {
        this.activity = activity;
        this.accountName = accountName;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Object doInBackground(Object... params) {
        String scope = "oauth2: " + Scopes.PLUS_LOGIN + " " + Scopes.PLUS_PROFILE;
        try {
            this.token = GoogleAuthUtil.getToken(this.activity, accountName, "oauth2:" + Scopes.PLUS_LOGIN + " " +
                    "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
            BaseViewModel.EverliveAPP.workWith().
                    authentication().
                    loginWithGoogle(this.token).
                    execute(new LoginRequestResultCallbackAction(this.activity, "Google", this.progressDialog));
        } catch (UserRecoverableAuthException e) {
            Intent recover = e.getIntent();
            activity.startActivityForResult(recover, LoginActivity.REQUEST_CODE_RESOLVE_ERR);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        }
        return null;
    }
}
