package com.lchowaniec.mvvm_app.ui.popular_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.lchowaniec.mvvm_app.R
import com.lchowaniec.mvvm_app.data.api.MovieDB
import com.lchowaniec.mvvm_app.data.api.MovieDBClient
import com.lchowaniec.mvvm_app.data.repository.NetworkState
import com.lchowaniec.mvvm_app.ui.single_movie.SingleMovieViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var  viewModel: MainActivityViewModel

    lateinit var movieRepository: MoviePageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val apiService: MovieDB = MovieDBClient.getClient()
        movieRepository = MoviePageListRepository(apiService)
        viewModel = getViewModel()

        val movieAdapter = PopularMoviePageListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,3)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if(viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3

            }

        }
        rec_view.layoutManager = gridLayoutManager
        rec_view.setHasFixedSize(true)
        rec_view.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            error_text.visibility = if(viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })




    }
    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}
