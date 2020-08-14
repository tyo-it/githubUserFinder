package com.tyo.githubuser.di

import androidx.lifecycle.ViewModelProvider
import com.tyo.githubuser.BuildConfig
import com.tyo.githubuser.repository.GithubService
import com.tyo.githubuser.repository.GithubServiceFactory
import com.tyo.githubuser.repository.UserRepository
import com.tyo.githubuser.view.ViewModelFactory

object Injection {

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    private fun provideGithubRepository(): UserRepository {
        return UserRepository(GithubServiceFactory.makeGithubService(BuildConfig.DEBUG))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository())
    }
}