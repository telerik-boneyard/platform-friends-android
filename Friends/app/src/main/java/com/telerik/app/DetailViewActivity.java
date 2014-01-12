package com.telerik.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import model.BaseViewModel;
import model.MyUser;
import model.Post;

public class DetailViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        Post selectedPost = BaseViewModel.getInstance().getSelectedPost();
        MyUser postedUser = BaseViewModel.getInstance().getUserById(selectedPost.getUserId());
        BitmapDownloadTask task = new BitmapDownloadTask(this, (ImageView) this.findViewById(R.id.dv_postPicture), false);
        task.execute(selectedPost.getPictureId() != null ? selectedPost.getPictureId().toString() : null);

        BitmapDownloadTask task1 = new BitmapDownloadTask(this, (ImageView) this.findViewById(R.id.dv_userPicture), true);
        task1.execute(postedUser.getPictureId() != null ? postedUser.getPictureId().toString() : null);

        ((TextView) findViewById(R.id.dv_userDisplayName)).setText(postedUser.getDisplayName());
        ((TextView) findViewById(R.id.dv_postText)).setText(selectedPost.getText());
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        ((TextView) findViewById(R.id.dv_createdAt)).setText(dateFormat.format(selectedPost.getCreatedAt()).toUpperCase());

        getActionBar().hide();
    }

}
