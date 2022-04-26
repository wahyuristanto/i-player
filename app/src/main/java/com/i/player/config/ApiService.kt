package com.i.player.config

import com.i.player.models.FeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("search?term=jack+johnson&entity=musicVideo")
    suspend fun fetchTopSongs(@Path("limit") limit: Int): Response<FeedResponse>

    @GET("search")
    suspend fun getSongs(
        @Query("term") term: String? = "",
        @Query("limit") limit: Int? = 15,
        ): FeedResponse
}