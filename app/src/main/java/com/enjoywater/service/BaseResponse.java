package com.enjoywater.service;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TONG HAI on 3/19/2018.
 */

public class BaseResponse {
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("success")
    private boolean success;
    @SerializedName("message ")
    private boolean message;
    @SerializedName("data")
    private Object data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
