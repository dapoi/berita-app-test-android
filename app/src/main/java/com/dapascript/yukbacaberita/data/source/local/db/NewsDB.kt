package com.dapascript.yukbacaberita.data.source.local.db

import androidx.room.RoomDatabase
import com.dapascript.yukbacaberita.data.source.local.model.NewsEntity

@androidx.room.Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDB : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}