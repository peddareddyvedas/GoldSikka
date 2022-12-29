package com.goldsikka.goldsikka.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutModel {

    @SerializedName("id")
    private float id;
    @SerializedName("user_id")
    private float user_id;
    @SerializedName("pid")
    private String pid;
    @SerializedName("cart_status")
    private float cart_status;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("cid")
    private float cid;
    @SerializedName("sid")
    private float sid;
    @SerializedName("pname")
    private String pname;
    @SerializedName("image_uri")
    private String image_uri;
    @SerializedName("description")
    private String description;
    @SerializedName("bid")
    private float bid;
    @SerializedName("va")
    private String va;
    @SerializedName("weight")
    private String weight;
    @SerializedName("status")
    private float status;
    @SerializedName("ratings")
    private float ratings;
    @SerializedName("reviews")
    private String reviews;
    @SerializedName("cust_purchased")
    private float cust_purchased;
    @SerializedName("price")
    private float price;
    @SerializedName("va_price")
    private float va_price;
    @SerializedName("total_price")
    private float total_price;
    @SerializedName("liveprice")
    private float liveprice;


    @SerializedName("stoneid")
    private String stoneid;

    public String getStoneid() {
        return stoneid;
    }

    public void setStoneid(String stoneid) {
        this.stoneid = stoneid;
    }



    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @SerializedName("size")
    private String size;


    /////Checkout////
    @SerializedName("processed")
    @Expose
    boolean processed;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    String message;


////////////////////////////////////

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    // Getter Methods

    public float getId() {
        return id;
    }

    public float getUser_id() {
        return user_id;
    }

    public String getPid() {
        return pid;
    }

    public float getCart_status() {
        return cart_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public float getCid() {
        return cid;
    }

    public float getSid() {
        return sid;
    }

    public String getPname() {
        return pname;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public String getDescription() {
        return description;
    }

    public float getBid() {
        return bid;
    }

    public String getVa() {
        return va;
    }

    public String getWeight() {
        return weight;
    }

    public float getStatus() {
        return status;
    }

    public float getRatings() {
        return ratings;
    }

    public String getReviews() {
        return reviews;
    }

    public float getCust_purchased() {
        return cust_purchased;
    }

    public float getPrice() {
        return price;
    }

    public float getVa_price() {
        return va_price;
    }

    public float getTotal_price() {
        return total_price;
    }

    public float getLiveprice() {
        return liveprice;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setUser_id(float user_id) {
        this.user_id = user_id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setCart_status(float cart_status) {
        this.cart_status = cart_status;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setCid(float cid) {
        this.cid = cid;
    }

    public void setSid(float sid) {
        this.sid = sid;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }

    public void setVa(String va) {
        this.va = va;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public void setCust_purchased(float cust_purchased) {
        this.cust_purchased = cust_purchased;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setVa_price(float va_price) {
        this.va_price = va_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public void setLiveprice(float liveprice) {
        this.liveprice = liveprice;
    }

}
