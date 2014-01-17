package com.telerik.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.telerik.app.tasks.BitmapDownloadTask;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import model.BaseViewModel;
import model.MyUser;
import model.Post;

public class DetailViewActivity extends Activity implements View.OnClickListener {

    private Button deleteButton;
    private Post selectedPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        this.deleteButton = (Button) findViewById(R.id.dv_deleteButton);
        this.deleteButton.setOnClickListener(this);

        this.updateUI();

        getActionBar().hide();
    }

    private void updateUI() {
        this.selectedPost = BaseViewModel.getInstance().getSelectedPost();
        final MyUser postedUser = BaseViewModel.getInstance().getUserById(this.selectedPost.getUserId());

        MyUser loggedUser = BaseViewModel.getInstance().getLoggedUser();
        if (loggedUser == null) {
            BaseViewModel.EverliveAPP.workWith().
                    users(MyUser.class).
                    getMe().
                    executeAsync(new RequestResultCallbackAction<MyUser>() {
                        @Override
                        public void invoke(RequestResult<MyUser> requestResult) {
                            if (requestResult.getSuccess()) {
                                MyUser logUser = requestResult.getValue();
                                BaseViewModel.getInstance().setLoggedUser(logUser);
                                if (logUser.getId().equals(postedUser.getId())) {
                                    DetailViewActivity.this.deleteButton.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            DetailViewActivity.this.deleteButton.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        }
                    });
        } else {
            if (loggedUser.getId().equals(postedUser.getId())) {
                this.deleteButton.post(new Runnable() {
                    @Override
                    public void run() {
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

        BitmapDownloadTask task = new BitmapDownloadTask(this, (ImageView) this.findViewById(R.id.dv_postPicture), ImageKind.Post);
        task.execute(this.selectedPost.getPictureId() != null ? this.selectedPost.getPictureId().toString() : null);

        BitmapDownloadTask task1 = new BitmapDownloadTask(this, (ImageView) this.findViewById(R.id.dv_userPicture), ImageKind.User);
        task1.execute(postedUser.getPictureId() != null ? postedUser.getPictureId().toString() : null);

        ((TextView) findViewById(R.id.dv_userDisplayName)).setText(postedUser.getDisplayName());
        ((TextView) findViewById(R.id.dv_postText)).setText(selectedPost.getText());
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        ((TextView) findViewById(R.id.dv_createdAt)).setText(dateFormat.format(selectedPost.getCreatedAt()).toUpperCase());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dv_deleteButton : {
                BaseViewModel.EverliveAPP.workWith().
                        data(Post.class).
                        deleteById(this.selectedPost.getId()).
                        executeAsync(new RequestResultCallbackAction() {
                            @Override
                            public void invoke(RequestResult requestResult) {
                                if (requestResult.getSuccess()) {
                                    Intent i = new Intent(DetailViewActivity.this, ListActivity.class);
                                    startActivity(i);
                                }
                            }
                        });
                break;
            }
        }
    }
}
