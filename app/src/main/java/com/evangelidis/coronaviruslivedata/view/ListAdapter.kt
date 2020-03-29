package com.evangelidis.coronaviruslivedata.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evangelidis.coronaviruslivedata.R
import com.evangelidis.coronaviruslivedata.model.CoronavirusDataResponseItem
import kotlinx.android.synthetic.main.item_country.view.*

class ListAdapter(var coronavirusData: ArrayList<CoronavirusDataResponseItem>) :
    RecyclerView.Adapter<ListAdapter.CoronavirusViewHolder>() {

    fun updateData(newData: ArrayList<CoronavirusDataResponseItem>) {
        coronavirusData.clear()
        coronavirusData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CoronavirusViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
    )

    override fun getItemCount() = coronavirusData.size

    override fun onBindViewHolder(holder: CoronavirusViewHolder, position: Int) {
        holder.bind(coronavirusData[position])
    }

    class CoronavirusViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val countryName = view.countryName
        private val totalCases = view.totalCases
        private val newCases = view.newCases
        private val totalDeaths = view.totalDeaths
        private val newDeaths = view.newDeaths

        fun bind(item: CoronavirusDataResponseItem) {

            countryName.text = item.country
            totalCases.text = item.cases.toString()
            totalDeaths.text = item.deaths.toString()

            if (item.todayCases == 0){
                newCases.text = item.todayCases.toString()
            } else{
                newCases.text = "+"+item.todayCases.toString()
            }

            if (item.todayDeaths == 0){
                newDeaths.text = item.todayDeaths.toString()
            } else{
                newDeaths.text = "+"+item.todayDeaths.toString()
            }

        }
    }
}