package com.lchowaniec.mvvm_app.data.api

import com.lchowaniec.mvvm_app.data.vo.MovieDetails
import com.lchowaniec.mvvm_app.data.vo.movie_reponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDB {


    @GET("movie/{movie_id}")
    fun getDetails(@Path("movie_id") id:Int): Single<MovieDetails>
    @GET("movie/popular")
    fun getPopular(@Query("page") page:Int): Single<movie_reponse>
}