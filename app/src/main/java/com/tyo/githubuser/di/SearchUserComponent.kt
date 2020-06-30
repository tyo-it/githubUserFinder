package com.tyo.githubuser.di

import com.tyo.githubuser.view.SearchUserFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SearchUserFragmentModule::class])
interface SearchUserComponent {
    fun inject(fragment: SearchUserFragment)
}