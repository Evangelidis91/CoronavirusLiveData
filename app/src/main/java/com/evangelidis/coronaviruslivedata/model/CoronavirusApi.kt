package com.evangelidis.coronaviruslivedata.model

import io.reactivex.Single
import retrofit2.http.GET

interface CoronavirusApi {

    @GET("countries")
    fun getCountriesList(): Single<CoronavirusDataResponse>
}