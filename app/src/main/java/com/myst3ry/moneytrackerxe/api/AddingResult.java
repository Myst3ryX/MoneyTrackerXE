package com.myst3ry.moneytrackerxe.api;

import android.text.TextUtils;

public class AddingResult { //result for adding operation

    private String status;

    public boolean isAddingSuccessful() {
        return TextUtils.equals(status, "success");
    }

}
