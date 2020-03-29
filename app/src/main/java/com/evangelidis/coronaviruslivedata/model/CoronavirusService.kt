package com.evangelidis.coronaviruslivedata.model

import com.evangelidis.coronaviruslivedata.di.DaggerApiComponent
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
}