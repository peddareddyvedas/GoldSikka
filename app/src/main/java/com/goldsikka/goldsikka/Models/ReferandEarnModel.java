package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferandEarnModel {

    @SerializedName("content")
    @Expose
    String content;
    @SerializedName("userCount")
    @Expose
    String userCount;
    @SerializedName("referralCode")
    @Expose
    String referralCode;
    @SerializedName("earningsAmount")
    @Expose
    String earningsAmount;
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("is_bank")
    @Expose
    Boolean is_bank;

    public Boolean getIs_bank() {
        return is_bank;
    }

    public void setIs_bank(Boolean is_bank) {
        this.is_bank = is_bank;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getEarningsAmount() {
        return earningsAmount;
    }

    public void setEarningsAmount(String earningsAmount) {
        this.earningsAmount = earningsAmount;
    }
}
