package com.ruchanokal.advancedtmdbapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ruchanokal.advancedtmdbapp.R
import com.ruchanokal.advancedtmdbapp.databinding.RowUpcomingMoviesBinding

class UpcomingMoviesAdapter(val upcomingMovies: ArrayList<com.ruchanokal.advancedtmdbapp.model.Result>) : RecyclerView.Adapter<UpcomingMoviesAdapter.MovieHolder>() {


    class MovieHolder(val binding: RowUpcomingMoviesBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = DataBindingUtil.inflate<RowUpcomingMoviesBinding>(LayoutInflater.from(parent.context), R.layout.row_upcoming_movies,parent,false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {

        //Data Binding kütüphanesi sayesinde listenin itemini sadece bir değişkene atıyoruz ve bu sayede
        //imageview ve textviewlara ayrı ayrı değer atamamıza gerek kalmıyor.
        holder.binding.nowplayingmovies = upcomingMovies.get(position)

        holder.itemView.setOnClickListener {

            val action = ListFragmentDirections.actionListFragmentToMovieDetailsFragment(upcomingMovies.get(position).id)
            Navigation.findNavController(it).navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return upcomingMovies.size
    }

    //adapterı güncellemek için yazılan fonksiyon
    fun updateMovieList(myNewList : List<com.ruchanokal.advancedtmdbapp.model.Result>){
        upcomingMovies.clear()
        upcomingMovies.addAll(myNewList)
        notifyDataSetChanged()
    }


}