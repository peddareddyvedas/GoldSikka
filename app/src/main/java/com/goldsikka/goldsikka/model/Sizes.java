package com.goldsikka.goldsikka.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sizes {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("sizes")
    @Expose
    private String sizes;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;





    // Getter Methods

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public String getSizes() {
        return sizes;
    }

    public String getWeight() {
        return weight;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
