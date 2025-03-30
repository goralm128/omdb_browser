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
        holder.bind(movie, position)
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, position: Int) {
            binding.titleTextView.text = movie.title
            binding.yearTextView.text = movie.year

            Glide.with(binding.posterImageView.context)
                .load(movie.poster)
                .into(binding.posterImageView)

            binding.root.setOnClickListener {
                onItemClicked(movie)
                updateSelectedPosition(position)
            }

            // Highlight selected item
            binding.root.alpha = if (selectedPosition == position) 1.0f else 0.6f
            binding.root.scaleX = if (selectedPosition == position) 1.1f else 1.0f
            binding.root.scaleY = if (selectedPosition == position) 1.1f else 1.0f
        }
    }

    private fun updateSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(position)
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie):
                Boolean = oldItem.imdbID == newItem.imdbID
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie):
                Boolean = oldItem == newItem
    }
}

