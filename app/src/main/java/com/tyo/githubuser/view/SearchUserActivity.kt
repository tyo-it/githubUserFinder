package com.tyo.githubuser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyo.githubuser.R
import com.tyo.githubuser.di.DaggerActivityComponent
import com.tyo.githubuser.di.SearchUserActivityModule
import com.tyo.githubuser.repository.User
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class SearchUserActivity : AppCompatActivity(), SearchUserPresenter.View {

    private val searchResultAdapter = SearchResultAdapter()

    @Inject
    lateinit var presenter: SearchUserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_projects.layoutManager = LinearLayoutManager(this)
        recycler_projects.adapter = searchResultAdapter

        btn_search.setOnClickListener { presenter.searchUser(input_text.text.toString()) }
    }

    private fun injectDependencies() {
        val component = DaggerActivityComponent.builder()
            .searchUserActivityModule(SearchUserActivityModule(this))
            .build()
        component.inject(this)
    }

    override fun setUserList(users: List<User>) {
        searchResultAdapter.users = users
        searchResultAdapter.notifyDataSetChanged()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView()
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }
}

