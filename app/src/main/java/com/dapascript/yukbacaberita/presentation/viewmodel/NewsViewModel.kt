package com.dapascript.yukbacaberita.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapascript.yukbacaberita.data.repository.NewsRepository
import com.dapascript.yukbacaberita.data.source.local.model.NewsEntity
import com.dapascript.yukbacaberita.data.source.remote.model.ArticlesItem
import com.dapascript.yukbacaberita.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _getNews = MutableStateFlow<Resource<List<ArticlesItem?>>>(Resource.Loading)
    val getNews: StateFlow<Resource<List<ArticlesItem?>>> get() = _getNews

    private var _getReadNews = MutableStateFlow<List<NewsEntity>>(listOf())
    val getReadNews: StateFlow<List<NewsEntity>> get() = _getReadNews

    fun setNews(q: String) {
        viewModelScope.launch {
            newsRepository.getNews(q).collect {
                _getNews.value = it
            }
        }
    }

    fun insertNews(news: List<ArticlesItem?>) {
        viewModelScope.launch {
            newsRepository.insertNews(news)
        }
    }

    init {
        viewModelScope.launch {
            newsRepository.getReadNews().collect {
                _getReadNews.value = it
            }
        }
    }
}