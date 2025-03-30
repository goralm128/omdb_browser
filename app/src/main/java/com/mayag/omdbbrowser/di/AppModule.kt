package com.mayag.omdbbrowser.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mayag.omdbbrowser.data.api.OmdbApiService
import com.mayag.omdbbrowser.data.repository.MovieRepositoryImpl
import com.mayag.omdbbrowser.domain.repository.MovieRepository
import com.mayag.omdbbrowser.utils.Constants.API_KEY
import com.mayag.omdbbrowser.utils.Constants.BASE_URL
import com.mayag.omdbbrowser.utils.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    @ApiKeyQualifier
    fun provideApiKey(): String = API_KEY

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideOmdbApiService(retrofit: Retrofit): OmdbApiService =
        retrofit.create(OmdbApiService::class.java)

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: OmdbApiService,
        networkManager: NetworkManager,
        @ApiKeyQualifier apiKey: String
    ): MovieRepository = MovieRepositoryImpl(api, networkManager, apiKey)
}


