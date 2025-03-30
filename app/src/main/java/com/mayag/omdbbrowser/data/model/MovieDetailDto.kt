package com.mayag.omdbbrowser.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetailDto(
    @SerializedName("Title")
    val title: String,

    @SerializedName("Year")
    val year: String,

    @SerializedName("Rated")
    val rated: String? = null,

    @SerializedName("Released")
    val released: String? = null,

    @SerializedName("Runtime")
    val runtime: String? = null,

    @SerializedName("Genre")
    val genre: String? = null,

    @SerializedName("Director")
    val director: String? = null,

    @SerializedName("Writer")
    val writer: String? = null,

    @SerializedName("Actors")
    val actors: String? = null,

    @SerializedName("Plot")
    val plot: String? = null,

    @SerializedName("Language")
    val language: String? = null,

    @SerializedName("Country")
    val country: String? = null,

    @SerializedName("Awards")
    val awards: String? = null,

    @SerializedName("Poster")
    val poster: String,

    @SerializedName("imdbRating")
    val imdbRating: String? = null,

    @SerializedName("imdbID")
    val imdbID: String
)

