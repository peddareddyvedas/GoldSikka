package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PredictionListModel {

    @SerializedName("current_page")
    @Expose
    int current_page;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("price_predicted")
    @Expose
    String price_predicted;
    @SerializedName("is_matched")
    @Expose
    String is_matched;
    @SerializedName("created_date")
    @Expose
    String created_date;
    @SerializedName("update")
    @Expose
    String update;

    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("predicted_message")
    @Expose
    String predicted_message;
    @SerializedName("last_page")
    @Expose
    int last_page;

    public String getPredicted_message() {
        return predicted_message;
    }

    public void setPredicted_message(String predicted_message) {
        this.predicted_message = predicted_message;
    }

    @SerializedName("data")
    private List<PredictionListModel> result = new ArrayList<>();

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice_predicted() {
        return price_predicted;
    }

    public void setPrice_predicted(String price_predicted) {
        this.price_predicted = price_predicted;
    }

    public String getIs_matched() {
        return is_matched;
    }

    public void setIs_matched(String is_matched) {
        this.is_matched = is_matched;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<PredictionListModel> getResult() {
        return result;
    }

    public void setResult(List<PredictionListModel> result) {
        this.result = result;
    }
}
