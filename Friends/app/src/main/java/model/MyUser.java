package model;

import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.UUID;

@ServerType("Users")
public class MyUser extends User {
    @ServerProperty("Picture")
    private UUID pictureId;

    public UUID getPictureId() {
        return pictureId;
    }

    public void setPictureId(UUID pictureId) {
        this.pictureId = pictureId;
    }
}
