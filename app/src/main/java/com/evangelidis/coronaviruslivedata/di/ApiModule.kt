package com.evangelidis.coronaviruslivedata.di

import com.evangelidis.coronaviruslivedata.model.CoronavirusApi
import com.evangelidis.coronaviruslivedata.model.CoronavirusService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private val BASE_URL = "https://corona.lmao.ninja"

    @Provides
    fun provideCoronavirusDataApi(): CoronavirusApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CoronavirusApi::class.java)
    }

    @Provides
    fun provideCoronavirusService(): CoronavirusService {
        return CoronavirusService()
    }
}