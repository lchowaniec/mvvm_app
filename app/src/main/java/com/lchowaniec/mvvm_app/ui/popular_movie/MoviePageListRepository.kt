package com.lchowaniec.mvvm_app.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.lchowaniec.mvvm_app.data.api.MovieDB
import com.lchowaniec.mvvm_app.data.api.POST_PER_PAGE
import com.lchowaniec.mvvm_app.data.repository.MovieDataSourceFactory
import com.lchowaniec.mvvm_app.data.repository.MovieDataSourcec
import com.lchowaniec.mvvm_app.data.repository.NetworkState
import com.lchowaniec.mvvm_app.data.vo.movie
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(private val apiService: MovieDB) {
    lateinit var moviePagedList: LiveData<PagedList<movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<movie>>{
        moviesDataSourceFactory = MovieDataSourceFactory(apiService,compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory,config).build()
        return moviePagedList


    }
    fun getNetworkState():LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSourcec,NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource,MovieDataSourcec::networkState
        )

    }
}