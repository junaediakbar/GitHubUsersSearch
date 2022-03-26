package com.juned.githubuserssearch.api

import com.juned.githubuserssearch.BuildConfig
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Authorization:" + BuildConfig.API_KEY)
    @GET("search/users")
    fun getUsers(@Query("q") keyword:String,
               @Query("per_page") per_page:Int=15,
               @Query("page") page:Int=1):Call<UsersResponse>?

    @Headers("Authorization:" + BuildConfig.API_KEY)
    @GET("/users/{username}")
    fun getUserDetail(@Path("username") username:String) : Call<UserDetailsResponse>?

    @Headers("Authorization:" + BuildConfig.API_KEY)
    @GET("users/{username}/following")
    fun getFollowingUser(@Path("username") username:String):Call<List<ListUsersResponse>>?

    @Headers("Authorization:" + BuildConfig.API_KEY)
    @GET("users/{username}/followers")
    fun getFollowersUser(@Path("username") username:String):Call<List<ListUsersResponse>>?

}