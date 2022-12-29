package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPModelClass {

    @SerializedName("verified")
    @Expose
    String otpverofy;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("maskedPhone")
    @Expose
    String maskedPhone;

    @SerializedName("last2")
    @Expose
    String last2digits;

    public String getOtpverofy() {
        return otpverofy;
    }

    public void setOtpverofy(String otpverofy) {
        this.otpverofy = otpverofy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMaskedPhone() {
        return maskedPhone;
    }

    public void setMaskedPhone(String maskedPhone) {
        this.maskedPhone = maskedPhone;
    }

    public String getLast2digits() {
        return last2digits;
    }

    public void setLast2digits(String last2digits) {
        this.last2digits = last2digits;
    }
}
