package com.telerik.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.telerik.app.tasks.BitmapDownloadTask;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import model.BaseViewModel;
import model.MyUser;
import model.Post;

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
                    getMe().
                    executeAsync(new RequestResultCallbackAction<MyUser>() {
                        @Override
                        public void invoke(RequestResult<MyUser> requestResult) {
                            if (requestResult.getSuccess()) {
                                loggedUser[0] = requestResult.getValue();
                                BaseViewModel.getInstance().setLoggedUser(loggedUser[0]);
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
    }

    private void loadUserInfo(ImageView userPicture, final MyUser user, final TextView userName) {
        BitmapDownloadTask task = new BitmapDownloadTask(this, userPicture, ImageKind.User);
        task.execute(user.getPictureId() != null ? user.getPictureId().toString() : null);
        userName.post(new Runnable() {
            @Override
            public void run() {
                userName.setText(user.getDisplayName());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Post post = new Post();
            post.setUserId(BaseViewModel.getInstance().getLoggedUser().getId());
            post.setText(((EditText) findViewById(R.id.cna_activityText)).getText().toString());
            BaseViewModel.EverliveAPP.workWith().data(Post.class).create(post).executeAsync(new RequestResultCallbackAction() {
                @Override
                public void invoke(RequestResult requestResult) {
                    if (requestResult.getSuccess()) {
                        Intent i = new Intent(CreateNewPostActivity.this, ListActivity.class);
                        startActivity(i);
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}