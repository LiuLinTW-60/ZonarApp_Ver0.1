package com.zonar.zonarapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetToken {

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Token " + token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetToken post = (GetToken) o;
        return token != null ? token.equals(post.token) : post.token == null;

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        return result;
    }
}
