package com.mayag.omdbbrowser.ui.screen.search

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.mayag.omdbbrowser.databinding.FragmentSearchBinding
import com.mayag.omdbbrowser.domain.model.Movie
import com.mayag.omdbbrowser.presentation.state.UiState
import com.mayag.omdbbrowser.presentation.viewmodel.SearchViewModel
import com.mayag.omdbbrowser.ui.components.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieAdapter
    private val snapHelper by lazy { LinearSnapHelper() }

    private var recyclerViewState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        observeViewModel()

        // Restore previous state if available
        if (viewModel.searchResults.value is UiState.Success) {
            val savedMovies = (viewModel.searchResults.value as UiState.Success<List<Movie>>).data
            showMovies(savedMovies)
        }
    }

    private fun setupRecyclerView() {
        val orientation = resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        } else {
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        movieAdapter = MovieAdapter { selectedMovie ->
            viewModel.selectMovie(selectedMovie)
            val action = SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(selectedMovie.imdbID)
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            this.layoutManager = layoutManager
            adapter = movieAdapter
            setHasFixedSize(true)

            // Attach Snap Helper
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)

            // Scroll Listener for snapping detection
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val snapView = snapHelper.findSnapView(layoutManager)
                        val position = snapView?.let { layoutManager.getPosition(it) }

                        if (position != null && position != RecyclerView.NO_POSITION) {
                            val snappedMovie = movieAdapter.currentList[position]
                            viewModel.selectMovie(snappedMovie)
                            movieAdapter.updateSelectedPosition(position)
                        }
                    }
                }
            })
        }
    }

    private fun setupSearch() {
        binding.searchInputLayout.setEndIconOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchMovies(query)
                hideKeyboard()
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> showLoading()
                        is UiState.Success -> {
                            if (uiState.data.isNotEmpty()) {
                                showMovies(uiState.data)
                            } else {
                                showError("No results found.")
                            }
                        }
                        is UiState.Error -> showError(uiState.message)
                        UiState.None -> {}
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE
    }

    private fun showMovies(movies: List<Movie>) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE

        movieAdapter.submitList(movies)
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.errorTextView.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        recyclerViewState = binding.recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}




