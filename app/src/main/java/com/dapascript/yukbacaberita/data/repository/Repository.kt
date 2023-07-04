package com.dapascript.yukbacaberita.data.repository

import com.dapascript.yukbacaberita.data.source.local.db.NewsDao
import com.dapascript.yukbacaberita.data.source.local.model.NewsEntity
import com.dapascript.yukbacaberita.data.source.remote.model.ArticlesItem
import com.dapascript.yukbacaberita.data.source.remote.network.ApiService
import com.dapascript.yukbacaberita.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    override suspend fun getNews(q: String): Flow<Resource<List<ArticlesItem?>>> = flow {
        try {
            emit(Resource.Loading)

            val response = apiService.getNews(q).articles
            if (response.isNullOrEmpty()) {
                emit(Resource.Error(Throwable("Data is empty")))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Throwable) {
            emit(Resource.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun insertNews(news: List<ArticlesItem?>) {
        news.map {
            val newsEntity = NewsEntity(
                url = it?.url.toString(),
                title = it?.title.toString(),
                image = it?.urlToImage.toString(),
            )
            newsDao.insertReadNews(
                news = listOf(newsEntity)
            )
        }
    }

    override fun getReadNews(): Flow<List<NewsEntity>> = newsDao.getReadNews()
}

interface NewsRepository {

    suspend fun getNews(q: String): Flow<Resource<List<ArticlesItem?>>>

    suspend fun insertNews(news: List<ArticlesItem?>)

    fun getReadNews(): Flow<List<NewsEntity>>
}