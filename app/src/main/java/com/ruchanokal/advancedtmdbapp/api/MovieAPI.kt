package com.ruchanokal.advancedtmdbapp.api

import com.ruchanokal.advancedtmdbapp.model.MovieModelById
import com.ruchanokal.advancedtmdbapp.model.NowPlaying
import com.ruchanokal.advancedtmdbapp.model.UpcomingMovies
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {


    // Upcoming --> https://api.themoviedb.org/3/movie/upcoming?api_key=<<api_key>>&language=en-US&page=1
    // NowPlaying --> https://api.themoviedb.org/3/movie/now_playing?api_key=<<api_key>>&language=en-US&page=1

    @GET("movie/upcoming?api_key=3821d63954d2b0afa52a3e29b642271e")
    fun getUpcomingData() : Observable<UpcomingMovies>

    @GET("movie/now_playing?api_key=3821d63954d2b0afa52a3e29b642271e")
    fun getNowPlaying() : Observable<NowPlaying>

    @GET("movie/{movie_id}?api_key=3821d63954d2b0afa52a3e29b642271e")
    fun getMovieById(@Path("movie_id") id : Int) : Single<MovieModelById>

}