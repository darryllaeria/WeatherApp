package com.example.weatherapp

object Constants {
    private const val WOEID_BRISBANE = 1100661
    const val END_POINT = "https://www.metaweather.com/api/location/${WOEID_BRISBANE}/"

    const val CONSOLIDATED_WEATHER = "consolidated_weather"
    const val WEATHER_TIME = "time"
    const val WEATHER_SUN_RISE = "sun_rise"
    const val WEATHER_SUN_SET = "sun_set"
    const val WEATHER_TIMEZONE_NAME = "timezone_name"
    const val WEATHER_PARENT = "parent"
    const val WEATHER_SOURCES = "sources"
    const val WEATHER_TITLE = "title"
    const val WEATHER_LOCATION_TYPE = "location_type"
    const val WEATHER_WOEID = "woeid"
    const val WEATHER_LATT_LONG = "latt_long"
    const val WEATHER_TIMEZONE = "timezone"
}