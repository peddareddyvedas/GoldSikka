package com.goldsikka.goldsikka.Fragments.JewelleryInventory;

public class Wislistmodel {

    private String title;
    private String grams;
    private String vat;
    private String maintitle;
    private int imgId;
    private int progresspercent;

    String id, pids,  userid;

    public Wislistmodel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Wislistmodel(String title, String grams, String vat, String maintitle, int imgId, int progresspercent, String id, String pids, String userid) {
        this.title = title;
        this.grams = grams;
        this.vat = vat;
        this.maintitle = maintitle;
        this.imgId = imgId;
        this.progresspercent = progresspercent;
        this.id = id;
        this.pids = pids;
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }


    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public String getMaintitle() {
        return maintitle;
    }

    public void setMaintitle(String maintitle) {
        this.maintitle = maintitle;
    }

    public int getProgresspercent() {
        return progresspercent;
    }

    public void setProgresspercent(int progresspercent) {
        this.progresspercent = progresspercent;
    }
}

