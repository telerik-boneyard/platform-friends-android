package com.telerik.app.model;

import android.graphics.Bitmap;

import com.telerik.everlive.sdk.core.EverliveApp;

import java.util.Hashtable;
import java.util.UUID;

import eqatec.analytics.monitor.IAnalyticsMonitor;

public class BaseViewModel {
//    private static String API_KEY = "E8tMoGEClJf0xmvV";
    public static EverliveApp EverliveAPP;

    private Post selectedPost;
    private Hashtable<UUID, Bitmap> pictures = new Hashtable<UUID, Bitmap>();
    private Hashtable<UUID, MyUser> users = new Hashtable<UUID, MyUser>();
    private MyUser loggedUser;
    private String selectedAccount;
    private IAnalyticsMonitor monitor;

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

    public String getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public IAnalyticsMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(IAnalyticsMonitor monitor) {
        this.monitor = monitor;
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
