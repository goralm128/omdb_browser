package com.mayag.omdbbrowser.data.repository

import android.util.Log
import com.mayag.omdbbrowser.utils.NetworkException
import com.mayag.omdbbrowser.utils.ServerException
import com.mayag.omdbbrowser.utils.ParsingException
import com.mayag.omdbbrowser.utils.UnknownException

import com.google.gson.JsonSyntaxException
import com.mayag.omdbbrowser.data.api.OmdbApiService
import com.mayag.omdbbrowser.data.mapper.MovieMapper
import com.mayag.omdbbrowser.di.ApiKeyQualifier
import com.mayag.omdbbrowser.domain.model.Movie
import com.mayag.omdbbrowser.domain.model.MovieDetail
import com.mayag.omdbbrowser.domain.repository.MovieRepository
import com.mayag.omdbbrowser.utils.NetworkManager
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@ViewModelScoped
class MovieRepositoryImpl @Inject constructor(
    private val apiService: OmdbApiService,
    private val networkManager: NetworkManager,
    @ApiKeyQualifier private val apiKey: String
) : MovieRepository {

    override suspend fun searchMovies(query: String): Flow<List<Movie>> = flow {
        if (!networkManager.isConnected()) {
            throw NetworkException("No internet connection.")
        }
        val response = apiService.searchMovies(apiKey, query)
        val movies = response.results.map { MovieMapper.mapToDomain(it) }
        emit(movies)
    }.catch {
        e -> handleException(e)
    }

    override suspend fun getMovieDetails(imdbId: String): Flow<MovieDetail> = flow {
        if (!networkManager.isConnected()) {
            throw NetworkException("No internet connection.")
        }

        val response = apiService.getMovieDetails(apiKey, imdbId)
        val movieDetail = MovieMapper.mapDetailToDomain(response)
        emit(movieDetail)
    }.catch {
        e -> handleException(e)
    }

    private fun <T> FlowCollector<T>.handleException(e: Throwable) {
        e.message?.let { Log.e("MovieRepositoryImpl", it) }
        when (e) {
            is IOException -> throw NetworkException("Network Error: ${e.message}", e)
            is HttpException -> throw ServerException("Server Error: ${e.message()}", e)
            is JsonSyntaxException -> throw ParsingException("Parsing Error: Unable to parse the response", e)
            else -> throw UnknownException("Unknown Error: ${e.message}", e)
        }
    }

}
