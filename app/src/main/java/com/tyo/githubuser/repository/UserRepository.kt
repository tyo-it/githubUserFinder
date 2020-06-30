package com.tyo.githubuser.repository

import io.reactivex.Single
import javax.inject.Inject

interface UserRepository {
    fun search(name: String): Single<List<User>>
}

class UserRepositoryImpl @Inject constructor(
    private val githubService: GithubService): UserRepository {

    override fun search(name: String): Single<List<User>> {
        return githubService.searchUser(name).map { result -> toUser(result.items) }
    }

    private fun toUser(items: List<Item>): List<User> {
        return items.map { it ->
            User(
                it.login,
                it.avatarUrl
            )
        }
    }
}