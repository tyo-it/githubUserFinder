package com.tyo.githubuser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyo.githubuser.R
import com.tyo.githubuser.di.Injection
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchUserFragment: Fragment() {

    private lateinit var viewModel: SearchUserViewModel
    private val adapter = SearchUserAdapter()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the view model
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(SearchUserViewModel::class.java)

        recycler_projects.layoutManager = LinearLayoutManager(context)
        initAdapter()

        btn_search.setOnClickListener {
            val query = input_text.text.toString()
            // Make sure we cancel the previous job before creating a new one
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                viewModel.searchRepo(query).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    fun initAdapter() {
        recycler_projects.adapter = adapter
    }
}