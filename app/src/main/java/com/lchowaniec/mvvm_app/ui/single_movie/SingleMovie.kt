package com.lchowaniec.mvvm_app.ui.single_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.lchowaniec.mvvm_app.R
import com.lchowaniec.mvvm_app.data.api.MovieDBClient
import com.lchowaniec.mvvm_app.data.api.POSTER_BASE_URL
import com.lchowaniec.mvvm_app.data.repository.NetworkState
import com.lchowaniec.mvvm_app.data.vo.MovieDetails
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

@Suppress("UNCHECKED_CAST")
class SingleMovie : AppCompatActivity() {

    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var viewModel: SingleMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)
        val movieId = intent.getIntExtra("id",1)
        val apiService = MovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })
        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            error_text.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }
    private fun bindUI(it: MovieDetails){
        movie_title.text = it.title
        movie_budget.text = it.budget.toString()
        movie_ovierview.text = it.overview
        movie_rating.text = it.Rating.toString()
        movie_revenue.text = it.revenue.toString()
        movie_runtime.text = it.runtime.toString() + "minutes"
        movie_tag.text = it.tagline

        val currency = NumberFormat.getCurrencyInstance(Locale.GERMANY)
        movie_budget.text = currency.format(it.budget)
        movie_revenue.text = currency.format(it.revenue)

        val posterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(posterURL)
            .into(movie_poster)


    }
    private fun getViewModel(movieId:Int): SingleMovieViewModel{
        return ViewModelProviders.of(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}
