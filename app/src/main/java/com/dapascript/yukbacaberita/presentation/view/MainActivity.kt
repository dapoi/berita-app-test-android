package com.dapascript.yukbacaberita.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dapascript.yukbacaberita.databinding.ActivityMainBinding
import com.dapascript.yukbacaberita.presentation.adapter.NewsAdapter
import com.dapascript.yukbacaberita.presentation.viewmodel.NewsViewModel
import com.dapascript.yukbacaberita.utils.moveToChrome
import com.dapascript.yukbacaberita.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter {
            newsViewModel.insertNews(listOf(it))
            if (it.url.isNullOrEmpty()) {
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
            adapter = newsAdapter
            val gridLayoutManager = GridLayoutManager(this@MainActivity, 2)
            layoutManager = gridLayoutManager

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position % 5 == 0) {
                        2
                    } else {
                        1
                    }
                }
            }

            // hide fab when scrolling
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: androidx.recyclerview.widget.RecyclerView,
                    dx: Int,
                    dy: Int
                ) {
                    if (dy > 0 || dy < 0 && binding.fabRead.isShown) {
                        binding.fabRead.hide()
                    }
                }

                override fun onScrollStateChanged(
                    recyclerView: androidx.recyclerview.widget.RecyclerView,
                    newState: Int
                ) {
                    if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                        binding.fabRead.show()
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }

    private fun initViewModel() {
        newsViewModel.apply {
            setNews("Indonesia")

            lifecycleScope.launch {
                getNews.collect { response ->
                    binding.apply {
                        shimmerLoading.isVisible = response is Resource.Loading
                        rvNews.isVisible = response is Resource.Success
                        fabRead.isVisible = response is Resource.Success
                        clError.isVisible = response is Resource.Error

                        if (response is Resource.Success) {
                            newsAdapter.submitList(response.data)
                            setSearch()
                        }

                        fabRead.setOnClickListener {
                            startActivity(Intent(this@MainActivity, ReadNewsActivity::class.java))
                        }

                        btnRefresh.setOnClickListener {
                            initViewModel()
                            svNews.isIconified = true
                        }
                    }
                }
            }
        }
    }

    private fun setSearch() {
        binding.svNews.apply {
            setOnSearchClickListener {
                binding.tvTitle.isVisible = false
            }

            setOnCloseListener {
                binding.tvTitle.isVisible = true
                false
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    newsViewModel.setNews(query.toString())
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }
}