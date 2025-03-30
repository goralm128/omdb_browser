package com.mayag.omdbbrowser.data.mapper

import com.mayag.omdbbrowser.data.model.MovieDetailDto
import com.mayag.omdbbrowser.data.model.MovieDto
import com.mayag.omdbbrowser.domain.model.Movie
import com.mayag.omdbbrowser.domain.model.MovieDetail

object MovieMapper {

    fun mapToDomain(dto: MovieDto): Movie {
        return Movie(
            title = dto.title,
            year = dto.year,
            imdbID = dto.imdbID,
            poster = dto.poster,
            type = dto.type
        )
    }

    fun mapDetailToDomain(dto: MovieDetailDto): MovieDetail {
        return MovieDetail(
            title = dto.title,
            year = dto.year,
            rated = dto.rated,
            released = dto.released,
            runtime = dto.runtime,
            genre = dto.genre,
            director = dto.director,
            writer = dto.writer,
            actors = dto.actors,
            plot = dto.plot,
            language = dto.language,
            country = dto.country,
            awards = dto.awards,
            poster = dto.poster,
            imdbRating = dto.imdbRating,
            imdbID = dto.imdbID
        )
    }
}
