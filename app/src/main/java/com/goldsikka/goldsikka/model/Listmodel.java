package com.goldsikka.goldsikka.model;


import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Listmodel {


    @SerializedName("accessToken")
    @Expose
    String accessToken;

    @SerializedName("sum")
    @Expose
    int sum;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @SerializedName("pid")
    @Expose
    String pid;

    @SerializedName("wishlist_status")
    @Expose
    int wishlist_status;

    public int getWishlist_status() {
        return wishlist_status;
    }

    public void setWishlist_status(int wishlist_status) {
        this.wishlist_status = wishlist_status;
    }

    @SerializedName("goldtransfer")
    @Expose
    String goldtransfer;

    public String getGoldtransfer() {
        return goldtransfer;
    }

    public void setGoldtransfer(String goldtransfer) {
        this.goldtransfer = goldtransfer;
    }

    @SerializedName("pname")
    @Expose
    String pname;

    @SerializedName("pva")
    @Expose
    String pva;

    @SerializedName("pweight")
    @Expose
    String pweight;

    @SerializedName("pimg")
    @Expose
    String pimg;

    @SerializedName("catid")
    @Expose
    String catid;


    @SerializedName("reviews")
    @Expose
    String reviews;

    @SerializedName("weight")
    @Expose
    String weight;

    @SerializedName("subcatname")
    @Expose
    String subcatname;

    @SerializedName("va")
    @Expose
    String va;

    @SerializedName("ratings")
    @Expose
    String ratings;


    @SerializedName("cid")
    @Expose
    String cid;


    @SerializedName("sid")
    @Expose
    String sid;


    @SerializedName("heading")
    @Expose
    String heading;

    @SerializedName("stylecount")
    @Expose
    String stylecount;


    @SerializedName("brand")
    @Expose
    String brand;


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }


    public String getStylecount() {
        return stylecount;
    }

    public void setStylecount(String stylecount) {
        this.stylecount = stylecount;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getVa() {
        return va;
    }

    public void setVa(String va) {
        this.va = va;
    }

    public String getSubcatname() {
        return subcatname;
    }

    public void setSubcatname(String subcatname) {
        this.subcatname = subcatname;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPva() {
        return pva;
    }

    public void setPva(String pva) {
        this.pva = pva;
    }

    public String getPweight() {
        return pweight;
    }

    public void setPweight(String pweight) {
        this.pweight = pweight;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    @SerializedName("catname")
    @Expose
    String catname;


    @SerializedName("image_uri")
    @Expose
    String image_uri;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @SerializedName("images")
    @Expose
    String images;

    @SerializedName("userid")
    @Expose
    String userid;

    @SerializedName("pids")
    @Expose
    String pids;

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    @SerializedName("verifyToken")
    @Expose
    String verifyToken;

    @SerializedName("notificationsCount")
    @Expose
    String notificationsCount;

    @SerializedName("is_referral")
    @Expose
    boolean is_referral;

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }


    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("gsPin")
    @Expose
    boolean Isgspin;

    @SerializedName("maskedPhone")
    @Expose
    String maskedPhone;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("mobile")
    @Expose
    String mobile;

    @SerializedName("password")
    @Expose
    String password;

    @SerializedName("confirmPassword")
    @Expose
    String cpassword;
    @SerializedName("roleType")
    @Expose
    String roletype;
    @SerializedName("roleId")
    @Expose
    String roleId;

    @SerializedName("verified")
    @Expose
    String otpverofy;
    @SerializedName("messageStatus")
    @Expose
    boolean messageStatus;
    @SerializedName("last2")
    @Expose
    String last2digits;
    @SerializedName("sequence")
    @Expose
    String sequence;
    @SerializedName("banner_uri")
    @Expose
    String banner_uri;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("purity")
    @Expose
    String gold_purity;
    @SerializedName("sell_price_per_gram")
    @Expose
    String sell_price_per_gram;
    @SerializedName("buy_price_per_gram")
    @Expose
    String buy_price_per_gram;
    @SerializedName("carat")
    @Expose
    String gold_type;
    @SerializedName("processed")
    @Expose
    boolean purchase_Status;

    @SerializedName("reference_id")
    @Expose
    String transactionId;
    @SerializedName("transactionId")
    @Expose
    String transaction_Id;

    @SerializedName("created_at")
    @Expose
    String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @SerializedName("updated_at")
    @Expose
    String updated_at;

    @SerializedName("statustext")
    @Expose
    String statustext;

    public String getStatustext() {
        return statustext;
    }

    public void setStatustext(String statustext) {
        this.statustext = statustext;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }


    @SerializedName("closing_grams_balance")
    @Expose
    String total_balance;
    @SerializedName("cashBackGstAmount")
    @Expose
    String cashBackGstAmount;

    @SerializedName("amount")
    @Expose
    String parchase_amount;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("role_id")
    @Expose
    String role_id;

    @SerializedName("current_page")
    @Expose
    String current_page;

    @SerializedName("desc")
    @Expose
    String desc;

    @SerializedName("txn_type")
    @Expose
    String txn_type;
    @SerializedName("tokenType")
    @Expose
    String tokenType;


    public String getProductliveprice() {
        return productliveprice;
    }

    public void setProductliveprice(String productliveprice) {
        this.productliveprice = productliveprice;
    }

    @SerializedName("liveprice")
    @Expose
    String productliveprice;

    @SerializedName("liveprice24")
    @Expose
    String liveprice24;


    public String getLiveprice24() {
        return liveprice24;
    }

    public void setLiveprice24(String liveprice24) {
        this.liveprice24 = liveprice24;
    }

    @SerializedName("type")
    @Expose
    String transction_type;

    @SerializedName("grams")
    @Expose
    String grams;

    @SerializedName("from")
    @Expose
    JsonElement from;

    @SerializedName("to")
    @Expose
    JsonElement to;


    @SerializedName("question")
    @Expose
    String question;
    @SerializedName("answers")
    @Expose
    String answers;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("city")
    @Expose
    String city;
    @SerializedName("state_id")
    @Expose
    String state_id;
    @SerializedName("zip")
    @Expose
    String zip;
    @SerializedName("name_on_account")
    @Expose
    String name_on_account;
    @SerializedName("account_number")
    @Expose
    String account_number;
    @SerializedName("bank_name")
    @Expose
    String bank_name;
    @SerializedName("bank_branch")
    @Expose
    String bank_branch;
    @SerializedName("ifsc_code")
    @Expose
    String ifsc_code;
    @SerializedName("quantity")
    @Expose
    String quantity;
    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("humanReadable")
    @Expose
    String humanReadable;

    @SerializedName("currentPassword")
    @Expose
    String currentPassword;

    @SerializedName("newPassword")
    @Expose
    String newPassword;

    @SerializedName("confirmNewPassword")
    @Expose
    String confirmNewPassword;

    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("gender")
    @Expose
    String gender;
    @SerializedName("father_name")
    @Expose
    String father_name;
    @SerializedName("spouse_name")
    @Expose
    String spouse_name;
    @SerializedName("alternate_phone")
    @Expose
    String alternate_phone;
    @SerializedName("pan_card")
    @Expose
    String pan_card;
    @SerializedName("aadhaar_card")
    @Expose
    String aadhaar_card;

    @SerializedName("country_id")
    @Expose
    String country_id;

    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("relation")
    @Expose
    String relation;

    @SerializedName("content")
    @Expose
    String content;
    @SerializedName("scheme_content")
    @Expose
    String scheme_content;
    @SerializedName("taxPercentage")
    @Expose
    String taxPercentage;
    @SerializedName("isTerminated")
    @Expose
    boolean isTerminated;
    @SerializedName("scheme_terms_and_conditions")
    @Expose
    String scheme_terms_and_conditions;
    @SerializedName("features")
    @Expose
    String features;
    @SerializedName("explanation")
    @Expose
    String explanation;
    @SerializedName("questions")
    @Expose
    String questions;
    @SerializedName("finalAmount")
    @Expose
    String finalAmount;
    @SerializedName("emiAmount")
    @Expose
    String emiAmount;
    @SerializedName("cashbackAmount")
    @Expose
    String cashbackAmount;
    @SerializedName("gst")
    @Expose
    String gst;
    @SerializedName("tenure")
    @Expose
    String tenure;
    @SerializedName("charge_date")
    @Expose
    String charge_date;
    @SerializedName("emi_grams")
    @Expose
    String emi_grams;
    @SerializedName("gramsEmi")
    @Expose
    String gramsEmi;
    @SerializedName("total_installments")
    @Expose
    String total_installments;

    @SerializedName("processingFee")
    @Expose
    String processingFee;
    @SerializedName("createdDate")
    @Expose
    String createdDate;


    @SerializedName("scheme_title")
    @Expose
    JsonElement Scheme_title;

    @SerializedName("user_transactions")
    @Expose
    JsonElement user_transactions;

    @SerializedName("screen_uri")
    @Expose
    String screen_uri;

    @SerializedName("SubCategories")
    @Expose
    JsonElement SubCategories;

    @SerializedName("isSubCategories")
    @Expose
    String isSubCategories;

    @SerializedName("amountDetails")
    @Expose
    JsonElement amountDetails;
    @SerializedName("scheme_calculation_type")
    @Expose
    String scheme_calculation_type;

    @SerializedName("product_uri")
    @Expose
    String product_uri;

    @SerializedName("productCount")
    @Expose
    String productCount;

    @SerializedName("paid_installments")
    @Expose
    String paid_installments;

    @SerializedName("pending_installments")
    @Expose
    String pending_installments;

    @SerializedName("processing_fee")
    @Expose
    String processing_fee;


    @SerializedName("source")
    @Expose
    String source;

    @SerializedName("emiDate")
    @Expose
    String emiDate;


    @SerializedName("emi_date")
    @Expose
    String emi_date;


    @SerializedName("category_uri")
    @Expose
    String category_uri;

    @SerializedName("description")
    @Expose
    String description;


    @SerializedName("final_amount")
    @Expose
    String final_amount;

    @SerializedName("price_predicted")
    @Expose
    String price_predicted;

    @SerializedName("is_matched")
    @Expose
    String is_matched;

    @SerializedName("tc")
    @Expose
    String tc;
    @SerializedName("updatedDate")
    @Expose
    String updatedDate;

    @SerializedName("totalAmount")
    @Expose
    String totalAmount;


    @SerializedName("price")
    @Expose
    String price;

    @SerializedName("data")
    @Expose
    String data;

    @SerializedName("order_id")
    @Expose
    String order_id;

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    @SerializedName("totalprice")
    @Expose
    String totalprice;

    @SerializedName("subContent")
    @Expose
    String subContent;


    @SerializedName("explanation_value")
    @Expose
    String explanation_value;


    @SerializedName("paragraph")
    @Expose
    String paragraph;

    @SerializedName("status")
    @Expose
    String status;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @SerializedName("size")
    @Expose
    String size;

    @SerializedName("category_name")
    @Expose
    JsonElement category_name;


    ////////////////////

    /////////////


    @SerializedName("sizes")
    ArrayList<Sizes> sizes = new ArrayList<Sizes>();

    public ArrayList<Sizes> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Sizes> sizes) {
        this.sizes = sizes;
    }

    public String getStoneprice() {
        return stoneprice;
    }

    public void setStoneprice(String stoneprice) {
        this.stoneprice = stoneprice;
    }

    @SerializedName("stoneprice")
    private String stoneprice;

    @SerializedName("stonename")
    private String stonename;
    @SerializedName("stonecolor")
    private String stonecolor;
    @SerializedName("vaprice")
    private String vaprice;
    @SerializedName("gstprice")
    private String gstprice;

    public String getStonename() {
        return stonename;
    }

    public void setStonename(String stonename) {
        this.stonename = stonename;
    }

    public String getStonecolor() {
        return stonecolor;
    }

    public void setStonecolor(String stonecolor) {
        this.stonecolor = stonecolor;
    }

    public String getVaprice() {
        return vaprice;
    }

    public void setVaprice(String vaprice) {
        this.vaprice = vaprice;
    }

    public String getGstprice() {
        return gstprice;
    }

    public void setGstprice(String gstprice) {
        this.gstprice = gstprice;
    }


    @SerializedName("update")
    @Expose
    String update;

    @SerializedName("is_primary")
    @Expose
    String is_primary;

    @SerializedName("livePrice")
    @Expose
    String livePrice;
    @SerializedName("gender_name")
    @Expose
    String gender_name;

    @SerializedName("avatar")
    @Expose
    String avatar;

    @SerializedName("avatarImageLink")
    @Expose
    String avatarImageLink;

    @SerializedName("date")
    @Expose
    String date;

    @SerializedName("time")
    @Expose
    String time;

    @SerializedName("location")
    @Expose
    String location;
    @SerializedName("actions")
    @Expose
    boolean actions;
    @SerializedName("isKyc")
    @Expose
    boolean isKyc;
    @SerializedName("categoryImageLink")
    @Expose
    String categoryImageLink;

    @SerializedName("customerId")
    @Expose
    String customerId;

    @SerializedName("token")
    @Expose
    String token;

    @SerializedName("amountValue")
    @Expose
    String amountValue;

    public boolean isIs_referral() {
        return is_referral;
    }

    public void setIs_referral(boolean is_referral) {
        this.is_referral = is_referral;
    }

    public String getNotificationsCount() {
        return notificationsCount;
    }

    public void setNotificationsCount(String notificationsCount) {
        this.notificationsCount = notificationsCount;
    }

    public String getAmount_wallet() {
        return amountValue;
    }

    public void setAmount_wallet(String amount_wallet) {
        this.amountValue = amount_wallet;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIsgspin() {
        return Isgspin;
    }

    public void setIsgspin(Boolean isgspin) {
        Isgspin = isgspin;
    }


    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCategoryImageLink() {
        return categoryImageLink;
    }

    public void setCategoryImageLink(String categoryImageLink) {
        this.categoryImageLink = categoryImageLink;
    }

    public boolean isKyc() {
        return isKyc;
    }

    public void setKyc(boolean kyc) {
        isKyc = kyc;
    }

    public boolean isActions() {
        return actions;
    }

    public void setActions(boolean actions) {
        this.actions = actions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isTerminated() {
        return isTerminated;
    }

    public void setTerminated(boolean terminated) {
        isTerminated = terminated;
    }

    public String getAvatarImageLink() {
        return avatarImageLink;
    }

    public void setAvatarImageLink(String avatarImageLink) {
        this.avatarImageLink = avatarImageLink;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender_name() {
        return gender_name;
    }

    public void setGender_name(String gender_name) {
        this.gender_name = gender_name;
    }

    public String getLivePrice() {
        return livePrice;
    }

    public void setLivePrice(String livePrice) {
        this.livePrice = livePrice;
    }

    public String getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(String is_primary) {
        this.is_primary = is_primary;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }


    @SerializedName("balance")
    @Expose
    JSONObject balance;

    public String getTransaction_Id() {
        return transaction_Id;
    }

    public void setTransaction_Id(String transaction_Id) {
        this.transaction_Id = transaction_Id;
    }


    @SerializedName("product_name")
    @Expose
    JsonElement product_name;

    @SerializedName("total_amount")
    @Expose
    String total_amount;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    @SerializedName("product_details")
    @Expose
    JsonElement product_details;


    @SerializedName("state")
    @Expose
    JsonElement state;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public JsonElement getState() {
        return state;
    }

    public void setState(JsonElement state) {
        this.state = state;
    }

    public JsonElement getProduct_details() {
        return product_details;
    }

    public void setProduct_details(JsonElement product_details) {
        this.product_details = product_details;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonElement getCategory_name() {
        return category_name;
    }

    public void setCategory_name(JsonElement category_name) {
        this.category_name = category_name;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }


    public String getExplanation_value() {
        return explanation_value;
    }

    public void setExplanation_value(String explanation_value) {
        this.explanation_value = explanation_value;
    }


    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getIs_matched() {
        return is_matched;
    }

    public void setIs_matched(String is_matched) {
        this.is_matched = is_matched;
    }

    public String getPrice_predicted() {
        return price_predicted;
    }

    public void setPrice_predicted(String price_predicted) {
        this.price_predicted = price_predicted;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    public JsonElement getUser_transactions() {
        return user_transactions;
    }

    public void setUser_transactions(JsonElement user_transactions) {
        this.user_transactions = user_transactions;
    }

    public String getProcessing_fee() {
        return processing_fee;
    }

    public void setProcessing_fee(String processing_fee) {
        this.processing_fee = processing_fee;
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

    public String getCashBackGstAmount() {
        return cashBackGstAmount;
    }

    public void setCashBackGstAmount(String cashBackGstAmount) {
        this.cashBackGstAmount = cashBackGstAmount;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getScheme_calculation_type() {
        return scheme_calculation_type;
    }

    public void setScheme_calculation_type(String scheme_calculation_type) {
        this.scheme_calculation_type = scheme_calculation_type;
    }

    public JsonElement getAmountDetails() {
        return amountDetails;
    }

    public void setAmountDetails(JsonElement amountDetails) {
        this.amountDetails = amountDetails;
    }

    public String getIsSubCategories() {
        return isSubCategories;
    }

    public void setIsSubCategories(String isSubCategories) {
        this.isSubCategories = isSubCategories;
    }

    public JsonElement getSubCategories() {
        return SubCategories;
    }

    public void setSubCategories(JsonElement subCategories) {
        SubCategories = subCategories;
    }


    public String getGramsEmi() {
        return gramsEmi;
    }

    public void setGramsEmi(String gramsEmi) {
        this.gramsEmi = gramsEmi;
    }

    public String getScreen_uri() {
        return screen_uri;
    }

    public void setScreen_uri(String screen_uri) {
        this.screen_uri = screen_uri;
    }

    public JsonElement getScheme_title() {
        return Scheme_title;
    }

    public void setScheme_title(JsonElement scheme_title) {
        Scheme_title = scheme_title;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(String processingFee) {
        this.processingFee = processingFee;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public String getCharge_date() {
        return charge_date;
    }

    public void setCharge_date(String charge_date) {
        this.charge_date = charge_date;
    }

    public String getEmi_grams() {
        return emi_grams;
    }

    public void setEmi_grams(String emi_grams) {
        this.emi_grams = emi_grams;
    }

    public String getTotal_installments() {
        return total_installments;
    }

    public void setTotal_installments(String total_installments) {
        this.total_installments = total_installments;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(String emiAmount) {
        this.emiAmount = emiAmount;
    }

    public String getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(String cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScheme_content() {
        return scheme_content;
    }

    public void setScheme_content(String scheme_content) {
        this.scheme_content = scheme_content;
    }


    public String getScheme_terms_and_conditions() {
        return scheme_terms_and_conditions;
    }

    public void setScheme_terms_and_conditions(String scheme_terms_and_conditions) {
        this.scheme_terms_and_conditions = scheme_terms_and_conditions;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getSpouse_name() {
        return spouse_name;
    }

    public void setSpouse_name(String spouse_name) {
        this.spouse_name = spouse_name;
    }

    public String getAlternate_phone() {
        return alternate_phone;
    }

    public void setAlternate_phone(String alternate_phone) {
        this.alternate_phone = alternate_phone;
    }

    public String getPan_card() {
        return pan_card;
    }

    public void setPan_card(String pan_card) {
        this.pan_card = pan_card;
    }

    public String getAadhaar_card() {
        return aadhaar_card;
    }

    public void setAadhaar_card(String aadhaar_card) {
        this.aadhaar_card = aadhaar_card;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getHumanReadable() {
        return humanReadable;
    }

    public void setHumanReadable(String humanReadable) {
        this.humanReadable = humanReadable;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName_on_account() {
        return name_on_account;
    }

    public void setName_on_account(String name_on_account) {
        this.name_on_account = name_on_account;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(String bank_branch) {
        this.bank_branch = bank_branch;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmi_date() {
        return emi_date;
    }

    public void setEmi_date(String emi_date) {
        this.emi_date = emi_date;
    }

    public JSONObject getBalance() {
        return balance;
    }

    public void setBalance(JSONObject balance) {
        this.balance = balance;
    }

    public boolean isPurchase_Status() {
        return purchase_Status;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTransction_type() {
        return transction_type;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public void setTransction_type(String transction_type) {
        this.transction_type = transction_type;
    }

    public JsonElement getTo() {
        return to;
    }

    public void setTo(JsonElement to) {
        this.to = to;
    }

    public JsonElement getFrom() {
        return from;
    }

    public void setFrom(JsonElement from) {
        this.from = from;
    }

    public String getTxn_type() {
        return txn_type;
    }

    public void setTxn_type(String txn_type) {
        this.txn_type = txn_type;
    }


    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

    public String getParchase_amount() {
        return parchase_amount;
    }

    public void setParchase_amount(String parchase_amount) {
        this.parchase_amount = parchase_amount;
    }


    public boolean getPurchase_Status() {
        return purchase_Status;
    }

    public void setPurchase_Status(boolean purchase_Status) {
        this.purchase_Status = purchase_Status;
    }

    public String getGold_type() {
        return gold_type;
    }

    public void setGold_type(String gold_type) {
        this.gold_type = gold_type;
    }


    public String getGold_purity() {
        return gold_purity;
    }

    public void setGold_purity(String gold_purity) {
        this.gold_purity = gold_purity;
    }

    public String getSell_price_per_gram() {
        return sell_price_per_gram;
    }

    public void setSell_price_per_gram(String sell_price_per_gram) {
        this.sell_price_per_gram = sell_price_per_gram;
    }

    public String getBuy_price_per_gram() {
        return buy_price_per_gram;
    }

    public void setBuy_price_per_gram(String buy_price_per_gram) {
        this.buy_price_per_gram = buy_price_per_gram;
    }


    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getBanner_uri() {
        return banner_uri;
    }

    public void setBanner_uri(String banner_uri) {
        this.banner_uri = banner_uri;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMessageStatus() {
        return messageStatus;
    }

    public String getLast2digits() {
        return last2digits;
    }

    public void setLast2digits(String last2digits) {
        this.last2digits = last2digits;
    }


    public boolean getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(boolean messageStatus) {
        this.messageStatus = messageStatus;
    }


    public String getOtpverofy() {
        return otpverofy;
    }

    public void setOtpverofy(String otpverofy) {
        this.otpverofy = otpverofy;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaskedPhone() {
        return maskedPhone;
    }

    public void setMaskedPhone(String maskedPhone) {
        this.maskedPhone = maskedPhone;
    }

    public JsonElement getProduct_name() {
        return product_name;
    }

    public void setProduct_name(JsonElement product_name) {
        this.product_name = product_name;
    }


    @SerializedName("category")
    @Expose
    String category;

    @SerializedName("image")
    @Expose
    String image;

    @SerializedName("store_start_time")
    @Expose
    String store_start_time;

    @SerializedName("store_closing_time")
    @Expose
    String store_closing_time;

    public String getStore_start_time() {
        return store_start_time;
    }

    public void setStore_start_time(String store_start_time) {
        this.store_start_time = store_start_time;
    }

    public String getStore_closing_time() {
        return store_closing_time;
    }

    public void setStore_closing_time(String store_closing_time) {
        this.store_closing_time = store_closing_time;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    @SerializedName("store_name")
    @Expose
    String store_name;

    @SerializedName("store_description1")
    @Expose
    String store_description1;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @SerializedName("tags")
    @Expose
    String tags;

    @SerializedName("latitude")
    @Expose
    String latitude;


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @SerializedName("longitude")
    @Expose
    String longitude;

    @SerializedName("rating")
    @Expose
    String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isIsgspin() {
        return Isgspin;
    }

    public void setIsgspin(boolean isgspin) {
        Isgspin = isgspin;
    }

    public String getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(String amountValue) {
        this.amountValue = amountValue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStore_description1() {
        return store_description1;
    }

    public void setStore_description1(String store_description1) {
        this.store_description1 = store_description1;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGift_card() {
        return gift_card;
    }

    public void setGift_card(String gift_card) {
        this.gift_card = gift_card;
    }

    @SerializedName("gift_card")
    @Expose
    String gift_card;

    @SerializedName("store_open_timings")
    @Expose
    String store_open_timings;

    @SerializedName("store_close_timings")
    @Expose
    String store_close_timings;

    public String getStore_open_timings() {
        return store_open_timings;
    }

    public void setStore_open_timings(String store_open_timings) {
        this.store_open_timings = store_open_timings;
    }

    public String getStore_close_timings() {
        return store_close_timings;
    }

    public void setStore_close_timings(String store_close_timings) {
        this.store_close_timings = store_close_timings;
    }


    @SerializedName("stones")
    private List<Stones> stones = new ArrayList<>();


    public List<Stones> getStones() {
        return stones;
    }

    public void setStones(List<Stones> stones) {
        this.stones = stones;
    }


    public String getGiftAmount() {
        return GiftAmount;
    }

    public void setGiftAmount(String giftAmount) {
        GiftAmount = giftAmount;
    }

    @SerializedName("GiftAmount")
    @Expose
    String GiftAmount;

    /* public String getCarat() {
         return carat;
     }

     public void setCarat(String carat) {
         this.carat = carat;
     }

     @SerializedName("carat")
     @Expose
     String carat;*/
    @SerializedName("review")
    @Expose
    String review;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


    @SerializedName("age_of_store")
    @Expose
    String age_of_store;

    public String getAge_of_store() {
        return age_of_store;
    }

    public void setAge_of_store(String age_of_store) {
        this.age_of_store = age_of_store;
    }
}
