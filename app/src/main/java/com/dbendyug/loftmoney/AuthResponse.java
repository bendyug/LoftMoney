package com.dbendyug.loftmoney;

import com.google.gson.annotations.SerializedName;

class AuthResponse {

    private int id;
    @SerializedName("auth_token")
    private String authToken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
