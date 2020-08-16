package com.tyo.githubuser.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): UserSearchResponse
}