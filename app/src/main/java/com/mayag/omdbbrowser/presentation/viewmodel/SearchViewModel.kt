package com.mayag.omdbbrowser.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mayag.omdbbrowser.domain.model.Movie
import com.mayag.omdbbrowser.domain.repository.MovieRepository
import com.mayag.omdbbrowser.presentation.state.UiState
import com.mayag.omdbbrowser.utils.ParsingException
import com.mayag.omdbbrowser.utils.UnknownException
import com.mayag.omdbbrowser.utils.ServerException
import com.mayag.omdbbrowser.utils.NetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_SEARCH_RESULTS = "search_results"
        private const val KEY_LAST_QUERY = "last_query"
    }

    private val gson = Gson()
    private val movieType = object : TypeToken<List<Movie>>() {}.type

    private val _searchResults = MutableStateFlow<UiState<List<Movie>>>(UiState.None)
    val searchResults: StateFlow<UiState<List<Movie>>> = _searchResults.asStateFlow()

    init {
        // Restore the search results and last query if available
        val savedResultsJson = savedStateHandle.get<String>(KEY_SEARCH_RESULTS)
        val lastQuery = savedStateHandle.get<String>(KEY_LAST_QUERY)

        if (savedResultsJson != null && lastQuery != null) {
            val savedResults: List<Movie> = gson.fromJson(savedResultsJson, movieType)
            _searchResults.value = UiState.Success(savedResults)
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _searchResults.value = UiState.Loading
            movieRepository.searchMovies(query)
                .catch { exception ->
                    val errorMessage = when (exception) {
                        is NetworkException -> "No internet connection. Please check your connection."
                        is ServerException -> "Server error occurred. Please try again later."
                        is ParsingException -> "Data parsing error. Please try again later."
                        is UnknownException -> "An unknown error occurred. Please try again."
                        else -> exception.message ?: "An unexpected error occurred"
                    }
                    _searchResults.value = UiState.Error(errorMessage)
                }
                .collect { movies ->
                    if (movies.isNotEmpty()) {
                        _searchResults.value = UiState.Success(movies)

                        // Save the state as a JSON string
                        val jsonMovies = gson.toJson(movies)
                        savedStateHandle[KEY_SEARCH_RESULTS] = jsonMovies
                        savedStateHandle[KEY_LAST_QUERY] = query
                    } else {
                        _searchResults.value = UiState.Error("No results found.")
                    }
                }
        }
    }

    private val _selectedMovie = MutableSharedFlow<Movie>(replay = 1)
    val selectedMovie = _selectedMovie.asSharedFlow()

    fun selectMovie(movie: Movie) {
        viewModelScope.launch {
            _selectedMovie.emit(movie)
        }
    }
}
