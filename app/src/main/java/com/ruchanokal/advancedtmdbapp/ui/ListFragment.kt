package com.ruchanokal.advancedtmdbapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.ruchanokal.advancedtmdbapp.R
import com.ruchanokal.advancedtmdbapp.databinding.FragmentListBinding
import com.ruchanokal.advancedtmdbapp.model.NowPlaying
import com.ruchanokal.advancedtmdbapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private val TAG = "ListFragment"
    private var binding: FragmentListBinding? = null
    private lateinit var viewModel : MovieViewModel

    private var upcomingMoviesAdapter  = UpcomingMoviesAdapter(arrayListOf())
    private var nowPlayingData : NowPlaying? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_list,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = androidx.lifecycle.ViewModelProvider(this).get(MovieViewModel::class.java)
        viewModel.getMovieDatas()
        initializeRecyclerViews()
        observeLiveData()

        binding!!.tryAgainImage.setOnClickListener {
            viewModel.getMovieDatas()
        }

        binding!!.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getMovieDatas()
            binding!!.swipeRefreshLayout.isRefreshing = false
        }



    }

    private fun initializeRecyclerViews() {

        val llm  =  LinearLayoutManager(requireContext())
        binding!!.upcomingRecyclerView.layoutManager = llm

        //RecyclerViewlarla adapterları bağlıyoruz
        binding!!.upcomingRecyclerView.adapter = upcomingMoviesAdapter
    }


    private fun observeLiveData() {

        //MainViewModelda yapılan değişiklikleri burada gözlemliyoruz
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

        viewModel.nowPlayingMovieDetailsData.observe(viewLifecycleOwner, Observer { nowPlayingMovieDetailsData ->

            nowPlayingMovieDetailsData?.let {
                binding!!.contentViewLayout.visibility = View.VISIBLE
                binding!!.imageSlider.setImageList(it)

                binding!!.imageSlider.setItemClickListener(object: ItemClickListener{
                    override fun onItemSelected(position: Int) {

                        Log.i(TAG,"onClicked " )

                        it.let {

                            Log.i(TAG,"onClicked-2 " + nowPlayingData?.results)

                            val action = ListFragmentDirections.actionListFragmentToMovieDetailsFragment(nowPlayingData?.results?.get(position)!!.id)
                            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                            val navController = navHostFragment.navController
                            navController.navigate(action)
                        }

                    }
                })

            }



        })


        viewModel.nowPlayingMovieData.observe(viewLifecycleOwner, Observer { it ->

            it?.let {
                nowPlayingData = it
            }

        })




        viewModel.upcomingMovieDetailsData.observe(viewLifecycleOwner, Observer { upcomingMovieDetailsData ->

            upcomingMovieDetailsData?.let {
                binding!!.contentViewLayout.visibility = View.VISIBLE
                val results = it.results
                upcomingMoviesAdapter.updateMovieList(results)
            }
        })


    }
}