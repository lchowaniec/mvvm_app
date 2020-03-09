package com.lchowaniec.mvvm_app.ui.single_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lchowaniec.mvvm_app.data.repository.NetworkState
import com.lchowaniec.mvvm_app.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieRepository:MovieDetailsRepository,movieId:Int) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchingSingleMovieDetails(compositeDisposable,movieId)
    }
    val networkState: LiveData<NetworkState> by lazy{
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
            super.onCleared()
        compositeDisposable.dispose()

    }

}