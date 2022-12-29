package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannersModel {
    @SerializedName("banner_uri")
    @Expose
    String banner_uri;
    public BannersModel(String get_banners){
        this.banner_uri = get_banners;
    }

    public String getBanner_uri() {
        return banner_uri;
    }

    public void setBanner_uri(String banner_uri) {
        this.banner_uri = banner_uri;
    }
}
