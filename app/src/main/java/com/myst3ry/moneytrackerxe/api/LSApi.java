package com.myst3ry.moneytrackerxe.api;

import com.myst3ry.moneytrackerxe.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LSApi {

    @GET("auth")
    Call<AuthResult> auth(@Query("social_user_id") String socialUserId);

    @GET("logout")
    Call<Result> logout(); //will be implement as logout operation later

    @GET("balance")
    Call<BalanceResult> balance(); //will be implement as check balance operation later

    @GET("items")
    Call<List<Item>> items(@Query("type") String type);

    @POST("items/add")
    Call<OperationResult> add(@Query("price") int amount, @Query("name") String article, @Query("type") String type);

    @POST("items/remove")
    Call<OperationResult> remove(@Query("id") int id);
}
