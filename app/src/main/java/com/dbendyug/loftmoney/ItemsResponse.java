package com.dbendyug.loftmoney;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemsResponse {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItemsData() {
        return itemsData;
    }

    public void setItemsData(List<Item> itemsData) {
        this.itemsData = itemsData;
    }

    @SerializedName("data")
    private List<Item> itemsData;
}
