package com.ruchanokal.advancedtmdbapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ruchanokal.advancedtmdbapp.R
import com.ruchanokal.advancedtmdbapp.databinding.FragmentListBinding
import com.ruchanokal.advancedtmdbapp.databinding.FragmentMovieDetailsBinding
import com.ruchanokal.advancedtmdbapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var binding : FragmentMovieDetailsBinding? = null

    private val TAG = "MovieDetailsFragment"
    private lateinit var viewModel : MovieViewModel
    private var imdbID : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_movie_details,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {

            val movieId = MovieDetailsFragmentArgs.fromBundle(it).movieId

            Log.i(TAG,"movieId: " + movieId)

            viewModel = androidx.lifecycle.ViewModelProvider(this).get(MovieViewModel::class.java)
            viewModel.getMovieDataById(movieId)

            binding!!.tryAgainImage.setOnClickListener {
                viewModel.getMovieDataById(movieId)
            }

            binding!!.swipeRefreshLayout.setOnRefreshListener {
                viewModel.getMovieDataById(movieId)
                binding!!.swipeRefreshLayout.isRefreshing = false
            }

            binding!!.redirectLink.setOnClickListener {

                if (imdbID != null)
                    viewModel.goToIMDBLink(imdbID,requireActivity())


            }

            observeLiveData()

        }

    }

    private fun observeLiveData() {

        viewModel.loadingLiveData.observe(viewLifecycleOwner, Observer { movieLoading ->

            if (movieLoading){

                binding!!.contentViewLayout.visibility = View.GONE
                binding!!.errorTryAgainLayout.visibility = View.GONE
                binding!!.progressBar.visibility = View.VISIBLE

            } else {

                binding!!.progressBar.visibility = View.GONE
            }

        })

        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { movieError ->

            if (movieError){

                binding!!.contentViewLayout.visibility = View.GONE
                binding!!.errorTryAgainLayout.visibility = View.VISIBLE
                binding!!.progressBar.visibility = View.GONE

            } else {

                binding!!.errorTryAgainLayout.visibility = View.GONE
            }

        })

        viewModel.movieData.observe(viewLifecycleOwner, Observer { movieData ->

            movieData?.let {

                binding!!.contentViewLayout.visibility = View.VISIBLE
                binding!!.moviedetails = it
                imdbID = it.imdbId

            }

        })



    }
}