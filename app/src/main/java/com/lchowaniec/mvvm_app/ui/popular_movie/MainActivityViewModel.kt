package com.lchowaniec.mvvm_app.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.lchowaniec.mvvm_app.data.repository.NetworkState
import com.lchowaniec.mvvm_app.data.vo.movie
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val movieReposity:MoviePageListRepository):ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val moviePagedList: LiveData<PagedList<movie>> by lazy {
        movieReposity.fetchLiveMoviePagedList(compositeDisposable)

    }
    val networkState:LiveData<NetworkState> by lazy {
        movieReposity.getNetworkState()
    }
    fun listIsEmpty():Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
            super.onCleared()
            compositeDisposable.dispose()
    }
}