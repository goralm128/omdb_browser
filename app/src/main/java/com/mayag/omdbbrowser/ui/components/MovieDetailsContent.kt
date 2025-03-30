package com.mayag.omdbbrowser.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mayag.omdbbrowser.domain.model.MovieDetail

@Composable
fun MovieDetailsContent(movie: MovieDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Year: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Director: ${movie.director}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Plot: ${movie.plot}", style = MaterialTheme.typography.bodySmall)
    }
}
