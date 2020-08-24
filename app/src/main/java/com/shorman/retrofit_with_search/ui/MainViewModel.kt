package com.shorman.retrofit_with_search.ui

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shorman.retrofit_with_search.models.CoronaModelItem
import com.shorman.retrofit_with_search.models.CountryModelItem
import com.shorman.retrofit_with_search.retrofit.CoronaApi
import com.shorman.retrofit_with_search.utility.Resuorce
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(val api:CoronaApi):ViewModel() {

    var coronaCases = MutableLiveData<Resuorce<List<CoronaModelItem>>>()
    var countries = MutableLiveData<Resuorce<List<CountryModelItem>>>()
    var nothingFound = MutableLiveData<Boolean>()

    init {
        getCoronaCases("")
        getCountries()
        nothingFound.value=  false

    }


    fun getCoronaCases(country:String) = viewModelScope.launch {
        coronaCases.postValue(Resuorce.Loading())
        val response = api.getCases(country)
        if(response.body().isNullOrEmpty()){
            nothingFound.value = true
        }
        coronaCases.postValue(handleCasesResponse(response))
    }


    private fun getCountries()= viewModelScope.launch {
        countries.postValue(Resuorce.Loading())
        val response = api.getCountries()
        countries.postValue(handleCountryResponse(response))
    }


    private fun handleCasesResponse(response: Response<List<CoronaModelItem>>):Resuorce<List<CoronaModelItem>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resuorce.Success(it)
            }
        }
        return Resuorce.Error(response.message())
    }


    private fun handleCountryResponse(response: Response<List<CountryModelItem>>):Resuorce<List<CountryModelItem>>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resuorce.Success(it)
            }
        }
        return Resuorce.Error(response.message())
    }

}