package com.enjoywater.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TONG HAI on 3/19/2018.
 */

public class BaseResponse {
    @SerializedName("errorName")
    private String errorName;
    @SerializedName("errorCode")
    private int errorCode;
    @SerializedName("message")
    private String message;
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private Object data;

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
