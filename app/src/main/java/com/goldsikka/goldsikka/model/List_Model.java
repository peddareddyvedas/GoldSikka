package com.goldsikka.goldsikka.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List_Model {

    @SerializedName("totalAmount")
    String totalAmount;

    @SerializedName("mainContent")
    @Expose
    String mainContent;

    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("info")
    @Expose
    String info;

    @SerializedName("price")
    @Expose
    JsonElement price;

    @SerializedName("data")
    @Expose
    String  data;

    @SerializedName("Winner")
    @Expose
    JsonElement Winner;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("userid")
    @Expose
    String userid;

    @SerializedName("pids")
    @Expose
    String pids;

    @SerializedName("created_at")
    @Expose
    String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @SerializedName("updated_at")
    @Expose
    String updated_at;

    @SerializedName("terms_and_conditions")
    @Expose
    String terms_and_conditions;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public JsonElement getWinner() {
        return Winner;
    }

    public void setWinner(JsonElement winner) {
        Winner = winner;
    }



    public JsonElement getPrice() {
        return price;
    }

    public void setPrice(JsonElement price) {
        this.price = price;
    }

    public String getTerms_and_conditions() {
        return terms_and_conditions;
    }

    public void setTerms_and_conditions(String terms_and_conditions) {
        this.terms_and_conditions = terms_and_conditions;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }
}
