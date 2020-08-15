package com.tyo.githubuser.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tyo.githubuser.R
import com.tyo.githubuser.databinding.ActivityMainBinding
import com.tyo.githubuser.di.Injection
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SearchUserViewModel

    private val searchUserAdapter = SearchUserAdapter()
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get the view model
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(SearchUserViewModel::class.java)

        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            val divider = DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            recyclerView.addItemDecoration(divider)
            recyclerView.adapter = initAdapter()

            inputText.setOnEditorActionListener { textView , actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    searchFromInputText(true)
                    true
                } else {
                    false
                }
            }

            inputText.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchFromInputText(true)
                    true
                } else {
                    false
                }
            }

            retryButton.setOnClickListener { searchUserAdapter.retry() }
        }
    }

    override fun onResume() {
        super.onResume()
        searchFromInputText(false)
    }

    private fun initAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        // show toast when result is empty
        lifecycleScope.launch {
            @OptIn(ExperimentalPagingApi::class)
            searchUserAdapter.dataRefreshFlow.collectLatest {
                if (searchUserAdapter.itemCount == 0) {
                    showInfoToast(getString(R.string.empty_result))
                }
            }
        }

        // show toast when error
        searchUserAdapter.addLoadStateListener { loadState ->
            binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error

            errorState?.let { showErrorToast(it.error) }
        }

        return searchUserAdapter.withLoadStateHeaderAndFooter(
            header = LoadWithRetryAdapter { searchUserAdapter.retry() },
            footer = LoadWithRetryAdapter { searchUserAdapter.retry() }
        )
    }

    private fun searchFromInputText(showEmptyInputToast: Boolean) {
        val query = binding.inputText.text.toString()
        if (query.isNotBlank()) {
            search(query)
        }
        if (showEmptyInputToast && query.isBlank()) {
            showInfoToast(getString(R.string.empty_search_input))
        }
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchUser(query).collectLatest {
                searchUserAdapter.submitData(it)
            }
        }
    }

    private fun showErrorToast(error: Throwable) {
        Toast.makeText(this,
            "Wooops ${error.localizedMessage}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showInfoToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

