package com.fidea.letter.api

import com.fidea.letter.models.Item
import com.fidea.letter.models.NotificationModel
import com.fidea.letter.models.Token
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    //AUTHENTICATION//////////////////////
    @POST("signup")
    @FormUrlEncoded
    fun signUp(
        @Field("username") username: String?, @Field("password") password: String?,
        @Field("email") email: String?
    ): Call<Temp>?

    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String?, @Field("password") password: String?
    ): Call<Token>?


    @POST("like")
    @FormUrlEncoded
    fun like(
        @Field("id") id: Int?
    ): Call<String>?

    @POST("seen")
    @FormUrlEncoded
    fun setAsSeen(
        @Field("id") id: Int?
    ): Call<String>?

    @GET("content")
    fun getContent(@Query("page") pageNumber: Int): Call<ArrayList<Item>>?

    @GET("favorites")
    fun getFavorites(
    ): Call<ArrayList<Item>>?


    @GET("notifications")
    fun getNotification(): Call<java.util.ArrayList<NotificationModel?>?>?


}