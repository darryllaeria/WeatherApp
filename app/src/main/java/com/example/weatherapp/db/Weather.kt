package com.example.weatherapp.db

data class Weather (
        var consolidated_weather: List<ConsolidatedWeather>,
        var time: String? = "",
        var sun_rise: String? = "",
        var sun_set: String? = "",
        var timezone_name: String? = "",
        var parent: Parent,
        var sources: List<Sources>,
        var title: String? = "",
        var location_type: String? = "",
        var woeid: Int = 0,
        var latt_long: String? = "",
        var timezone: String? = ""
)

data class ConsolidatedWeather (
    var id: Long = 0L,
    var weather_state_name: String? = "",
    var weather_state_abbr: String? = "",
    var wind_direction_compass: String? = "",
    var created: String? = "",
    var applicable_date: String? = "",
    var min_temp: Double = 0.0,
    var max_temp: Double = 0.0,
    var the_temp: Double = 0.0,
    var wind_speed: Double = 0.0,
    var wind_direction: Double = 0.0,
    var air_pressure: Double = 0.0,
    var humidity: Int = 0,
    var visibility: Double = 0.0,
    var predictability: Int = 0
)

data class Parent (
    var title: String? = "",
    var location_type: String? = "",
    var woeid: Int = 0,
    var latt_long: String? = ""
)

data class Sources (
    var title: String? = "",
    var slug: String? = "",
    var url: String? = "",
    var crawl_rate: String? = ""
)