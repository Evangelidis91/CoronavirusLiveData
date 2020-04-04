package com.evangelidis.coronaviruslivedata.model

import com.evangelidis.coronaviruslivedata.di.DaggerApiComponent
import com.evangelidis.coronaviruslivedata.model.allcountries.CoronavirusDataResponse
import com.evangelidis.coronaviruslivedata.model.allcountries.CoronavirusDataResponseItem
import com.evangelidis.coronaviruslivedata.model.countryfinaldata.CountryFinalDataResponse
import io.reactivex.Single
import javax.inject.Inject

class CoronavirusService {

    @Inject
    lateinit var api: CoronavirusApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getCountriesList(): Single<CoronavirusDataResponse> {
        return api.getCountriesList()
    }

    fun getWorldData(): Single<CoronavirusDataResponseItem>{
        return api.getWorldTotalData()
    }

    fun getDataPerCountry(countryName: String): Single<CountryFinalDataResponse>{
        return api.getCountryFinalData(countryName)
    }
}