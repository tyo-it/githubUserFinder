package com.tyo.githubuser.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
            recyclerProjects.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerProjects.adapter = searchUserAdapter
            recyclerProjects.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

            inputText.setOnEditorActionListener { textView , actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    searchFromInputText()
                    true
                } else {
                    false
                }
            }

            inputText.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchFromInputText()
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        searchFromInputText()
    }

    private fun searchFromInputText() {
        val query = binding.inputText.text.toString()
        if (query.isBlank()) {
            Toast.makeText(this, "empty search input", Toast.LENGTH_SHORT).show()
        }
        search(query)
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                searchUserAdapter.submitData(it)
            }
        }
    }
}

