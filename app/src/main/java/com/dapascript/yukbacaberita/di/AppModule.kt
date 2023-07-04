package com.dapascript.yukbacaberita.di

import android.content.Context
import androidx.room.Room
import com.dapascript.yukbacaberita.data.repository.NewsRepository
import com.dapascript.yukbacaberita.data.repository.NewsRepositoryImpl
import com.dapascript.yukbacaberita.data.source.local.db.NewsDB
import com.dapascript.yukbacaberita.data.source.remote.network.ApiService
import com.facebook.shimmer.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideBaseUrl(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    @Singleton
    fun provideNewsService(
        retrofit: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ApiService {
        return retrofit
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDB(@ApplicationContext context: Context): NewsDB = Room.databaseBuilder(
        context, NewsDB::class.java, "news.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideNewsDao(database: NewsDB) = database.newsDao()

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository {
        return newsRepositoryImpl
    }
}