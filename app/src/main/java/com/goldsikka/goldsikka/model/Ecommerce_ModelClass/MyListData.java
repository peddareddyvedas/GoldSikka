package com.goldsikka.goldsikka.model.Ecommerce_ModelClass;

import com.goldsikka.goldsikka.model.Listmodel;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyListData {
    private String description;
    private int imgId;

    public MyListData(String description, int imgId) {
        this.description = description;
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

}
