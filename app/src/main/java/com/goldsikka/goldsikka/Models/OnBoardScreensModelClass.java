package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnBoardScreensModelClass {

    @SerializedName("screen_uri")
    @Expose
    String screen_uri;

    public String getScreen_uri() {
        return screen_uri;
    }

    public void setScreen_uri(String screen_uri) {
        this.screen_uri = screen_uri;
    }
}
