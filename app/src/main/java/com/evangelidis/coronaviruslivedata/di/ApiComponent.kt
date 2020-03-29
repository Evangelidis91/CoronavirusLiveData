package com.evangelidis.coronaviruslivedata.di

import com.evangelidis.coronaviruslivedata.model.CoronavirusService
import com.evangelidis.coronaviruslivedata.viewholder.ListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: CoronavirusService)

    fun inject(viewModel: ListViewModel)
}