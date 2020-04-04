package com.evangelidis.coronaviruslivedata.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.evangelidis.coronaviruslivedata.R
import com.evangelidis.coronaviruslivedata.viewholder.ListViewModel
import com.evangelidis.tantintoast.TanTinToast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_country.*
import java.text.SimpleDateFormat
import java.util.*

class CountryActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    lateinit var countryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_country)

        countryName = intent.getStringExtra("countryName").orEmpty()

        supportActionBar?.title = countryName

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.getCountryData(countryName)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.coronavirusCountryData.observe(this, Observer { data ->
            data?.let {

                Picasso.get().load(data.countryInfo.flag).into(flag)

                totalCases.text =
                    resources.getString(R.string.total_cases_c).replace("_", data.cases.toString())
                totalTodaysCases.text = resources.getString(R.string.todays_cases)
                    .replace("_", data.todayCases.toString())
                totalDeaths.text = resources.getString(R.string.total_deaths_c)
                    .replace("_", data.deaths.toString())
                totalTodaysDeaths.text = resources.getString(R.string.todays_deaths)
                    .replace("_", data.todayDeaths.toString())
                totalRecovered.text =
                    resources.getString(R.string.recovered).replace("_", data.recovered.toString())
                totalActive.text =
                    resources.getString(R.string.active).replace("_", data.active.toString())
                totalCritical.text =
                    resources.getString(R.string.critical).replace("_", data.critical.toString())
                totalCasesPerMillion.text = resources.getString(R.string.cases_per_million)
                    .replace("_", data.casesPerOneMillion.toString())
                totalDeathsPerMillion.text = resources.getString(R.string.deaths_per_million)
                    .replace("_", data.deathsPerOneMillion.toString())
                lastUpdate.text = resources.getString(R.string.last_update)
                    .replace("_", getDateTime(data.updated))
            }
        })

        viewModel.loadError.observe(this, Observer { isError ->
            isError?.let {
                list_error.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    TanTinToast.Warning(this).text("Please check your internet connection.").show()
                }
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                }
            }
        })
    }

    private fun getDateTime(s: Long): String {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
            val netDate = Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}
