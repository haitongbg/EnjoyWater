package com.enjoywater.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TONG HAI on 3/21/2018.
 */

public class UserInfo {
    @SerializedName("role_name")
    private String role_name;
    @SerializedName("role")
    private int role;
    @SerializedName("fulname")
    private String fulname;
    @SerializedName("id")
    private int id;
    @SerializedName("token")
    private String token;

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFulname() {
        return fulname;
    }

    public void setFulname(String fulname) {
        this.fulname = fulname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
