package com.dapascript.yukbacaberita.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dapascript.yukbacaberita.data.source.local.model.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReadNews(news: List<NewsEntity>)

    @Query("SELECT * FROM news_read")
    fun getReadNews(): Flow<List<NewsEntity>>
}