package com.chaitanya.mangashelf.di

import android.content.Context
import androidx.room.Room
import com.chaitanya.mangashelf.api.ApiService
import com.chaitanya.mangashelf.local.MangaDao
import com.chaitanya.mangashelf.local.MangaShelfDatabase
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
object DataModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.jsonkeeper.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MangaShelfDatabase {
        return Room.databaseBuilder(
            context,
            MangaShelfDatabase::class.java,
            "manga_db.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideDao(db:MangaShelfDatabase): MangaDao {
        return db.mangaDao()
    }

}