package com.mayag.omdbbrowser.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponseDto(
    @SerializedName("Search")
    val results: List<MovieDto> = emptyList(),

    @SerializedName("totalResults")
    val totalResults: String? = null,

    @SerializedName("Response")
    val response: String? = null
)

