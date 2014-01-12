package com.telerik.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.telerik.everlive.sdk.core.model.system.File;
import com.telerik.everlive.sdk.core.result.RequestResult;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.UUID;

import model.BaseViewModel;

public class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewWeakReference;
    private Context context;
    private boolean makePictureCircular;

    public BitmapDownloadTask(Context context, ImageView imageView, boolean makePictureCircular) {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        this.context = context;
        this.makePictureCircular = makePictureCircular;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String pictureId = strings[0];
        try {
            if (pictureId != null) {
                Bitmap bitmap = BaseViewModel.getInstance().getPictureById(UUID.fromString(pictureId));
                if (bitmap != null) {
                    return bitmap;
                }

                String url = null;
                RequestResult userImageRequest = BaseViewModel.EverliveAPP.workWith().files().getById(pictureId).executeSync();
                if (userImageRequest.getSuccess()) {
                    url = ((File) userImageRequest.getValue()).getUri();
                    URL imageUrl = new URL(url);
                    InputStream inputStream = imageUrl.openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);

                    BaseViewModel.getInstance().addPicture(UUID.fromString(pictureId), bitmap);

                    return bitmap;
                }
            }
            if (this.makePictureCircular) {
                return BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_contact_picture_64);
            } else {
                return null;
            }
        } catch (Exception ex) {
//            Log.e("Exception when downloading image: ", ex.getMessage().toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewWeakReference != null && bitmap != null) {
            final ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}