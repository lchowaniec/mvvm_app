package com.lchowaniec.mvvm_app.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.lchowaniec.mvvm_app.data.api.FIRST_PAGE
import com.lchowaniec.mvvm_app.data.api.MovieDB
import com.lchowaniec.mvvm_app.data.vo.movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSourcec(private val apiService: MovieDB, private val compositeDisposable: CompositeDisposable):PageKeyedDataSource<Int, movie>(){

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, movie>) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopular(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.movieList,null,page+1)
                        networkState.postValue(NetworkState.LOADED)

                    },
                {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MovieDataSource error: ", it.message)

                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopular(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages >= params.key){
                            callback.onResult(it.movieList,params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }else{
                            networkState.postValue(NetworkState.ENDLIST)
                        }

                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource error: ", it.message)

                    })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, movie>) {
    }

}