package com.tyo.githubuser.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tyo.githubuser.repository.UserRepository

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}