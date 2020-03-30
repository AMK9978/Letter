package com.fidea.letter.api

import com.fidea.letter.models.Item
import com.fidea.letter.models.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {

    //AUTHENTICATION//////////////////////
    @POST("signUp")
    @FormUrlEncoded
    fun signUp(
        @Field("name") name: String?, @Field("password") password: String?,
        @Field("email") email: String?,
        @Field("mood") mood: String?
    ): Call<User>?

    @POST("login")
    @FormUrlEncoded
    fun signUp(
        @Field("name") name: String?, @Field("password") password: String?
    ): Call<User>?


    @POST("like")
    @FormUrlEncoded
    fun like(
        @Field("id") id: Int?
    ): Call<String>?


    @POST("visit")
    @FormUrlEncoded
    fun visit(
        @Field("id") id: Int?
    ): Call<String>?

    @POST("seen")
    @FormUrlEncoded
    fun setAsSeen(
        @Field("id") id: Int?
    ): Call<String>?


    @GET("content")
    fun getContent(
    ): Call<ArrayList<Item>>?


}