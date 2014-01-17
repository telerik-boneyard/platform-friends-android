package com.telerik.app;

import android.app.Activity;
import android.content.Context;

import com.telerik.everlive.sdk.core.model.system.AccessToken;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import eqatec.analytics.monitor.IAnalyticsMonitor;
import model.BaseViewModel;

public class LoginRequestResultCallbackAction extends RequestResultCallbackAction<AccessToken> {
    private String loginMethod;
    private Activity activity;

    public LoginRequestResultCallbackAction(Activity activity, String loginMethod) {
        this.activity = activity;
        this.loginMethod = loginMethod;
    }

    @Override
    public void invoke(RequestResult<AccessToken> accessTokenRequestResult) {
        if (accessTokenRequestResult.getSuccess()) {
//            connectionProgressDialog.dismiss();
            LoginActivity.startListActivity(this.activity);
            IAnalyticsMonitor monitor = BaseViewModel.getInstance().getMonitor();
            if (monitor != null) {
                monitor.trackFeature("Login." + this.loginMethod);
            }
        } else {
//            connectionProgressDialog.dismiss();
            final String errorMessage = accessTokenRequestResult.getError().getMessage();
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                LoginActivity.showAlertMessage(activity, errorMessage, null);
                }
            });
        }
    }
}
