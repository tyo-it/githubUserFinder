package com.tyo.githubuser.repository

import com.google.gson.annotations.SerializedName

class SearchResult(@SerializedName("items") val items: List<Item>)

class Item(@SerializedName("login") val login: String,
           @SerializedName("avatar_url") val avatarUrl: String)