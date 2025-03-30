package com.mayag.omdbbrowser.ui.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mayag.omdbbrowser.databinding.ItemMovieBinding
import com.mayag.omdbbrowser.domain.model.Movie

class MovieAdapter(
    private val onItemClicked: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DiffCallback()) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, position == selectedPosition)

        holder.itemView.setOnClickListener {
            onItemClicked(movie)
            updateSelectedPosition(position)
        }
    }

    fun updateSelectedPosition(position: Int) {
        if (selectedPosition != position) {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
        }
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, isSelected: Boolean) {
            binding.titleTextView.text = movie.title
            binding.yearTextView.text = movie.year

            Glide.with(binding.posterImageView.context)
                .load(movie.poster)
                .into(binding.posterImageView)

            if (isSelected) {
                binding.root.apply {
                    scaleX = 1.1f
                    scaleY = 1.1f
                    alpha = 1.0f
                    elevation = 8f // Add shadow to emphasize the selection
                }
            } else {
                binding.root.apply {
                    scaleX = 1.0f
                    scaleY = 1.0f
                    alpha = 0.7f
                    elevation = 0f
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.imdbID == newItem.imdbID
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}


