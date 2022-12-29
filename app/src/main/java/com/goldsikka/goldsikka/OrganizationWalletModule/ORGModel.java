package com.goldsikka.goldsikka.OrganizationWalletModule;

import com.goldsikka.goldsikka.Activitys.Coupons.CouponsModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ORGModel {

    @SerializedName("data")
    private List<ORGModel> result = new ArrayList<>();

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("mobile")
    @Expose
    String mobile;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("profileStatus")
    @Expose
    String profileStatus;

    @SerializedName("created_date")
    @Expose
    String created_date;

    @SerializedName("current_page")
    int current_page;
    @SerializedName("last_page")
    int last_page;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<ORGModel> getResult() {
        return result;
    }

    public void setResult(List<ORGModel> result) {
        this.result = result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
