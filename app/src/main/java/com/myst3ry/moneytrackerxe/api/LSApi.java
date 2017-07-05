package com.myst3ry.moneytrackerxe.api;

import com.myst3ry.moneytrackerxe.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LSApi {

    @Headers("Content-Type: application/json")
    @GET("items")
    Call<List<Item>> items(@Query("type") String type);

    @Headers("Content-Type: application/json")
    @POST("items/add")
    Call<AddingResult> add(@Query("name") String article, @Query("price") int amount, @Query("type") String type);

    @Headers("Content-Type: application/json")
    @POST("items/remove")
    Call<Result> remove(@Query("id") int id);
}
