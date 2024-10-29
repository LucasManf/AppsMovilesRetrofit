package com.example.clase9appsmovilesii

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BreedViewModel : ViewModel() {
    val _listado = MutableLiveData<MutableList<String>>()
    val listado: LiveData<MutableList<String>>
        get() = _listado

    fun getListOfBreeds() {
        viewModelScope.launch {
            val call =
                getRetrofit().create(ApiService::class.java).getListOfBreeds("breeds/list/all")
            val response: BreedsResponse? = call.body()
            val list = mutableListOf<String>()

            if (call.isSuccessful) {
                val breeds = response?.message

                breeds?.keys?.forEach {
                    list.add(it)
                }

                _listado.value = list
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}