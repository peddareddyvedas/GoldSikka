package com.goldsikka.goldsikka.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stones {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("stonename")
    @Expose
    private String stonename;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("stoneprice")
    @Expose
    private String stoneprice;
    @SerializedName("noofstones")
    @Expose
    private String noofstones;
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

    public String getStonename() {
        return stonename;
    }

    public String getColor() {
        return color;
    }

    public String getStoneprice() {
        return stoneprice;
    }

    public String getNoofstones() {
        return noofstones;
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

    public void setStonename(String stonename) {
        this.stonename = stonename;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setStoneprice(String stoneprice) {
        this.stoneprice = stoneprice;
    }

    public void setNoofstones(String noofstones) {
        this.noofstones = noofstones;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
