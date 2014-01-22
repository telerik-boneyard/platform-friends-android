package com.telerik.app.model;

import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.Date;
import java.util.UUID;

@ServerType("Users")
public class MyUser extends User {
    @ServerProperty("Picture")
    private UUID pictureId;

    @ServerProperty("BirthDate")
    private Date birthDate;

    @ServerProperty("Gender")
    private Integer gender;

    @ServerProperty("About")
    private String about;

    public UUID getPictureId() {
        return pictureId;
    }

    public void setPictureId(UUID pictureId) {
        this.pictureId = pictureId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
