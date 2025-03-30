package com.mayag.omdbbrowser.domain.repository

import com.mayag.omdbbrowser.domain.model.Movie
import com.mayag.omdbbrowser.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun searchMovies(query: String): Flow<List<Movie>>
    suspend fun getMovieDetails(imdbId: String): Flow<MovieDetail>
}
