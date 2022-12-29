package com.goldsikka.goldsikka.model.Ecommerce_ModelClass;

import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Ecommerce_ModelClass {

    @SerializedName("product_details")

    private List<Listmodel> product_details = new ArrayList<>();

    public List<Listmodel> getProduct_details() {
        return product_details;
    }

    public void setProduct_details(List<Listmodel> product_details) {
        this.product_details = product_details;
    }
    @SerializedName("product_image")
    private List<Listmodel> product_image = new ArrayList<>();


    public List<Listmodel> getProduct_image() {
        return product_image;
    }

    public void setProduct_image(List<Listmodel> product_image) {
        this.product_image = product_image;
    }
    @SerializedName("order_items")
    private List<Listmodel> order_items = new ArrayList<>();

    public List<Listmodel> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<Listmodel> order_items) {
        this.order_items = order_items;
    }



    @SerializedName("totalAmount")
    @Expose
    String totalAmount;


    @SerializedName("price")
    @Expose
    String price;

    @SerializedName("data")
    @Expose
    JSONArray data;

    @SerializedName("order_id")
    @Expose
    String order_id;
    @SerializedName("source")
    @Expose
    String source;

    @SerializedName("emiDate")
    @Expose
    String emiDate;

    @SerializedName("category_uri")
    @Expose
    String category_uri;

    @SerializedName("description")
    @Expose
    String description;



    @SerializedName("final_amount")
    @Expose
    String final_amount;

    @SerializedName("product_uri")
    @Expose
    String product_uri;

    @SerializedName("productCount")
    @Expose
    String productCount;

    @SerializedName("quantity")
    @Expose
    String quantity;
    @SerializedName("grams")
    @Expose
    String grams;
    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("product_name")
    @Expose
    JsonElement product_name;

    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("finalAmount")
    @Expose
    String finalAmount;

    @SerializedName("user_transactions")
    @Expose
    JsonElement user_transactions;

    @SerializedName("screen_uri")
    @Expose
    String screen_uri;

    @SerializedName("amountDetails")
    @Expose
    JsonElement amountDetails;

    @SerializedName("updatedDate")
    @Expose
    String updatedDate;

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEmiDate() {
        return emiDate;
    }

    public void setEmiDate(String emiDate) {
        this.emiDate = emiDate;
    }

    public String getCategory_uri() {
        return category_uri;
    }

    public void setCategory_uri(String category_uri) {
        this.category_uri = category_uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getProduct_uri() {
        return product_uri;
    }

    public void setProduct_uri(String product_uri) {
        this.product_uri = product_uri;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonElement getProduct_name() {
        return product_name;
    }

    public void setProduct_name(JsonElement product_name) {
        this.product_name = product_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public JsonElement getUser_transactions() {
        return user_transactions;
    }

    public void setUser_transactions(JsonElement user_transactions) {
        this.user_transactions = user_transactions;
    }

    public String getScreen_uri() {
        return screen_uri;
    }

    public void setScreen_uri(String screen_uri) {
        this.screen_uri = screen_uri;
    }

    public JsonElement getAmountDetails() {
        return amountDetails;
    }

    public void setAmountDetails(JsonElement amountDetails) {
        this.amountDetails = amountDetails;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
