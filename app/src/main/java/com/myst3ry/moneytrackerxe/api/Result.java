package com.myst3ry.moneytrackerxe.api;

import android.text.TextUtils;

public class Result { //result for removing operation

    private String status;

    public boolean isSuccessful() {
        return TextUtils.equals(status, "success");
    }

}
