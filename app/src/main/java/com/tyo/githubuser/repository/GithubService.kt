package com.tyo.githubuser.repository

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    fun searchUser(@Query("q") name: String): Single<SearchResult>
}