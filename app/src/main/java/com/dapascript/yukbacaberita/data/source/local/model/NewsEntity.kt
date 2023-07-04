package com.dapascript.yukbacaberita.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_read")
data class NewsEntity(
    @PrimaryKey val url: String,
    val title: String,
    val image: String
)
