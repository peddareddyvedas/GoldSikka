package com.goldsikka.goldsikka.Models;

import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SchemeModel {
    @SerializedName("data")
    private List<SchemeModel> result = new ArrayList<>();

    @SerializedName("scheme_sub_content")
    private List<Listmodel> scheme_sub_content = new ArrayList<>();
    @SerializedName("scheme_faqs")
    private List<SchemeModel> scheme_faqs = new ArrayList<>();
    @SerializedName("scheme_features")
    private List<SchemeModel> scheme_features = new ArrayList<>();


    @SerializedName("user_scheme_installments")
    private List<SchemeModel> user_scheme_installments = new ArrayList<>();


    @SerializedName("created_date")
    @Expose
    String created_date;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("total_installments")
    @Expose
    String total_installments;

    @SerializedName("user_id")
    @Expose
    String user_id;

    @SerializedName("emi_grams")
    @Expose
    String emi_grams;

    @SerializedName("created_at")
    @Expose
    String created_at;

    @SerializedName("updated_at")
    @Expose
    String updated_at;

    @SerializedName("processing_fee")
    @Expose
    String processing_fee;

    @SerializedName("charge_date")
    @Expose
    String charge_date;

    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("scheme_title")
    @Expose
    JsonElement Scheme_title;

    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("current_page")
    @Expose
    int current_page;
    @SerializedName("last_page")
    @Expose
    int last_page;


    @SerializedName("paid_installments")
    @Expose
    String paid_installments;



    @SerializedName("pending_installments")
    @Expose
    String pending_installments;

    @SerializedName("scheme_calculation_type")
    @Expose
    String scheme_calculation_type;



    @SerializedName("scheme_id")
    @Expose
    String scheme_id;

    @SerializedName("emi_date")
    @Expose
    String emi_date;



    @SerializedName("payment_status")
    @Expose
    String payment_status;

    @SerializedName("payment_method")
    @Expose
    String payment_method;

    @SerializedName("transaction_id")
    @Expose
    String transaction_id;


    @SerializedName("user_scheme_id")
    @Expose
    String user_scheme_id;

    @SerializedName("payment_order_id")
    @Expose
    String payment_order_id;

    @SerializedName("grams")
    @Expose
    String grams;

    @SerializedName("gst")
    @Expose
    String gst;

    @SerializedName("total_amount")
    @Expose
    String total_amount;

    @SerializedName("source")
    @Expose
    String source;

    @SerializedName("user_transactions")
    @Expose
    JsonElement user_transactions;

    @SerializedName("scheme_status")
    @Expose
    int scheme_status;

    @SerializedName("schemeStatus")
    @Expose
    String schemeStatus;
    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("livePrice")
    @Expose
    String livePrice;
    @SerializedName("gramsEmi")
    @Expose
    String gramsEmi;
    @SerializedName("emiAmount")
    @Expose
    String emiAmount;
    @SerializedName("totalAmount")
    @Expose
    String totalAmount;
    @SerializedName("cashbackAmount")
    @Expose
    String cashbackAmount;
    @SerializedName("finalAmount")
    @Expose
    String finalAmount;
    @SerializedName("processed")
    @Expose
    boolean purchase_Status;

    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("nick_name")
    @Expose
    String nick_name;

    @SerializedName("nickName")
    @Expose
    String nickName;


    @SerializedName("isNickname")
    @Expose
    boolean isNickname;

    @SerializedName("isPay")
    @Expose
    boolean isPay;

    public boolean isNickname() {
        return isNickname;
    }

    public void setNickname(boolean nickname) {
        isNickname = nickname;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPurchase_Status() {
        return purchase_Status;
    }

    public void setPurchase_Status(boolean purchase_Status) {
        this.purchase_Status = purchase_Status;
    }

    public String getLivePrice() {
        return livePrice;
    }

    public void setLivePrice(String livePrice) {
        this.livePrice = livePrice;
    }

    public String getGramsEmi() {
        return gramsEmi;
    }

    public void setGramsEmi(String gramsEmi) {
        this.gramsEmi = gramsEmi;
    }

    public String getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(String emiAmount) {
        this.emiAmount = emiAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(String cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public int getScheme_status() {
        return scheme_status;
    }

    public void setScheme_status(int scheme_status) {
        this.scheme_status = scheme_status;
    }

    public String getSchemeStatus() {
        return schemeStatus;
    }

    public void setSchemeStatus(String schemeStatus) {
        this.schemeStatus = schemeStatus;
    }

    public JsonElement getUser_transactions() {
        return user_transactions;
    }

    public void setUser_transactions(JsonElement user_transactions) {
        this.user_transactions = user_transactions;
    }

    public String getPaid_installments() {
        return paid_installments;
    }

    public void setPaid_installments(String paid_installments) {
        this.paid_installments = paid_installments;
    }

    public String getPending_installments() {
        return pending_installments;
    }

    public void setPending_installments(String pending_installments) {
        this.pending_installments = pending_installments;
    }

    public String getScheme_calculation_type() {
        return scheme_calculation_type;
    }

    public void setScheme_calculation_type(String scheme_calculation_type) {
        this.scheme_calculation_type = scheme_calculation_type;
    }

    public String getScheme_id() {
        return scheme_id;
    }

    public void setScheme_id(String scheme_id) {
        this.scheme_id = scheme_id;
    }

    public String getEmi_date() {
        return emi_date;
    }

    public void setEmi_date(String emi_date) {
        this.emi_date = emi_date;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getUser_scheme_id() {
        return user_scheme_id;
    }

    public void setUser_scheme_id(String user_scheme_id) {
        this.user_scheme_id = user_scheme_id;
    }

    public String getPayment_order_id() {
        return payment_order_id;
    }

    public void setPayment_order_id(String payment_order_id) {
        this.payment_order_id = payment_order_id;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal_installments() {
        return total_installments;
    }

    public void setTotal_installments(String total_installments) {
        this.total_installments = total_installments;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmi_grams() {
        return emi_grams;
    }

    public void setEmi_grams(String emi_grams) {
        this.emi_grams = emi_grams;
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

    public String getProcessing_fee() {
        return processing_fee;
    }

    public void setProcessing_fee(String processing_fee) {
        this.processing_fee = processing_fee;
    }

    public String getCharge_date() {
        return charge_date;
    }

    public void setCharge_date(String charge_date) {
        this.charge_date = charge_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public JsonElement getScheme_title() {
        return Scheme_title;
    }

    public void setScheme_title(JsonElement scheme_title) {
        Scheme_title = scheme_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SchemeModel> getResult() {
        return result;
    }

    public void setResult(List<SchemeModel> result) {
        this.result = result;
    }

    public List<Listmodel> getScheme_sub_content() {
        return scheme_sub_content;
    }

    public void setScheme_sub_content(List<Listmodel> scheme_sub_content) {
        this.scheme_sub_content = scheme_sub_content;
    }

    public List<SchemeModel> getScheme_faqs() {
        return scheme_faqs;
    }

    public void setScheme_faqs(List<SchemeModel> scheme_faqs) {
        this.scheme_faqs = scheme_faqs;
    }

    public List<SchemeModel> getScheme_features() {
        return scheme_features;
    }

    public void setScheme_features(List<SchemeModel> scheme_features) {
        this.scheme_features = scheme_features;
    }

    public List<SchemeModel> getUser_scheme_installments() {
        return user_scheme_installments;
    }

    public void setUser_scheme_installments(List<SchemeModel> user_scheme_installments) {
        this.user_scheme_installments = user_scheme_installments;
    }
}
