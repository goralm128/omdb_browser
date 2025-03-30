package com.mayag.omdbbrowser.ui.screen.search

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mayag.omdbbrowser.domain.model.Movie
import com.mayag.omdbbrowser.presentation.state.UiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<MovieViewModel>(relaxed = true)

    @Test
    fun searchScreen_showsSearchField() {
        // Given
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
        every { mockViewModel.movies } returns moviesState

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Search movies or shows").assertExists()
    }

    @Test
    fun searchScreen_showsLoadingIndicator_whenLoading() {
        // Given
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
        every { mockViewModel.movies } returns moviesState

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun searchScreen_showsMovieList_whenSuccess() {
        // Given
        val movies = listOf(
            Movie(
                imdbID = "tt0096895",
                title = "Batman",
                year = "1989",
                poster = "https://example.com/poster.jpg"
            )
        )
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Success(movies))
        every { mockViewModel.movies } returns moviesState

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Batman").assertExists()
        composeTestRule.onNodeWithText("1989").assertExists()
    }

    @Test
    fun searchScreen_showsError_whenError() {
        // Given
        val errorMessage = "Network error"
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Error(errorMessage))
        every { mockViewModel.movies } returns moviesState

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertExists()
        composeTestRule.onNodeWithTag("error_message").assertExists()
    }

    @Test
    fun searchScreen_callsOnMovieSelected_whenMovieClicked() {
        // Given
        val movies = listOf(
            Movie(
                imdbID = "tt0096895",
                title = "Batman",
                year = "1989",
                poster = "https://example.com/poster.jpg"
            )
        )
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Success(movies))
        every { mockViewModel.movies } returns moviesState
        var onMovieSelectedCalled = false

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = { onMovieSelectedCalled = true }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Batman").performClick()
        assert(onMovieSelectedCalled) { "onMovieSelected should have been called" }
    }

    // New error handling tests
    @Test
    fun searchScreen_handlesEmptySearchQuery() {
        // Given
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Success(emptyList()))
        every { mockViewModel.movies } returns moviesState

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Search movies or shows").performTextInput("")
        verify { mockViewModel.searchMovies("") }
    }

    @Test
    fun searchScreen_handlesSpecialCharactersInSearch() {
        // Given
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Success(emptyList()))
        every { mockViewModel.movies } returns moviesState
        val specialQuery = "Star Wars: Episode IV - A New Hope"

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Search movies or shows").performTextInput(specialQuery)
        verify { mockViewModel.searchMovies(specialQuery) }
    }

    @Test
    fun searchScreen_handlesLongSearchQuery() {
        // Given
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Success(emptyList()))
        every { mockViewModel.movies } returns moviesState
        val longQuery = "The Lord of the Rings: The Fellowship of the Ring Extended Edition 2001"

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Search movies or shows").performTextInput(longQuery)
        verify { mockViewModel.searchMovies(longQuery) }
    }

    @Test
    fun searchScreen_handlesNetworkError() {
        // Given
        val errorMessage = "No internet connection"
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Error(errorMessage))
        every { mockViewModel.movies } returns moviesState

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertExists()
        composeTestRule.onNodeWithTag("error_message").assertExists()
    }

    @Test
    fun searchScreen_handlesServerError() {
        // Given
        val errorMessage = "Server error: 500 Internal Server Error"
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Error(errorMessage))
        every { mockViewModel.movies } returns moviesState

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertExists()
        composeTestRule.onNodeWithTag("error_message").assertExists()
    }

    @Test
    fun searchScreen_handlesEmptySearchResults() {
        // Given
        val moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Success(emptyList()))
        every { mockViewModel.movies } returns moviesState

        // When
        composeTestRule.setContent {
            SearchScreen(
                viewModel = mockViewModel,
                onMovieSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Search movies or shows").performTextInput("NonexistentMovie123")
        verify { mockViewModel.searchMovies("NonexistentMovie123") }
    }
} 