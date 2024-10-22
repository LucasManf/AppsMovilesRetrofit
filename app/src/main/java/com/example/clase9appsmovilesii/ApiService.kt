package com.example.clase9appsmovilesii;

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun getImagesByBreed(@Url url: String): Response<BreedResponse>

    /*@GET
    suspend fun getListOfBreeds(@Url url: String): Response<BreedsResponse>*/
}