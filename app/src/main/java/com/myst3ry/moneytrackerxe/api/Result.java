package com.myst3ry.moneytrackerxe.api;

import android.text.TextUtils;

public class Result { //base result class for any operation

    private String status;

    public boolean isSuccessful() {
        return TextUtils.equals(status, "success");
    }

    public String getStatus() {
        return status;
    }
}
