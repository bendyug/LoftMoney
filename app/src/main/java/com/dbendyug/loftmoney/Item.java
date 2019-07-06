package com.dbendyug.loftmoney;

import com.google.gson.annotations.SerializedName;

public class Item {

    private String name;
    private int price;

    @SerializedName("created_at")
    private String createdAt;

    public Item(String name, int price, String createdAt) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
