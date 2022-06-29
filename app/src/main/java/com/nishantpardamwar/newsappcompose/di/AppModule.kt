package com.nishantpardamwar.newsappcompose.di

import com.nishantpardamwar.newsappcompose.models.ApiKey
import com.nishantpardamwar.newsappcompose.network.RetrofitApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesApiService() = Retrofit.Builder().baseUrl("https://newsapi.org").client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitApiInterface::class.java)

    @Provides
    fun providesApiKey() = ApiKey("YOUR API KEY")
}