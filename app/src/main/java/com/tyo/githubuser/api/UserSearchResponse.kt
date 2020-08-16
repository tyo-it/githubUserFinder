package com.tyo.githubuser.api

import com.google.gson.annotations.SerializedName

class UserSearchResponse(
    @SerializedName("items") val items: List<UserResponse>,
    val nextPage: Int? = null
)

class UserResponse(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String)