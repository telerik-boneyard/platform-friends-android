package com.telerik.app;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import model.BaseViewModel;
import model.MyUser;

public class CreateNewPostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_post);

        final ImageView userPicture = (ImageView) findViewById(R.id.cna_userPicture);
        final TextView userName = (TextView) findViewById(R.id.cna_userDisplayName);

        final MyUser[] loggedUser = {BaseViewModel.getInstance().getLoggedUser()};
        if (loggedUser[0] == null) {
            BaseViewModel.EverliveAPP.workWith().
                    users(MyUser.class).
                    getMe(MyUser.class).
                    executeAsync(new RequestResultCallbackAction<MyUser>() {
                        @Override
                        public void invoke(RequestResult<MyUser> requestResult) {
                            if (requestResult.getSuccess()) {
                                loggedUser[0] = requestResult.getValue();
                                loadUserInfo(userPicture, loggedUser[0], userName);
                            }
                        }
                    });
        } else {
            loadUserInfo(userPicture, loggedUser[0], userName);
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 52, 73, 94)));

        if (savedInstanceState == null) {

        }
    }

    private void loadUserInfo(ImageView userPicture, final MyUser user, final TextView userName) {
        BitmapDownloadTask task = new BitmapDownloadTask(this, userPicture, true);
        task.execute(user.getPictureId().toString());
        userName.post(new Runnable() {
            @Override
            public void run() {
                userName.setText(user.getDisplayName());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_new_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_post) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}