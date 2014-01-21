package com.telerik.app.model;

import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.UUID;

@ServerType("Activities")
public class Post extends DataItem {
    @ServerProperty("Text")
    private String text;

    @ServerProperty("UserId")
    private UUID userId;

    @ServerProperty("Picture")
    private UUID pictureId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPictureId() {
        return pictureId;
    }

    public void setPictureId(UUID pictureId) {
        this.pictureId = pictureId;
    }

    @Override
    public String toString() {
        return String.format("%1s", this.getText());
    }
}
