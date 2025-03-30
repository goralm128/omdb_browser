package com.mayag.omdbbrowser.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mayag.omdbbrowser.domain.model.Movie
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun movieCard_showsMovieDetails() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "Batman",
            year = "1989",
            poster = "https://example.com/poster.jpg"
        )

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = false,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Batman").assertExists()
        composeTestRule.onNodeWithText("1989").assertExists()
        composeTestRule.onNodeWithTag("movie_poster").assertExists()
    }

    @Test
    fun movieCard_showsSelectedState_whenSelected() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "Batman",
            year = "1989",
            poster = "https://example.com/poster.jpg"
        )

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = true,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag("selected_indicator").assertExists()
    }

    @Test
    fun movieCard_callsOnClick_whenClicked() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "Batman",
            year = "1989",
            poster = "https://example.com/poster.jpg"
        )
        var onClickCalled = false

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = false,
                onClick = { onClickCalled = true }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Batman").performClick()
        assert(onClickCalled) { "onClick should have been called" }
    }

    // New error handling tests
    @Test
    fun movieCard_handlesLongTitle() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "The Lord of the Rings: The Fellowship of the Ring Extended Edition 2001",
            year = "2001",
            poster = "https://example.com/poster.jpg"
        )

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = false,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(movie.title).assertExists()
        composeTestRule.onNodeWithText(movie.year).assertExists()
    }

    @Test
    fun movieCard_handlesSpecialCharactersInTitle() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "Star Wars: Episode IV - A New Hope (1977)",
            year = "1977",
            poster = "https://example.com/poster.jpg"
        )

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = false,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(movie.title).assertExists()
        composeTestRule.onNodeWithText(movie.year).assertExists()
    }

    @Test
    fun movieCard_handlesInvalidPosterUrl() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "Batman",
            year = "1989",
            poster = "invalid-url"
        )

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = false,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag("movie_poster").assertExists()
        composeTestRule.onNodeWithText(movie.title).assertExists()
        composeTestRule.onNodeWithText(movie.year).assertExists()
    }

    @Test
    fun movieCard_handlesEmptyPosterUrl() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "Batman",
            year = "1989",
            poster = ""
        )

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = false,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag("movie_poster").assertExists()
        composeTestRule.onNodeWithText(movie.title).assertExists()
        composeTestRule.onNodeWithText(movie.year).assertExists()
    }

    @Test
    fun movieCard_handlesNullPosterUrl() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "Batman",
            year = "1989",
            poster = null
        )

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = false,
                onClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag("movie_poster").assertExists()
        composeTestRule.onNodeWithText(movie.title).assertExists()
        composeTestRule.onNodeWithText(movie.year).assertExists()
    }

    @Test
    fun movieCard_handlesRapidClicks() {
        // Given
        val movie = Movie(
            imdbID = "tt0096895",
            title = "Batman",
            year = "1989",
            poster = "https://example.com/poster.jpg"
        )
        var clickCount = 0

        // When
        composeTestRule.setContent {
            MovieCard(
                movie = movie,
                isSelected = false,
                onClick = { clickCount++ }
            )
        }

        // Then
        repeat(5) {
            composeTestRule.onNodeWithText("Batman").performClick()
        }
        assertEquals(5, clickCount) { "Click count should be 5" }
    }
} 