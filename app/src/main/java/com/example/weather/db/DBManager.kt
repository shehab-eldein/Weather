package com.example.weather.db

import android.content.Context
import android.util.Log
import com.example.weather.model.WeatherForecast
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.flow


class DBManager(context: Context) {

    private  val TAG = "DBManager"
    private var favDao: WeatherDAO


    init {
        val db = WeatherDatabase.getInstance(context.applicationContext)
        favDao = db.addressesDao()

    }

    companion object{
        private var DBManager:DBManager? = null
        fun getInstance(context: Context):DBManager{
            if(DBManager == null){
                DBManager = DBManager(context)
            }
            return DBManager!!
        }
    }

    //***************************** Favorite *********************************************
     fun getAll() =  flow {
        emit(favDao.getAll())
     }

     fun insertWeather(weather: WeatherForecast) {
        favDao.insert (weather)

    }

     fun deleteWeather(weather: WeatherForecast) {
        favDao.delte(weather)
         Log.i(TAG, "insertWeather: Dlete in DBMana")
    }
    fun search(latLong: LatLng) = flow {
      emit(  favDao.searchWithLatLong(latLong.latitude,latLong.longitude))
    }











}