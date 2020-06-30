package com.tyo.githubuser.view

import com.tyo.githubuser.SchedulerProvider
import com.tyo.githubuser.repository.User
import com.tyo.githubuser.repository.UserRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

interface SearchUserPresenter {
    fun searchUser(name: String)
    fun attachView()
    fun detachView()

    interface View {
        fun setUserList(users: List<User>)
        fun showError(message: String)
    }
}

class SearchUserPresenterImpl @Inject constructor(
    private val view: SearchUserPresenter.View,
    private val repository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : SearchUserPresenter {

    private val disposables = CompositeDisposable()

    override fun attachView() {
        //do nothing
    }

    override fun detachView() {
        disposables.clear()
    }

    override fun searchUser(name: String) {
        if (name.isEmpty()) {
            view.showError("input search is empty")
        } else {
            repository.search(name)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({ users ->
                    if (users.isEmpty()) view.showError("empty result")
                    else view.setUserList(users)
                }, {
                    view.showError("search failed")
                }).let {
                    disposables.add(it)
                }
        }
    }
}