package com.example.weather.Favorite.view

import com.example.weather.model.Location
import com.example.weather.model.WeatherForecast

interface OnFavWeatherClickListener {
    fun onRemoveBtnClick(weather: WeatherForecast)
    fun onFavItemClick(weather: WeatherForecast)
}