package com.tyo.githubuser.di

import androidx.lifecycle.ViewModelProvider
import com.tyo.githubuser.BuildConfig
import com.tyo.githubuser.api.GithubServiceFactory
import com.tyo.githubuser.repository.UserRepository
import com.tyo.githubuser.view.ViewModelFactory

object Injection {

    private fun provideUserRepository(): UserRepository {
        return UserRepository(GithubServiceFactory.makeGithubService(BuildConfig.DEBUG))
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideUserRepository())
    }
}