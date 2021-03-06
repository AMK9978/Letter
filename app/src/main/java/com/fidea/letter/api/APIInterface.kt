package com.fidea.letter.api

import com.fidea.letter.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    //AUTHENTICATION//////////////////////
    @POST("signup")
    @Multipart
    fun signUp(
        @Part("username") username: RequestBody, @Part("password") password: RequestBody,
        @Part("email") email: RequestBody,
        @Part user_image: MultipartBody.Part?
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


    @GET("board")
    fun getBoards(): Call<ArrayList<Board>>


    @GET("user")
    fun getProfile(): Call<User>?

    @POST("search")
    fun search(): Call<ArrayList<Item>>?


}