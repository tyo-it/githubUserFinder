package com.tyo.githubuser

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.tyo.githubuser.repository.User
import com.tyo.githubuser.repository.UserRepository
import com.tyo.githubuser.view.SearchUserPresenter
import com.tyo.githubuser.view.SearchUserPresenterImpl
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import java.lang.Exception

class SearchUserPresenterTest {
    private val userRepository: UserRepository = mock()
    private val view: SearchUserPresenter.View = mock()
    private val schedulerProvider: SchedulerProvider = SchedulerProviderImpl(Schedulers.trampoline(), Schedulers.trampoline())

    private val searchUserPresenter =
        SearchUserPresenterImpl(
            view,
            userRepository,
            schedulerProvider
        )

    @Test
    fun givenSuccess_whenSearchUser_thenShowUserOnView() {
        // given
        val name = "any"
        val users = listOf(User("name", "url"))
        whenever(userRepository.search(name)).thenReturn(Single.just(users))

        // when
        searchUserPresenter.searchUser(name)

        // then
        verify(view).setUserList(users)
    }

    @Test
    fun givenEmptyResult_whenSearchUser_thenShowError() {
        // given
        val name = "any"
        val users = emptyList<User>()
        whenever(userRepository.search(name)).thenReturn(Single.just(users))

        // when
        searchUserPresenter.searchUser(name)

        // then
        verify(view).showError("empty result")
    }

    @Test
    fun givenExceptionThrown_whenSearchUser_thenShowError() {
        // given
        val name = "any"
        whenever(userRepository.search(name)).thenReturn(Single.error(Exception("error")))

        // when
        searchUserPresenter.searchUser(name)

        // then
        verify(view).showError("search failed")
    }
}
