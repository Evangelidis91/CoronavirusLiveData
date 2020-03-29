package com.evangelidis.coronaviruslivedata.view

import android.content.DialogInterface
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.evangelidis.coronaviruslivedata.R
import com.evangelidis.coronaviruslivedata.viewholder.ListViewModel
import com.evangelidis.tantintoast.TanTinToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private val coronavirusListAdapter = ListAdapter(arrayListOf())

    private val sortValue =
        mutableListOf("countryName", "totalCases", "newCases", "totalDeaths", "newDeaths")
    private val sortType = mutableListOf("asc", "desc")

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

        setUpvariables()

        observeViewModel()
    }

    private fun setUpvariables() {
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
        viewModel.coronavirusData.observe(this, Observer { data ->
            data?.let {
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