package com.example.weather.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.safyweather.Constants
import com.example.weather.db.DBManager
import com.example.weather.networking.NetworkingManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val TAG = "Repo"
class Repo(var networkingManager: NetworkingManager,
           var dbManager: DBManager,
           var context: Context,
           var sharedPreferences: SharedPreferences
)  {


    companion object{
        private var instance:Repo? = null
        fun getInstance(networkingManager: NetworkingManager,
                        dbManager: DBManager,
                        context: Context,
                        appSHP:SharedPreferences):Repo{
            return instance?: Repo(networkingManager,dbManager,
         context,appSHP)
        }
    }

    //*********************************** RetroFit ************************************************
      suspend fun getCurrentWeatherWithLocationInRepo(lat:Double, long:Double, unit:String): WeatherForecast {
        Log.i(TAG, "getCurrentWeatherWithLocationInRepoooooooooooooo: ")



        if(getSettingsSharedPreferences()?.language as Boolean){

            val weatherinrepo = networkingManager.getWeatherByLocation(lat,long,unit,Constants.languages.en.langValue)
            Log.i(TAG, "getCurrentWeatherWithLocationInRepo: ${weatherinrepo.current.weather[0].description} ")
            return weatherinrepo
        }



        val weatherinrepo2 = networkingManager.getWeatherByLocation(lat,long,unit,Constants.languages.ar.langValue)
        Log.i("TAG", "getCurrentWeatherWithLocationInRepo: ${weatherinrepo2.current.weather[0].description} ")
        return weatherinrepo2
    }


    //***************************** ROOM *****************************************

     var storedWeathers: List<WeatherForecast>? = null
    var searchWeather :WeatherForecast? = null


    fun storedLocations() = flow {
        var updatedWeather: WeatherForecast
        dbManager.getAll()
            .collect{
           storedWeathers = it

        }
        if (storedWeathers != null) {
            for (weather in storedWeathers!!) {
                updatedWeather =  getCurrentWeatherWithLocationInRepo(weather.lat,weather.lon,"metric")
                emit(updatedWeather)
            }

        }
    }
    fun insertFavoriteWeather(weather: WeatherForecast) {
        dbManager.insertWeather(weather)
    }

    fun deleteFavoriteWeather(weather: WeatherForecast) {
        dbManager.deleteWeather(weather)
    }

    fun searchWithLatLong (latLong: LatLng) = flow {
         dbManager.search(latLong).collect{
             searchWeather = it
         }
        if(searchWeather !=null) {
            emit(searchWeather)
        }
    }





    //************************* SHARED PREFRENCE **********************************************
      fun addSettingsToSharedPreferences(setting: Setting) {
        var prefEditor = sharedPreferences.edit()
        var gson= Gson()
        var settingStr = gson.toJson(setting)
        prefEditor.putString(Constants.MY_SETTINGS_PREFS,settingStr)
        prefEditor.commit()
        Log.i(TAG, "addSettingsToSharedPreferences: Done")
    }

      fun getSettingsSharedPreferences(): Setting? {
        var settingStr = sharedPreferences.getString(Constants.MY_SETTINGS_PREFS,"")
        var gson= Gson()
        var settingObj:Setting? = gson.fromJson(settingStr,Setting::class.java)
        return settingObj
    }

      fun add_LatLongToSP(latLong: LatLng) {
        var prefEditor = sharedPreferences.edit()
        var gson= Gson()
        var weatherStr = gson.toJson(latLong)
        prefEditor.putString(Constants.MY_CURRENT_LOCATION,weatherStr)
        prefEditor.commit()
    }

      fun get_LatLongSP(): LatLng? {
        var latLong = sharedPreferences.getString(Constants.MY_CURRENT_LOCATION,"")
        var gson= Gson()
        var location:LatLng? = gson.fromJson(latLong,LatLng::class.java)
        return location
      }


}