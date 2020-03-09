package com.lchowaniec.mvvm_app.data.repository

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status:Status,val msg:String) {
    companion object{
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val ENDLIST: NetworkState
        init {
            LOADED = NetworkState(Status.SUCCESS,"success")
            LOADING = NetworkState(Status.RUNNING,"loading")
            ERROR = NetworkState(Status.FAILED,"error")
            ENDLIST = NetworkState(Status.FAILED, "You have reached end of list")

        }
    }

}