package com.goldsikka.goldsikka.Activitys.Coupons;

import com.goldsikka.goldsikka.Models.PassBookModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CouponsModel {


    @SerializedName("data")
    private List<CouponsModel> result = new ArrayList<>();

    @SerializedName("id")
    String id;
    @SerializedName("coupon")
    String coupon;
    @SerializedName("amount")
    String amount;
    @SerializedName("expiresAt")
    String expiresAt;
    @SerializedName("message")
    String message;
    @SerializedName("description")
    String description;
    @SerializedName("status")
    String status;
    @SerializedName("is_expired")
    boolean is_expired;
    @SerializedName("notApplicable")
    boolean notApplicable;
    @SerializedName("current_page")
    int current_page;
    @SerializedName("last_page")
    int last_page;
    @SerializedName("minimum_transaction_amount")
    String minimum_transaction_amount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIs_expired() {
        return is_expired;
    }

    public void setIs_expired(boolean is_expired) {
        this.is_expired = is_expired;
    }

    public boolean isNotApplicable() {
        return notApplicable;
    }

    public void setNotApplicable(boolean notApplicable) {
        this.notApplicable = notApplicable;
    }

    public String getMinimum_transaction_amount() {
        return minimum_transaction_amount;
    }

    public void setMinimum_transaction_amount(String minimum_transaction_amount) {
        this.minimum_transaction_amount = minimum_transaction_amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public List<CouponsModel> getResult() {
        return result;
    }

    public void setResult(List<CouponsModel> result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
