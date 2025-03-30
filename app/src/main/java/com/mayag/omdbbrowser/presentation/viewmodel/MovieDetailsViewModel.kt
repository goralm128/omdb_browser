package com.mayag.omdbbrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayag.omdbbrowser.domain.model.MovieDetail
import com.mayag.omdbbrowser.domain.repository.MovieRepository
import com.mayag.omdbbrowser.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _selectedMovieDetail = MutableStateFlow<UiState<MovieDetail>>(UiState.None)
    val selectedMovieDetail: StateFlow<UiState<MovieDetail>> = _selectedMovieDetail.asStateFlow()

    fun fetchMovieDetails(imdbID: String) {
        viewModelScope.launch {
            _selectedMovieDetail.value = UiState.Loading
            movieRepository.getMovieDetails(imdbID)
                .catch { e ->
                    _selectedMovieDetail.value =
                        UiState.Error(e.message ?: "An unexpected error occurred")
                }
                .collect { movieDetail ->
                    _selectedMovieDetail.value = UiState.Success(movieDetail)
                }
        }
    }
}

