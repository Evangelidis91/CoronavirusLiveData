package com.evangelidis.coronaviruslivedata.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.evangelidis.coronaviruslivedata.R
import com.evangelidis.coronaviruslivedata.model.allcountries.CoronavirusDataResponseItem
import com.evangelidis.coronaviruslivedata.model.OnGetCountryCallback
import com.evangelidis.coronaviruslivedata.model.allcountries.CoronavirusDataResponse
import com.evangelidis.coronaviruslivedata.utils.InternetStatus
import com.evangelidis.coronaviruslivedata.viewholder.ListViewModel
import com.evangelidis.tantintoast.TanTinToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var callback: OnGetCountryCallback = object :
        OnGetCountryCallback {
        override fun onClick(coronavirusDataResponseItem: CoronavirusDataResponseItem) {
            if (InternetStatus.getInstance(applicationContext).isOnline) {
                val intent = Intent(this@MainActivity, CountryActivity::class.java)
                intent.putExtra("countryName", coronavirusDataResponseItem.country)
                startActivity(intent)
            } else {
                TanTinToast.Warning(this@MainActivity).text("Please check your internet connection")
                    .time(Toast.LENGTH_LONG).show()
            }
        }
    }

    lateinit var viewModel: ListViewModel
    private val coronavirusListAdapter = ListAdapter(arrayListOf(), callback)

    private lateinit var fff: CoronavirusDataResponseItem

    private val sortValue =
        listOf("countryName", "totalCases", "newCases", "totalDeaths", "newDeaths")
    private val sortType = listOf("asc", "desc")

    var selectedSortValue = "totalCases"
    var selectedSortType = "desc"

    var boldTypeface: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh(selectedSortValue, selectedSortType)

        boldTypeface = ResourcesCompat.getFont(this, R.font.montserrat_extra_bold)

        countriesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coronavirusListAdapter
        }

        setUpVariables()
        observeViewModel()
    }

    private fun setUpVariables() {
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refresh(selectedSortValue, selectedSortType)
        }

        countryName.setOnClickListener {
            setupSortVariable(sortValue[0])
            countryName.typeface = boldTypeface
        }

        totalCases.setOnClickListener {
            setupSortVariable(sortValue[1])
            totalCases.typeface = boldTypeface
        }

        newCases.setOnClickListener {
            setupSortVariable(sortValue[2])
            newCases.typeface = boldTypeface
        }

        totalDeaths.setOnClickListener {
            setupSortVariable(sortValue[3])
            totalDeaths.typeface = boldTypeface
        }

        newDeaths.setOnClickListener {
            setupSortVariable(sortValue[4])
            newDeaths.typeface = boldTypeface
        }
    }

    private fun setupSortVariable(s: String) {
        resetHeaderFont()
        if (selectedSortValue == s) {
            selectedSortType = if (selectedSortType == sortType[1]) {
                sortType[0]
            } else {
                sortType[1]
            }
        } else {
            selectedSortValue = s
            selectedSortType = sortType[1]
        }
        viewModel.shuffleData(selectedSortValue, selectedSortType)
    }

    private fun resetHeaderFont() {
        val defaultTypeface = ResourcesCompat.getFont(this, R.font.montserrat_medium)
        countryName.typeface = defaultTypeface
        totalCases.typeface = defaultTypeface
        newCases.typeface = defaultTypeface
        totalDeaths.typeface = defaultTypeface
        newDeaths.typeface = defaultTypeface
    }

    private fun observeViewModel() {

        viewModel.coronavirusWorldData.observe(this, Observer { data ->
            fff = data
        })

        viewModel.coronavirusData.observe(this, Observer { data ->
            data?.let {

                val finder = data.find { it.country == "World" }
                if (finder == null) {
                    addWorldData(data)
                }
                countriesList.visibility = View.VISIBLE
                coronavirusListAdapter.updateData(it)
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
                    countriesList.visibility = View.GONE
                }
            }
        })
    }

    private fun addWorldData(data: CoronavirusDataResponse) {
        var todayTotalCases = 0
        var todayTotalDeaths = 0
        for (x in 0 until data.size) {
            todayTotalCases += data[x].todayCases
            todayTotalDeaths += data[x].todayDeaths
        }
        fff.country = "World"
        fff.todayCases = todayTotalCases
        fff.todayDeaths = todayTotalDeaths

        data.add(0, fff)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_retreive_new_data) {
            viewModel.refresh(selectedSortValue, selectedSortType)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.drawable.ic_virus)
        builder.setMessage(resources.getString(R.string.close_popup_window_title))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.close_the_app_text),
                DialogInterface.OnClickListener { dialog, id -> finish() })
            .setNegativeButton(resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}