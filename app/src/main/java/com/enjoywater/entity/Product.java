package com.enjoywater.entity;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("price")
    private int price;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    private boolean selected = false;
    private int count = 1;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
