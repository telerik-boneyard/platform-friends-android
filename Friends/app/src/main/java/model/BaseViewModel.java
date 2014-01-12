package model;

import android.graphics.Bitmap;

import com.telerik.everlive.sdk.core.EverliveApp;

import java.util.Hashtable;
import java.util.UUID;

public class BaseViewModel {
    private static final String API_KEY = "9Hszw6CETUzpynmN";
    public static final EverliveApp EverliveAPP = new EverliveApp(API_KEY);

    private Post selectedPost;
    private Hashtable<UUID, Bitmap> pictures = new Hashtable<UUID, Bitmap>();
    private Hashtable<UUID, MyUser> users = new Hashtable<UUID, MyUser>();
    private MyUser loggedUser;

    private static BaseViewModel instance;

    public void addUser(MyUser user) {
        this.users.put(user.getId(), user);
    }

    public MyUser getUserById(UUID id) {
        return this.users.get(id);
    }

    public void addPicture(UUID id, Bitmap image) {
        this.pictures.put(id, image);
    }

    public Bitmap getPictureById(UUID id) {
        return this.pictures.get(id);
    }

    public Post getSelectedPost() {
        return selectedPost;
    }

    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }

    public MyUser getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(MyUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    private BaseViewModel() {
    }

    public static BaseViewModel getInstance() {
        if (instance == null) {
            instance = new BaseViewModel();
        }
        return instance;
    }
}
