package com.tyo.githubuser.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyo.githubuser.R
import com.tyo.githubuser.di.DaggerSearchUserComponent
import com.tyo.githubuser.di.SearchUserFragmentModule
import com.tyo.githubuser.repository.User
import kotlinx.android.synthetic.main.fragment_search_result.*
import javax.inject.Inject

class SearchUserFragment: Fragment(), SearchUserPresenter.View {

    private val searchResultAdapter = SearchResultAdapter()

    @Inject
    lateinit var presenter: SearchUserPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true

        recycler_projects.layoutManager = LinearLayoutManager(context)
        recycler_projects.adapter = searchResultAdapter

        btn_search.setOnClickListener {
            val searchText = input_text.text.toString()
            presenter.searchUser(searchText)
        }
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun injectDependencies() {
        val component = DaggerSearchUserComponent.builder()
            .searchUserFragmentModule(SearchUserFragmentModule((this)))
            .build()
        component.inject(this)
    }

    override fun setUserList(users: List<User>) {
        searchResultAdapter.users = users
        searchResultAdapter.notifyDataSetChanged()
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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