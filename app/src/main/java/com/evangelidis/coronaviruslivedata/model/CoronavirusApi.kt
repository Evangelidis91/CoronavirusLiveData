package com.evangelidis.coronaviruslivedata.model

import com.evangelidis.coronaviruslivedata.model.allcountries.CoronavirusDataResponse
import com.evangelidis.coronaviruslivedata.model.allcountries.CoronavirusDataResponseItem
import com.evangelidis.coronaviruslivedata.model.countryfinaldata.CountryFinalDataResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CoronavirusApi {

    @GET("countries")
    fun getCountriesList(): Single<CoronavirusDataResponse>

    @GET("all")
    fun getWorldTotalData(): Single<CoronavirusDataResponseItem>

    @GET("countries/{country_name}")
    fun getCountryFinalData(
        @Path("country_name") countryName: String
    ): Single<CountryFinalDataResponse>
}