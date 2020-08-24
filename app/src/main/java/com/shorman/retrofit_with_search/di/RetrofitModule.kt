package com.shorman.retrofit_with_search.di

import com.shorman.retrofit_with_search.retrofit.CoronaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RetrofitModule {

    @Provides
    @Singleton
    fun provideHttpClient() =  OkHttpClient.Builder()
        .callTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.MINUTES)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://api.covid19api.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideCoronaApi(retrofit: Retrofit) = retrofit.create(CoronaApi::class.java)
}