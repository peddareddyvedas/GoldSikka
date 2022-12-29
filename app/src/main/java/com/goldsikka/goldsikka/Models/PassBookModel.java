package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PassBookModel {


    @SerializedName("data")
    private List<PassBookModel> result = new ArrayList<>();

    public List<PassBookModel> getResult() {
        return result;
    }

    public void setResult(List<PassBookModel> result) {
        this.result = result;
    }

    @SerializedName("current_page")
    int current_page;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    @SerializedName("id")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @SerializedName("created_at")
    String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    @SerializedName("last_page")
    int last_page;

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }
    @SerializedName("grams")
    @Expose
    String grams;
    @SerializedName("amount")
    @Expose
    String parchase_amount;
    @SerializedName("desc")
    @Expose
    String desc;

    @SerializedName("type")
    @Expose
    String transction_type;

    @SerializedName("txn_type")
    @Expose
    String txn_type;


    @SerializedName("total_amount")
    @Expose
    String total_amount;
    @SerializedName("gold_price_per_gram")
    @Expose
    String gold_price_per_gram;
    @SerializedName("gst")
    @Expose
    String gst;
    @SerializedName("source")
    @Expose
    String source;

    @SerializedName("transactionStatus")
    @Expose
    String transactionStatus;

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTxn_type() {
        return txn_type;
    }

    public void setTxn_type(String txn_type) {
        this.txn_type = txn_type;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getGold_price_per_gram() {
        return gold_price_per_gram;
    }

    public void setGold_price_per_gram(String gold_price_per_gram) {
        this.gold_price_per_gram = gold_price_per_gram;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTransction_type() {
        return transction_type;
    }

    public void setTransction_type(String transction_type) {
        this.transction_type = transction_type;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public String getParchase_amount() {
        return parchase_amount;
    }

    public void setParchase_amount(String parchase_amount) {
        this.parchase_amount = parchase_amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
