package com.tyo.githubuser.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tyo.githubuser.repository.User
import com.tyo.githubuser.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class SearchUserViewModel(private val repository: UserRepository) : ViewModel() {

    private var lastQuery: String? = null
    private var lastSearchResult: Flow<PagingData<User>>? = null

    fun searchUser(queryString: String): Flow<PagingData<User>> {
        val lastResult = lastSearchResult
        return if (queryString == lastQuery && lastResult != null) {
             lastResult
        } else {
            val newResult: Flow<PagingData<User>> = repository.getSearchResultStream(queryString)
                .cachedIn(viewModelScope)
            lastQuery = queryString
            lastSearchResult = newResult
            newResult
        }
    }
}