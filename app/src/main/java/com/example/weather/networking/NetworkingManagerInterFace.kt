package com.example.weather.networking

import com.example.weather.model.WeatherForecast
import retrofit2.Response

interface NetworkingManagerInterFace {
    suspend fun getWeatherByLocation(lat:Double,long:Double,unit:String,lang:String):  WeatherForecast

}