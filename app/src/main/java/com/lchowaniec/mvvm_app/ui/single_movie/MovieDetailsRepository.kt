package com.lchowaniec.mvvm_app.ui.single_movie

import androidx.lifecycle.LiveData
import com.lchowaniec.mvvm_app.data.api.MovieDB
import com.lchowaniec.mvvm_app.data.repository.MovieDetailsNetworkDataSource
import com.lchowaniec.mvvm_app.data.repository.NetworkState
import com.lchowaniec.mvvm_app.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: MovieDB) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchingSingleMovieDetails(compositeDisposable: CompositeDisposable,movieId:Int): LiveData<MovieDetails>{

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse

    }
    fun getMovieDetailsNetworkState():LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }


}