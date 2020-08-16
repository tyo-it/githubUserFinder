package com.tyo.githubuser.api

import androidx.paging.PagingSource
import com.tyo.githubuser.repository.User
import retrofit2.HttpException
import java.io.IOException

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubPagingSource(
    private val service: GithubService,
    private val query: String) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val userQuery = query
        return try {
            val response = service.searchUser(userQuery, position, params.loadSize)
            val users = response.items
            LoadResult.Page(
                data = toUser(users),
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (users.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private fun toUser(items: List<UserResponse>): List<User> {
        return items.map {
            User(
                it.login,
                it.avatarUrl
            )
        }
    }
}