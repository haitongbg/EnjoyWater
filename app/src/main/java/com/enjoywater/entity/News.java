package com.enjoywater.entity;

import com.google.gson.annotations.SerializedName;

public class News {
    @SerializedName("image")
    private String image;
    @SerializedName("summary")
    private String summary;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("detail")
    private String detail;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
