package com.telerik.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import model.BaseViewModel;
import model.MyUser;
import model.Post;

public class PostAdapter extends ArrayAdapter<Post> {
    private Context context;
    private int layoutResourceId;
    private List<Post> posts;

    public PostAdapter(Context context, int layoutResourceId, List<Post> posts) {
        super(context, layoutResourceId, posts);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PostHolder holder = null;

//        Log.d("AndroidSandbox", "getView for position -> " + position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PostHolder();
            holder.userImage = (ImageView) row.findViewById(R.id.lvi_userImage);
            holder.postText = (TextView) row.findViewById(R.id.lvi_postText);
            holder.userName = (TextView) row.findViewById(R.id.lvi_userName);
            holder.postCreateDate = (TextView) row.findViewById(R.id.lvi_createDate);

            row.setTag(holder);
        } else {
            holder = (PostHolder) row.getTag();
        }

        Post post = this.posts.get(position);
        if (post != null) {
            holder.postText.setText(post.getText());
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            holder.postCreateDate.setText(dateFormat.format(post.getCreatedAt()).toUpperCase());
            BaseViewModel.EverliveAPP.workWith().users(MyUser.class).getById(post.getUserId()).
                    executeAsync(new MyRequestResultCallbackAction(holder, ((Activity) context).getCurrentFocus()));

        }

        return row;
    }

    static class PostHolder {
        private ImageView userImage;
        private TextView postText;
        private TextView userName;
        private TextView postCreateDate;
    }

    class MyRequestResultCallbackAction extends RequestResultCallbackAction {
        private PostHolder postHolder;
        private View parentView;

        MyRequestResultCallbackAction(PostHolder postHolder, View parentView) {
            this.postHolder = postHolder;
            this.parentView = parentView;
        }

        @Override
        public void invoke(RequestResult requestResult) {
            if (requestResult.getSuccess()) {
                final MyUser user = (MyUser) requestResult.getValue();
                BaseViewModel.getInstance().addUser(user);
                final String userName = user.getDisplayName();
//                Log.d("AndroidSandbox", "get display nane for user -> " + user.getDisplayName());
                this.parentView.post(new Runnable() {
                    @Override
                    public void run() {
                        postHolder.userName.setText(userName);
                    }
                });
                UUID pictureId = user.getPictureId();
                BitmapDownloadTask task = new BitmapDownloadTask(this.parentView.getContext(), postHolder.userImage, false);
                task.execute(pictureId != null ? pictureId.toString() : null);
            }
        }
    }
}
