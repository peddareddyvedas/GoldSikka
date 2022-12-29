package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NomineeDetailsModelClass {

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("address")
    @Expose
    String address;

    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("city")
    @Expose
    String city;

    @SerializedName("relation")
    @Expose
    String relation;

    @SerializedName("state_id")
    @Expose
    String state_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }
}
