package com.tyo.githubuser.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tyo.githubuser.R
import com.tyo.githubuser.databinding.ViewItemLoadWithRetryBinding
import java.io.IOException

class LoadWithRetryAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadWithRetryAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder.create(
            parent,
            retry
        )
    }

    class ViewHolder(
        private val binding: ViewItemLoadWithRetryBinding,
        retry: () -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                if (loadState.error is IOException) {
                    binding.errorMsg.text = this.itemView.context.getString(R.string.fetch_failed)
                } else {
                    binding.errorMsg.text = loadState.error.localizedMessage
                }
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_item_load_with_retry, parent, false)
                val binding = ViewItemLoadWithRetryBinding.bind(view)
                return ViewHolder(
                    binding,
                    retry
                )
            }
        }
    }
}