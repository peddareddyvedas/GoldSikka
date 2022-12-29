package com.goldsikka.goldsikka.model;

import com.goldsikka.goldsikka.Models.MainModuleModel;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class data {


    @SerializedName("data")
    private List<Listmodel> result = new ArrayList<>();

    @SerializedName("wishlist")
    private List<Listmodel> wishlist = new ArrayList<>();

    public List<Listmodel> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Listmodel> wishlist) {
        this.wishlist = wishlist;
    }

    @SerializedName("features")
    private List<Listmodel> features = new ArrayList<>();


    public List<Listmodel> getResult() {
        return result;
    }

    public void setResult(List<Listmodel> result) {
        this.result = result;
    }
    @SerializedName("subContent")
    private List<Listmodel> subContent = new ArrayList<>();

    @SerializedName("product_image")
    private List<Listmodel> product_image = new ArrayList<>();
    @SerializedName("scheme_sub_content")
    private List<Listmodel> scheme_sub_content = new ArrayList<>();
    @SerializedName("scheme_faqs")
    private List<Listmodel> scheme_faqs = new ArrayList<>();
    @SerializedName("scheme_features")
    private List<Listmodel> scheme_features = new ArrayList<>();
    @SerializedName("transactions")
    private List<Listmodel> transactions = new ArrayList<>();

    @SerializedName("user_scheme_installments")
    private List<Listmodel> user_scheme_installments = new ArrayList<>();

    public List<Listmodel> getFeatures() {
        return features;
    }

    public void setFeatures(List<Listmodel> features) {
        this.features = features;
    }

    public List<Listmodel> getProduct_image() {
        return product_image;
    }

    public void setProduct_image(List<Listmodel> product_image) {
        this.product_image = product_image;
    }

    public List<Listmodel> getUser_scheme_installments() {
        return user_scheme_installments;
    }

    public void setUser_scheme_installments(List<Listmodel> user_scheme_installments) {
        this.user_scheme_installments = user_scheme_installments;
    }

    public List<Listmodel> getSubContent() {
        return subContent;
    }

    public void setSubContent(List<Listmodel> subContent) {
        this.subContent = subContent;
    }

    public List<Listmodel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Listmodel> transactions) {
        this.transactions = transactions;
    }


    public List<Listmodel> getScheme_sub_content() {
        return scheme_sub_content;
    }

    public void setScheme_sub_content(List<Listmodel> scheme_sub_content) {
        this.scheme_sub_content = scheme_sub_content;
    }

    public List<Listmodel> getScheme_faqs() {
        return scheme_faqs;
    }

    public void setScheme_faqs(List<Listmodel> scheme_faqs) {
        this.scheme_faqs = scheme_faqs;
    }

    public List<Listmodel> getScheme_features() {
        return scheme_features;
    }

    public void setScheme_features(List<Listmodel> scheme_features) {
        this.scheme_features = scheme_features;
    }
}
