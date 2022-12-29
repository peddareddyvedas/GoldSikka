package com.goldsikka.goldsikka.Models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Enquiryformmodel {


    @SerializedName("data")
    private List<Enquiryformmodel> result = new ArrayList<>();

    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("message")
    @Expose
    String message;


    @SerializedName("current_page")
    @Expose
    int current_page;
    @SerializedName("last_page")
    @Expose
    int last_page;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("scheme")
    @Expose
    JsonElement scheme;
    @SerializedName("charge_date")
    @Expose
    String charge_date;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("ticket_id")
    @Expose
    String ticket_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public List<Enquiryformmodel> getResult() {
        return result;
    }

    public void setResult(List<Enquiryformmodel> result) {
        this.result = result;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonElement getScheme() {
        return scheme;
    }

    public void setScheme(JsonElement scheme) {
        this.scheme = scheme;
    }

    public String getCharge_date() {
        return charge_date;
    }

    public void setCharge_date(String charge_date) {
        this.charge_date = charge_date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
