package com.evangelidis.coronaviruslivedata.viewholder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evangelidis.coronaviruslivedata.di.DaggerApiComponent
import com.evangelidis.coronaviruslivedata.model.CoronavirusDataResponse
import com.evangelidis.coronaviruslivedata.model.CoronavirusService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel : ViewModel() {

    @Inject
    lateinit var coronavirusService: CoronavirusService

    private val disposable = CompositeDisposable()

    private var listOfData: CoronavirusDataResponse? = null

    val coronavirusData = MutableLiveData<CoronavirusDataResponse>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refresh(selectedSortValue: String, selectedSortType: String) {
        fetchCountries(selectedSortValue, selectedSortType)
    }

    private fun fetchCountries(selectedSortValue: String, selectedSortType: String) {
        loading.value = true
        disposable.add(
            coronavirusService.getCountriesList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CoronavirusDataResponse>() {
                    override fun onSuccess(response: CoronavirusDataResponse) {
                        coronavirusData.value = response
                        listOfData = response
                        loadError.value = false
                        loading.value = false
                        shuffleData(selectedSortValue, selectedSortType)
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                    }
                })
        )
    }

    private val sortValue =
        mutableListOf("countryName", "totalCases", "newCases", "totalDeaths", "newDeaths")
    private val sortType = mutableListOf("asc", "desc")

    fun shuffleData(selectedSortValue: String, selectedSortType: String) {

        when (selectedSortValue) {
            sortValue[0] -> {
                if (selectedSortType == sortType[0]) {
                    listOfData?.sortBy { it.country }
                } else {
                    listOfData?.sortByDescending { it.country }
                }
            }
            sortValue[1] -> {
                if (selectedSortType == sortType[0]) {
                    listOfData?.sortBy { it.cases }
                } else {
                    listOfData?.sortByDescending { it.cases }
                }
            }
            sortValue[2] -> {
                if (selectedSortType == sortType[0]) {
                    listOfData?.sortBy { it.todayCases }
                } else {
                    listOfData?.sortByDescending { it.todayCases }
                }
            }
            sortValue[3] -> {
                if (selectedSortType == sortType[0]) {
                    listOfData?.sortBy { it.deaths }
                } else {
                    listOfData?.sortByDescending { it.deaths }
                }
            }
            sortValue[4] -> {
                if (selectedSortType == sortType[0]) {
                    listOfData?.sortBy { it.todayDeaths }
                } else {
                    listOfData?.sortByDescending { it.todayDeaths }
                }
            }
        }
        coronavirusData.value = listOfData
    }
}