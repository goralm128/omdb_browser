package com.mayag.omdbbrowser.ui.screen.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mayag.omdbbrowser.domain.model.MovieDetail
import com.mayag.omdbbrowser.presentation.state.UiState
import com.mayag.omdbbrowser.presentation.viewmodel.MovieDetailsViewModel
import com.mayag.omdbbrowser.ui.components.LoadingIndicator
import com.mayag.omdbbrowser.ui.components.MovieDetailsContent

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsViewModel) {
    val movieDetailState by viewModel.selectedMovieDetail.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (movieDetailState) {
            is UiState.Loading -> LoadingIndicator()
            is UiState.Success -> {
                val movieDetail = (movieDetailState as UiState.Success<MovieDetail>).data
                MovieDetailsContent(movieDetail)
            }
            is UiState.Error -> {
                val errorMessage = (movieDetailState as UiState.Error).message
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
            UiState.None -> {
                Text(text = "Select a movie to see details.", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

