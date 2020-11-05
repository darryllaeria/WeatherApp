package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.db.ConsolidatedWeather
import com.example.weatherapp.db.Weather
import com.google.gson.Gson
import io.paperdb.Paper
import okhttp3.*
import org.aviran.cookiebar2.CookieBar
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.simpleName
    private lateinit var rvWeather: RecyclerView
    private lateinit var pbLoading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Paper.init(applicationContext)
        apiCall()
    }

    private fun apiCall() {
        val request = Request.Builder().url(Constants.END_POINT).build()
        pbLoading = findViewById(R.id.pbLoading)
        pbLoading.visibility = View.VISIBLE

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                pbLoading.visibility = View.GONE
                Toast.makeText(applicationContext, getString(R.string.error_message), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val response = response.body!!.string()
                    val weatherJson = Gson().fromJson(response, Weather::class.java)
                    Paper.book().write(Constants.CONSOLIDATED_WEATHER, weatherJson.consolidated_weather)
                            .write(Constants.WEATHER_TIME, weatherJson.time)
                            .write(Constants.WEATHER_SUN_RISE, weatherJson.sun_rise)
                            .write(Constants.WEATHER_SUN_SET, weatherJson.sun_set)
                            .write(Constants.WEATHER_TIMEZONE_NAME, weatherJson.timezone_name)
                            .write(Constants.WEATHER_PARENT, weatherJson.parent)
                            .write(Constants.WEATHER_SOURCES, weatherJson.sources)
                            .write(Constants.WEATHER_TITLE, weatherJson.title)
                            .write(Constants.WEATHER_LOCATION_TYPE, weatherJson.location_type)
                            .write(Constants.WEATHER_WOEID, weatherJson.woeid)
                            .write(Constants.WEATHER_LATT_LONG, weatherJson.latt_long)
                            .write(Constants.WEATHER_TIMEZONE, weatherJson.timezone)

                    runOnUiThread {
                        pbLoading.visibility = View.GONE
                        initUI()
                    }
                }
            }
        })
    }

    private fun initUI() {
        setSupportActionBar(findViewById(R.id.toolbar))
        val weatherData: ArrayList<ConsolidatedWeather> = Paper.book().read(Constants.CONSOLIDATED_WEATHER)
        val weatherTitle: String? = Paper.book().read(Constants.WEATHER_TITLE)
        val weatherWoeid: Int = Paper.book().read(Constants.WEATHER_WOEID)

        if(!weatherData.isNullOrEmpty()) {
            rvWeather = findViewById(R.id.rvWeather)
            rvWeather.layoutManager = LinearLayoutManager(this)
            rvWeather.adapter = WeatherAdapter(weatherData, weatherTitle, weatherWoeid, this) { action, weather, weatherTitle, weatherWoeid, position ->
                when(action) {
                    WeatherAdapter.Actions.ACTION_CLICK -> {
                        CookieBar.build(this)
                                .setCustomView(R.layout.dialog_list_item)
                                .setBackgroundColor(R.color.teal_200)
                                .setCustomViewInitializer { view ->
                                    try {
                                        if (weather != null) {
                                            view.findViewById<TextView>(R.id.tvTitleWeatherState).text = String.format(getString(R.string.weather_state), weather.weather_state_name)
                                            view.findViewById<TextView>(R.id.tvWindDirection).text = String.format(getString(R.string.wind_direction), weather.wind_direction)
                                            view.findViewById<TextView>(R.id.tvTemperature).text = String.format(getString(R.string.temperature), weather.min_temp, weather.max_temp)
                                            view.findViewById<TextView>(R.id.tvWindSpeed).text = String.format(getString(R.string.wind_speed), weather.wind_speed)
                                            view.findViewById<TextView>(R.id.tvHumidity).text = String.format(getString(R.string.humidity), weather.humidity)
                                            view.findViewById<TextView>(R.id.tvVisibility).text = String.format(getString(R.string.visibility), weather.visibility)
                                            view.findViewById<TextView>(R.id.tvPredictability).text = String.format(getString(R.string.predictability), weather.predictability)
                                            view.findViewById<TextView>(R.id.tvApplicableDate).text = String.format(getString(R.string.predictability), weather.applicable_date)
                                        } else {
                                            Toast.makeText(applicationContext, getString(R.string.error_message), Toast.LENGTH_LONG).show()
                                        }

                                        val tvLocationTitle = view.findViewById<TextView>(R.id.tvLocationTitle)
                                        if(!weatherTitle.isNullOrEmpty()) {
                                            tvLocationTitle.text = "$weatherTitle - $weatherWoeid"
                                        } else {
                                            tvLocationTitle.visibility = View.GONE
                                        }
                                    } catch (e: Exception) {
                                        Log.d(TAG, e.localizedMessage)
                                        Toast.makeText(applicationContext, getString(R.string.error_message), Toast.LENGTH_LONG).show()
                                    }
                                }
                                .setEnableAutoDismiss(false)
                                .setSwipeToDismiss(true)
                                .setCookiePosition(Gravity.BOTTOM)
                                .show()
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, getString(R.string.error_message), Toast.LENGTH_LONG).show()
        }
    }
}