package com.goldsikka.goldsikka.Activitys.Events;

import com.goldsikka.goldsikka.Models.PassBookModel;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventModel {


    @SerializedName("data")
    private List<EventModel> result = new ArrayList<>();

    public List<EventModel> getResult() {
        return result;
    }

    public void setResult(List<EventModel> result) {
        this.result = result;
    }

    @SerializedName("userDetails")
    @Expose
    JsonElement userDetails;

    public JsonElement getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(JsonElement user_name) {
        this.userDetails = user_name;
    }

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("event_name")
    @Expose
    String event_name;
    @SerializedName("event_date")
    @Expose
    String event_date;

    @SerializedName("eventStatus")
    @Expose
    String eventStatus;

    @SerializedName("created_date")
    @Expose
    String created_date;

    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("avatar")
    @Expose
    String avatar;



    @SerializedName("rating")
    @Expose
    String rating;



    @SerializedName("event_id")
    @Expose
    String event_id;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("venue")
    @Expose
    String venue;

    @SerializedName("qr_code_hash")
    @Expose
    String qr_code_hash;
    @SerializedName("bride_name")
    @Expose
    String bride_name;
    @SerializedName("groom_name")
    @Expose
    String groom_name;
    @SerializedName("holder_name")
    @Expose
    String holder_name;

    @SerializedName("eventDetails")
    @Expose
    JsonElement eventDetails;
    @SerializedName("event")
    @Expose
    JsonElement event;
    @SerializedName("event_type")
    @Expose
    String event_type;

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("photo")
    @Expose
    String photo;
    @SerializedName("wedding_card_photo")
    @Expose
    String wedding_card_photo;
    @SerializedName("muhurtham_time")
    @Expose
    String muhurtham_time;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("photoImageLink")
    @Expose
    String photoImageLink;
    @SerializedName("weddingCardPhotoLink")
    @Expose
    String weddingCardPhotoLink;

    @SerializedName("processed")
    @Expose
    boolean processed;
    @SerializedName("errors")
    @Expose
    JsonElement errors;

    @SerializedName("transactionId")
    @Expose
    String transactionId;

    @SerializedName("transaction_id")
    @Expose
    String transaction_id;

    @SerializedName("gender")
    @Expose
    String gender;
    @SerializedName("isUpdate")
    @Expose
    boolean isUpdate;
    @SerializedName("adminStatus")
    @Expose
    String adminStatus;

    @SerializedName("isQR")
    @Expose
    boolean isQR;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("current_page")
    @Expose
    int current_page;
    @SerializedName("last_page")
    @Expose
    int last_page;
    @SerializedName("eventName")
    @Expose
    String eventName;
    @SerializedName("guest_mobile")
    @Expose
    String guest_mobile;
    @SerializedName("desc")
    @Expose
    String desc;
    @SerializedName("other_event_type")
    @Expose
    String other_event_type;

    @SerializedName("avatarImageLink")
    @Expose
    String avatarImageLink;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("isSeen")
    @Expose
    boolean isSeen;

    @SerializedName("image_uri")
    @Expose
    String image_uri;

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }



    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("created_at")
    @Expose
    String created_at;

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarImageLink() {
        return avatarImageLink;
    }

    public void setAvatarImageLink(String avatarImageLink) {
        this.avatarImageLink = avatarImageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOther_event_type() {
        return other_event_type;
    }

    public void setOther_event_type(String other_event_type) {
        this.other_event_type = other_event_type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getGuest_mobile() {
        return guest_mobile;
    }

    public void setGuest_mobile(String guest_mobile) {
        this.guest_mobile = guest_mobile;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isQR() {
        return isQR;
    }

    public void setQR(boolean QR) {
        isQR = QR;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public JsonElement getErrors() {
        return errors;
    }

    public void setErrors(JsonElement errors) {
        this.errors = errors;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public JsonElement getEvent() {
        return event;
    }

    public void setEvent(JsonElement event) {
        this.event = event;
    }

    public String getPhotoImageLink() {
        return photoImageLink;
    }

    public void setPhotoImageLink(String photoImageLink) {
        this.photoImageLink = photoImageLink;
    }

    public String getWeddingCardPhotoLink() {
        return weddingCardPhotoLink;
    }

    public void setWeddingCardPhotoLink(String weddingCardPhotoLink) {
        this.weddingCardPhotoLink = weddingCardPhotoLink;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getWedding_card_photo() {
        return wedding_card_photo;
    }

    public void setWedding_card_photo(String wedding_card_photo) {
        this.wedding_card_photo = wedding_card_photo;
    }

    public String getMuhurtham_time() {
        return muhurtham_time;
    }

    public void setMuhurtham_time(String muhurtham_time) {
        this.muhurtham_time = muhurtham_time;
    }

    public JsonElement getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(JsonElement eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getQr_code_hash() {
        return qr_code_hash;
    }

    public void setQr_code_hash(String qr_code_hash) {
        this.qr_code_hash = qr_code_hash;
    }

    public String getBride_name() {
        return bride_name;
    }

    public void setBride_name(String bride_name) {
        this.bride_name = bride_name;
    }

    public String getGroom_name() {
        return groom_name;
    }

    public void setGroom_name(String groom_name) {
        this.groom_name = groom_name;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }
}
