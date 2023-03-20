package com.ruchanokal.advancedtmdbapp.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import com.ruchanokal.advancedtmdbapp.api.MovieRepo
import com.ruchanokal.advancedtmdbapp.model.MovieModelById
import com.ruchanokal.advancedtmdbapp.model.NowPlaying
import com.ruchanokal.advancedtmdbapp.model.UpcomingMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository : MovieRepo
) : ViewModel() {

    private val TAG = "MovieViewModel"
    val loadingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Boolean>()
    val movieData = MutableLiveData<MovieModelById>()

    val upcomingMovieDetailsData = MutableLiveData<UpcomingMovies>()
    val nowPlayingMovieData = MutableLiveData<NowPlaying>()
    val nowPlayingMovieDetailsData = MutableLiveData<List<SlideModel>>()
    private val compositeDisposable  = CompositeDisposable()


    fun getMovieDatas() {
        repository.makeApiCall(compositeDisposable,upcomingMovieDetailsData,
            nowPlayingMovieDetailsData,nowPlayingMovieData,loadingLiveData,errorLiveData)
    }


    fun getMovieDataById(id : Int) {
        repository.makeApiCall2(compositeDisposable, loadingLiveData,id,movieData,errorLiveData)
    }

    fun goToIMDBLink(imdbId : String?, activity : FragmentActivity) {
        repository.goToIMDBLink(imdbId,activity)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}