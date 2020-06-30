package com.tyo.githubuser.di

import com.tyo.githubuser.BuildConfig
import com.tyo.githubuser.SchedulerProvider
import com.tyo.githubuser.SchedulerProviderImpl
import com.tyo.githubuser.repository.*
import com.tyo.githubuser.view.SearchUserFragment
import com.tyo.githubuser.view.SearchUserPresenter
import com.tyo.githubuser.view.SearchUserPresenterImpl
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class SearchUserFragmentModule(private val fragment: SearchUserFragment) {

    @Provides
    fun provideSearchUserPresenter(
        repository: UserRepository,
        schedulerProvider: SchedulerProvider
    ): SearchUserPresenter {
        return SearchUserPresenterImpl(fragment, repository, schedulerProvider)
    }

    @Provides
    fun provideUserRepository(githubService: GithubService): UserRepository {
        return UserRepositoryImpl(githubService)
    }

    @Singleton
    @Provides
    fun provideGithubService(): GithubService {
        return GithubServiceFactory.makeGithubService(BuildConfig.DEBUG)
    }

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return SchedulerProviderImpl(AndroidSchedulers.mainThread(), Schedulers.io())
    }
}