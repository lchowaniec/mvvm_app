package com.lchowaniec.mvvm_app.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.lchowaniec.mvvm_app.data.api.MovieDB
import com.lchowaniec.mvvm_app.data.vo.movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val apiService: MovieDB,private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int,movie>() {
    val moviesLiveDataSource = MutableLiveData<MovieDataSourcec>()
    override fun create(): DataSource<Int, movie> {
        val movieDataSource = MovieDataSourcec(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}