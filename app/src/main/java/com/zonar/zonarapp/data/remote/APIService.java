package com.zonar.zonarapp.data.remote;


import com.zonar.zonarapp.data.model.GetData;
import com.zonar.zonarapp.data.model.GetToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

/**
 * Created by Chike on 12/3/2016.
 */

public interface APIService {

    @POST("/users/login")
    @FormUrlEncoded
    Call<GetToken> login(@Field("username") String username,
                         @Field("password") String password);

    @POST("/users/register")
    @FormUrlEncoded
    Call<GetToken> register(@Field("username") String reg_username,
                            @Field("password1") String reg_password,
                            @Field("password2") String confirm_password);


    @GET("user_data/user_data")
    Call<GetData> get(@Header("authorization") String token);

    @PATCH("user_data/user_data")
    @FormUrlEncoded
    Call<GetData> modify(@Header("authorization") String token,
                         @Field("name") String mod_name,
                         @Field("Gender") String mod_gender,
                         @Field("Birthday") String mod_birthday,
                         @Field("E_mail") String mod_email,
                         @Field("data1") String mod_data1);

/*
    @POST("/posts")
    Call<GetToken> savePost(@Body GetToken post);

    @PUT("/posts/{id}")
    @FormUrlEncoded
    Call<GetToken> updatePost(@Path("id") long id,
                          @Field("title") String title,
                          @Field("body") String body,
                          @Field("userId") long userId);

    @DELETE("/posts/{id}")
    Call<GetToken> deletePost(@Path("id") long id);
    */

}

