package com.task.network.model

import retrofit2.Response

data class ResponseData<T>(
    var status: Status,
    var code: Int,
    var data: T? = null,
    var message: String? = null
) {
    companion object{
        fun <T> success(code: Int, data: T) = ResponseData<T>(Status.SUCCESS, code,data)

        fun <T> error(code: Int, message: String?) = ResponseData<T>(Status.ERROR, code, null, message)

        fun <T> loading() = ResponseData<T>(Status.LOADING, -1, null)
    }
}

enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}

fun <T> Response<T>.getResponseData(): ResponseData<T> {
    if(isSuccessful){
        this.body()?.let {
            return ResponseData.success(this.code(), it)
        }?: kotlin.run {
            return ResponseData.error(this.code(), this.message().orEmpty())
        }
    }else{
        return ResponseData.error<T>(this.code(), "Empty Response")
    }
}

object StatusCode{
    const val SUCCESS = 200
    const val INTERNAL_SERVER_ERROR = 500
    const val ENTITY_NOT_FOUND = 404
    const val NOT_NETWORK_FOUND = 510
}