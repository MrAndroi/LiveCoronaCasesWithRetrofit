package com.shorman.retrofit_with_search.utility

sealed class Resuorce<T>(
    val data:T? = null,
    val massege:String?=null
) {

    class Success<T>(data: T) :Resuorce<T>(data)
    class Error<T> (massege: String,data: T? = null):Resuorce<T>(data,massege)
    class Loading<T>:Resuorce<T>()


}