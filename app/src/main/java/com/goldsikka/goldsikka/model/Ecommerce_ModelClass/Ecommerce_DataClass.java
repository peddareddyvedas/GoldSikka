package com.goldsikka.goldsikka.model.Ecommerce_ModelClass;


import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Ecommerce_DataClass {

    @SerializedName("data")
    private List<Ecommerce_ModelClass> result = new ArrayList<>();

    @SerializedName("product_image")
    private List<Ecommerce_ModelClass> product_image = new ArrayList<>();

    @SerializedName("transactions")
    private List<Ecommerce_ModelClass> transactions = new ArrayList<>();

    public List<Ecommerce_ModelClass> getResult() {
        return result;
    }

    public void setResult(List<Ecommerce_ModelClass> result) {
        this.result = result;
    }

    public List<Ecommerce_ModelClass> getProduct_image() {
        return product_image;
    }

    public void setProduct_image(List<Ecommerce_ModelClass> product_image) {
        this.product_image = product_image;
    }

    public List<Ecommerce_ModelClass> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Ecommerce_ModelClass> transactions) {
        this.transactions = transactions;
    }
}
