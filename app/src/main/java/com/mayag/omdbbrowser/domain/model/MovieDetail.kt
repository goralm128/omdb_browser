package com.mayag.omdbbrowser.domain.model

data class MovieDetail(
    val title: String,
    val year: String,
    val rated: String?,
    val released: String?,
    val runtime: String?,
    val genre: String?,
    val director: String?,
    val writer: String?,
    val actors: String?,
    val plot: String?,
    val language: String?,
    val country: String?,
    val awards: String?,
    val poster: String,
    val imdbRating: String?,
    val imdbID: String
)
