package com.dapascript.yukbacaberita.presentation.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapascript.yukbacaberita.databinding.ActivityReadNewsBinding
import com.dapascript.yukbacaberita.presentation.adapter.ReadNewsAdapter
import com.dapascript.yukbacaberita.presentation.viewmodel.NewsViewModel
import com.dapascript.yukbacaberita.utils.moveToChrome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReadNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadNewsBinding
    private lateinit var readNewsAdapter: ReadNewsAdapter
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        readNewsAdapter = ReadNewsAdapter {
            if (it.url.isEmpty()) {
                Toast.makeText(
                    this,
                    "Maaf, link tidak tersedia",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                moveToChrome(this, it.url)
            }
        }
        binding.rvNews.apply {
            adapter = readNewsAdapter
            layoutManager = LinearLayoutManager(this@ReadNewsActivity)
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            newsViewModel.getReadNews.collect { response ->
                if (response.isEmpty()) {
                    binding.tvEmpty.isVisible = true
                } else {
                    binding.tvEmpty.isVisible = false
                    readNewsAdapter.setListReadNews(response)
                }
            }
        }
    }
}