package com.kotlin.retrofit2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceHolderApi {

    @GET("/posts")
    fun getData(): Call<Post>

    @GET("/posts/{id}")
    fun getSingleData(@Path("id") id: String): Call<PostSingleData>
}