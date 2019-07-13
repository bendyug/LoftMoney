package com.dbendyug.loftmoney;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoftApi {

    @GET("auth")
    Call<AuthResponse> auth(@Query("social_user_id") String userId);

    @GET("items")
    Call<List<Item>> getItems(@Query("type") String type, @Query("auth-token") String authToken);

    @POST("items/add")
    Call<UserId> addItems(@Body AddItemRequest addItemRequest, @Query("auth-token") String authToken);

    @POST("items/remove")
    Call<UserId> deleteItem(@Query("id") int id, @Query("auth-token") String authToken);
}
