package com.goldsikka.goldsikka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MainModuleModel {


    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("module_type")
    @Expose
    String module_type;
    @SerializedName("mobile_icon")
    @Expose
    String mobile_icon;


    @SerializedName("modules")
    private List<MainModuleModel> modules = new ArrayList<>();

    public List<MainModuleModel> getModules() {
        return modules;
    }

    public void setModules(List<MainModuleModel> modules) {
        this.modules = modules;
    }
//    @SerializedName("status")
//    @Expose
//    String status;


    public String getMobile_icon() {
        return mobile_icon;
    }

    public void setMobile_icon(String mobile_icon) {
        this.mobile_icon = mobile_icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule_type() {
        return module_type;
    }

    public void setModule_type(String module_type) {
        this.module_type = module_type;
    }
}
