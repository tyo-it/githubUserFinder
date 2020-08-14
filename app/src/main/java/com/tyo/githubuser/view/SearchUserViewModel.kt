package com.tyo.githubuser.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tyo.githubuser.repository.User
import com.tyo.githubuser.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class SearchUserViewModel(private val repository: UserRepository) : ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<User>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<User>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<User>> = repository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}