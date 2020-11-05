package com.example.weatherapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.db.ConsolidatedWeather

class WeatherAdapter(
        private val weathers : ArrayList<ConsolidatedWeather>,
        private val weatherTitle: String?,
        private val weatherWoeid: Int = 0,
        private val context: Context,
        val onItemSelected: (action: Int, weather: ConsolidatedWeather?, weatherTitle: String?, weatherWoeid: Int, position: Int) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.weather_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weathers[position]

        val tvWeatherId = holder.itemView.findViewById<TextView>(R.id.tvWeatherId)
        tvWeatherId.text = String.format(context.getString(R.string.weather_id), weather.id.toString())

        val tvWeatherState = holder.itemView.findViewById<TextView>(R.id.tvWeatherState)
        tvWeatherState.text = String.format(context.getString(R.string.weather_state), weather.weather_state_name)

        val clWeatherItem = holder.itemView.findViewById<ConstraintLayout>(R.id.clWeatherItem)
        clWeatherItem.setOnClickListener {
            onItemSelected(Actions.ACTION_CLICK, weather, weatherTitle, weatherWoeid, position)
        }
    }

    override fun getItemCount() = weathers.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    object Actions {
        const val ACTION_CLICK = 0
    }
}