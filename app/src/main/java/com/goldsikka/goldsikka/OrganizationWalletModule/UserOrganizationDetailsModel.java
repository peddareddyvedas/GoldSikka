package com.goldsikka.goldsikka.OrganizationWalletModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserOrganizationDetailsModel {

    @SerializedName("data")
    private List<UserOrganizationDetailsModel> result = new ArrayList<>();

    @SerializedName("current_page")
    int current_page;

    @SerializedName("last_page")
    int last_page;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("organization_id")
    @Expose
    String organization_id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("registration_number")
    @Expose
    String registration_number;

    @SerializedName("address")
    @Expose
    String address;

    @SerializedName("city")
    @Expose
    String city;

    @SerializedName("state")
    @Expose
    String state;

    @SerializedName("role_id")
    @Expose
    String role_id;

    @SerializedName("zip")
    @Expose
    String zip;

    @SerializedName("photo")
    @Expose
    String photo;

    @SerializedName("registration_photo")
    @Expose
    String registration_photo;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("update_status")
    @Expose
    String update_status;

    @SerializedName("created_at")
    @Expose
    String created_at;

    @SerializedName("updated_at")
    @Expose
    String updated_at;

    @SerializedName("organizationStatus")
    @Expose
    String organizationStatus;

    @SerializedName("note")
    @Expose
    String note;

    @SerializedName("typeName")
    @Expose
    String typeName;

    @SerializedName("photoImageLink")
    @Expose
    String photoImageLink;

    @SerializedName("stateName")
    @Expose
    String stateName;

    @SerializedName("registrationPhotoImageLink")
    @Expose
    String registrationPhotoImageLink;

    public List<UserOrganizationDetailsModel> getResult() {
        return result;
    }

    public void setResult(List<UserOrganizationDetailsModel> result) {
        this.result = result;
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

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRegistration_photo() {
        return registration_photo;
    }

    public void setRegistration_photo(String registration_photo) {
        this.registration_photo = registration_photo;
    }

    public String getOrganizationStatus() {
        return organizationStatus;
    }

    public void setOrganizationStatus(String organizationStatus) {
        this.organizationStatus = organizationStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(String update_status) {
        this.update_status = update_status;
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



    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPhotoImageLink() {
        return photoImageLink;
    }

    public void setPhotoImageLink(String photoImageLink) {
        this.photoImageLink = photoImageLink;
    }

    public String getRegistrationPhotoImageLink() {
        return registrationPhotoImageLink;
    }

    public void setRegistrationPhotoImageLink(String registrationPhotoImageLink) {
        this.registrationPhotoImageLink = registrationPhotoImageLink;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
