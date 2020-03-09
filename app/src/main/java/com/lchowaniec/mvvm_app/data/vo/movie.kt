package com.lchowaniec.mvvm_app.data.vo


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class movie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String

)