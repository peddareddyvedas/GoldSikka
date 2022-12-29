package com.goldsikka.goldsikka.Fragments.Ecommerce.Adapters;

public class Underbudgetmodel {

    private String title;


    private String status;
    private int imageUrl;


    public Underbudgetmodel() {
    }

    public Underbudgetmodel(String title, String stat, int image) {
        this.title = title;
        this.status = stat;
        this.imageUrl = image;

    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}

