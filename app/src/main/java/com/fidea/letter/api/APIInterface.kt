package com.fidea.letter.api

import com.fidea.letter.models.Item
import com.fidea.letter.models.Token
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
        @Field("username") username: String?, @Field("password") password: String?,
        @Field("email") email: String?
    ): Call<User>?

    @POST("token")
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
    fun getContent(
    ): Call<ArrayList<Item>>?

    @GET("favorites")
    fun getFavorites(
    ): Call<ArrayList<Item>>?


}