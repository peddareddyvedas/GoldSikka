package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetailsModelClass {

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("mobile")
    @Expose
    String mobile;

    @SerializedName("name")
    @Expose
    String name;

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

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name_on_account")
    @Expose
    String name_on_account;

    @SerializedName("account_number")
    @Expose
    String account_number;

    @SerializedName("bank_name")
    @Expose
    String bank_name;

    @SerializedName("bank_branch")
    @Expose
    String bank_branch;

    @SerializedName("ifsc_code")
    @Expose
    String ifsc_code;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_on_account() {
        return name_on_account;
    }

    public void setName_on_account(String name_on_account) {
        this.name_on_account = name_on_account;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(String bank_branch) {
        this.bank_branch = bank_branch;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }
}
