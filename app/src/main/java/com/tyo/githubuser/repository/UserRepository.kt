package com.tyo.githubuser.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tyo.githubuser.api.GithubPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val githubService: GithubService) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    fun getSearchResultStream(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(githubService, query) }
        ).flow
    }
}