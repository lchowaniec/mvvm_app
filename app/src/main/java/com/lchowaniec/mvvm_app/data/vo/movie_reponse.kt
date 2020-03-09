package com.lchowaniec.mvvm_app.data.vo


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class movie_reponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movieList: List<movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)