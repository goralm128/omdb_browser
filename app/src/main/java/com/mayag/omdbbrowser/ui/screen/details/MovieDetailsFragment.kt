package com.mayag.omdbbrowser.ui.screen.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mayag.omdbbrowser.databinding.FragmentMovieDetailsBinding
import com.mayag.omdbbrowser.domain.model.MovieDetail
import com.mayag.omdbbrowser.presentation.state.UiState
import com.mayag.omdbbrowser.presentation.viewmodel.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MovieDetailsFragmentArgs by navArgs()
        val imdbID = args.movieId

        viewModel.fetchMovieDetails(imdbID)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedMovieDetail.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> showLoading()
                        is UiState.Success -> showMovieDetails(uiState.data)
                        is UiState.Error -> showError(uiState.message)
                        UiState.None -> {}
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.movieDetailsLayout.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE
    }

    private fun showMovieDetails(movieDetail: MovieDetail) {
        binding.progressBar.visibility = View.GONE
        binding.movieDetailsLayout.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE

        binding.titleTextView.text = movieDetail.title
        binding.yearTextView.text = movieDetail.year
        binding.plotTextView.text = movieDetail.plot

        Glide.with(this)
            .load(movieDetail.poster)
            .into(binding.posterImageView)
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.movieDetailsLayout.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
        binding.errorTextView.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





