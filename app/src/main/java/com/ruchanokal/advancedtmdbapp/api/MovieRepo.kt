package com.ruchanokal.advancedtmdbapp.api

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import androidx.lifecycle.MutableLiveData
import com.denzcoskun.imageslider.models.SlideModel
import com.ruchanokal.advancedtmdbapp.api.Constants.Companion.IMAGE_BASE_URL
import com.ruchanokal.advancedtmdbapp.model.MovieModelById
import com.ruchanokal.advancedtmdbapp.model.NowPlaying
import com.ruchanokal.advancedtmdbapp.model.Result
import com.ruchanokal.advancedtmdbapp.model.UpcomingMovies
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieRepo @Inject constructor(private val api : MovieAPI) {

    private val TAG = "MovieRepo"


    fun getUpcomingData() : Observable<UpcomingMovies> {
        return api.getUpcomingData()
    }

    fun getNowPlayingData() : Observable<NowPlaying> {
        return api.getNowPlaying()
    }

    fun getMovieById(id : Int) : Single<MovieModelById> {
        return api.getMovieById(id)
    }

    fun makeApiCall(compositeDisposable: CompositeDisposable,
                    upcomingLiveData: MutableLiveData<UpcomingMovies>,
                    nowPlayingLiveDataResults : MutableLiveData<List<SlideModel>>,
                    nowPlayingMovieData : MutableLiveData<NowPlaying>,
                    movieLoading: MutableLiveData<Boolean>,
                    movieError: MutableLiveData<Boolean>) {

        movieLoading.postValue(true)

        val requests = ArrayList<Observable<*>>()
        requests.add(getUpcomingData())
        requests.add(getNowPlayingData())

        // Burada Observable.zip yaparak birden fazla requesti doğru bir şekilde yapıyoruz.
        // Bir request tamamlandıktan sonra diğer requeste geçiliyor. Bu sayede Network
        // Exception gerçekleşmiyor ve doğru bir şekilde sonuçları almış oluyoruz.

        compositeDisposable.add(Observable
            .zip(requests) {

                if( it != null){

                    movieLoading.postValue(false)
                    movieError.postValue(false)

                    val upcomingMovies = it.get(0) as UpcomingMovies
                    val nowPlaying = it.get(1) as NowPlaying

                    upcomingLiveData.postValue(upcomingMovies)
                    nowPlayingMovieData.postValue(nowPlaying)

                    // ImageSlider için bir liste oluşturuyoruz ve MutableLiveData değişkenine atayacağız.
                    val imageList = ArrayList<SlideModel>()


                    for (result in nowPlaying.results) {

                        val imageUrl = IMAGE_BASE_URL + result.backdropPath
                        imageList.add(SlideModel(imageUrl,result.originalTitle, ScaleTypes.CENTER_CROP))
                    }

                    nowPlayingLiveDataResults.postValue(imageList)

                }
                Any()

            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }) {

                it.stackTrace
                Log.e(TAG,"error: " + it)
                movieError.value = true
                movieLoading.value  = false
            })


    }


    fun makeApiCall2(compositeDisposable: CompositeDisposable,
                     movieLoading: MutableLiveData<Boolean>,
                     id : Int,
                     movieData: MutableLiveData<MovieModelById>,
                     movieError: MutableLiveData<Boolean>) {

        Log.e(TAG,"get search movie datas")

        movieLoading.postValue(true)

        compositeDisposable.add(getMovieById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<MovieModelById>(){
                override fun onSuccess(t: MovieModelById) {
                    movieLoading.postValue(false)
                    movieError.postValue(false)
                    movieData.postValue(t)

                }

                override fun onError(e: Throwable) {

                    Log.e(TAG,"error: " + e.localizedMessage)
                    movieLoading.postValue(false)
                    movieError.postValue(true)
                }

            }))


    }

    fun goToIMDBLink(imdbId : String?,activity : FragmentActivity) {

        val urlIntent = Intent(Intent.ACTION_VIEW)
        val URL = "https://www.imdb.com/title/${imdbId}"
        urlIntent.data = Uri.parse(URL)
        activity.startActivity(urlIntent)

    }

}