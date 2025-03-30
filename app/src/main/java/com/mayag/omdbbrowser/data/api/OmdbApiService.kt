package com.mayag.omdbbrowser.data.api

import com.mayag.omdbbrowser.data.model.MovieDetailDto
import com.mayag.omdbbrowser.data.model.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {

    // Search by title
    @GET(".")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String
    ): SearchResponseDto

    // Get full details using IMDb ID
    @GET(".")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String
    ): MovieDetailDto

}

