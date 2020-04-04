package com.evangelidis.coronaviruslivedata.model.allcountries

data class CoronavirusDataResponseItem(
    val active: Int,
    val cases: Int,
    val casesPerOneMillion: Double,
    var country: String,
    val countryInfo: CountryInfo,
    val critical: Int,
    val deaths: Int,
    val deathsPerOneMillion: Double,
    val recovered: Int,
    var todayCases: Int,
    var todayDeaths: Int,
    val affectedCountries: Int,
    val updated: Long
)