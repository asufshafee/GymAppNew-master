package com.araia.henock.volve.Objects;

/**
 * Created by Fazal Mola on 3/7/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelCreateProfile implements Serializable {

    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Cost")
    @Expose
    private String cost;
    @SerializedName("Lattitude")
    @Expose
    private String lattitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("ProfilePhoto")
    @Expose
    private String profilePhoto;
    @SerializedName("Available")
    @Expose
    private Available available;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Available getAvailable() {
        return available;
    }

    public void setAvailable(Available available) {
        this.available = available;
    }

}
