package com.tyo.githubuser.di

import com.tyo.githubuser.view.SearchUserActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SearchUserActivityModule::class])
interface ActivityComponent {
    fun inject(activity: SearchUserActivity)
}