package com.myst3ry.moneytrackerxe.api;


public class AuthResult extends Result { //result for Google Sign-In operation

    private int userId;
    private String authToken;

    public int getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }
}
