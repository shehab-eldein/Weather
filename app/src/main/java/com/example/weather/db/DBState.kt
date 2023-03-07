package com.example.weather.db

import com.example.weather.model.WeatherForecast

sealed class DBState{
    class onSuccessList(val weatherList : List<WeatherForecast> ):DBState()
    class onSuccessWeather(val weather: WeatherForecast):DBState()
    class onFail(val msg: Throwable ):DBState()
    object Loading : DBState()
}




