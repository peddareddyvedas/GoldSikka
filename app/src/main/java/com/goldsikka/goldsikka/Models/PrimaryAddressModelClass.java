package com.goldsikka.goldsikka.Models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrimaryAddressModelClass {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("city")
    @Expose
    String city;

    @SerializedName("state_id")
    @Expose
    String state_id;

    @SerializedName("zip")
    @Expose
    String zip;

    @SerializedName("is_primary")
    @Expose
    String is_primary;

    @SerializedName("state")
    @Expose
    JsonElement state;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("code")
    @Expose
    String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(String is_primary) {
        this.is_primary = is_primary;
    }

    public JsonElement getState() {
        return state;
    }

    public void setState(JsonElement state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
