package com.goldsikka.goldsikka.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewsModel {

    @SerializedName("rating")
    @Expose
    String rrating;

    @SerializedName("review")
    @Expose
    String rreview;

    @SerializedName("user_id")
    @Expose
    String ruser_id;

    @SerializedName("created_at")
    @Expose
    String rcreated_at;


    public String getRrating() {
        return rrating;
    }

    public void setRrating(String rrating) {
        this.rrating = rrating;
    }

    public String getRreview() {
        return rreview;
    }

    public void setRreview(String rreview) {
        this.rreview = rreview;
    }

    public String getRuser_id() {
        return ruser_id;
    }

    public void setRuser_id(String ruser_id) {
        this.ruser_id = ruser_id;
    }

    public String getRcreated_at() {
        return rcreated_at;
    }

    public void setRcreated_at(String rcreated_at) {
        this.rcreated_at = rcreated_at;
    }


}
