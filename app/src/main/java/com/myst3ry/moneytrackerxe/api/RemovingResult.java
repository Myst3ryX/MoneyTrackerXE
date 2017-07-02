package com.myst3ry.moneytrackerxe.api;

import android.text.TextUtils;

public class RemovingResult { //result for removing operation

    private String status;

    public boolean isRemovingSuccessful() {
        return TextUtils.equals(status, "success");
    }

}
