package com.ruchanokal.advancedtmdbapp.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ruchanokal.advancedtmdbapp.R
import com.ruchanokal.advancedtmdbapp.api.Constants.Companion.IMAGE_BASE_URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable){


    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_baseline_error_outline_24)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false

            }
        })
        .into(this)

}

fun placeholderProgressBar(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url:String?) {

    //Example Link : https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg
    val realURL = IMAGE_BASE_URL + url
    view.downloadFromUrl(realURL, placeholderProgressBar(view.context))
}


@BindingAdapter("android:changeRateText")
fun changeRateText(view: TextView, rate:Double?) {

    val formattedDouble = String.format(Locale.US,"%.1f", rate)
    view.text = formattedDouble
}

@BindingAdapter("android:changeDateText")
fun changeText(view: TextView, mydate:String?) {

   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.parse(mydate)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formattedDate = formatter.format(date)
        view.text = formattedDate
    }

}