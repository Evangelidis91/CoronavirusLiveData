package com.evangelidis.coronaviruslivedata.model

data class CoronavirusDataResponseItem(
    val active: Int,
    val cases: Int,
    val casesPerOneMillion: Double,
    val country: String,
    val countryInfo: CountryInfo,
    val critical: Int,
    val deaths: Int,
    val deathsPerOneMillion: Double,
    val recovered: Int,
    val todayCases: Int,
    val todayDeaths: Int
)