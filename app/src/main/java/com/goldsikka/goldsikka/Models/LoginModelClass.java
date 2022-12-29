package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModelClass {

    @SerializedName("accessToken")
    @Expose
    String accessToken;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("maskedPhone")
    @Expose
    String maskedPhone;

    @SerializedName("role_id")
    @Expose
    String role_id;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaskedPhone() {
        return maskedPhone;
    }

    public void setMaskedPhone(String maskedPhone) {
        this.maskedPhone = maskedPhone;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
}
