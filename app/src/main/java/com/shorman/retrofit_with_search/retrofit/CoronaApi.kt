package com.shorman.retrofit_with_search.retrofit

import com.shorman.retrofit_with_search.models.CoronaModelItem
import com.shorman.retrofit_with_search.models.CountryModelItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CoronaApi {

    @GET("/dayone/country/{country}/status/confirmed/live")
    suspend fun getCases(@Path("country") country:String) : Response<List<CoronaModelItem>>

    @GET("/countries")
    suspend fun getCountries():Response<List<CountryModelItem>>

}