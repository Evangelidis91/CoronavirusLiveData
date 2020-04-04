package com.evangelidis.coronaviruslivedata.model

import com.evangelidis.coronaviruslivedata.model.allcountries.CoronavirusDataResponseItem

interface OnGetCountryCallback {

    fun onClick(coronavirusDataResponseItem: CoronavirusDataResponseItem)
}