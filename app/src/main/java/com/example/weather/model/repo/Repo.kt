package com.example.weather.model.repo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weather.db.DBManager
import com.example.weather.helper.Constants
import com.example.weather.model.AlertData
import com.example.weather.model.Setting
import com.example.weather.model.WeatherForecast
import com.example.weather.networking.NetworkingManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class Repo @Inject constructor (var networkingManager: NetworkingManager,
           var dbManager: DBManager,
           var context: Context,
           var sharedPreferences: SharedPreferences
) : RepoInterFace {

    private  val TAG = "Repo"

    companion object{
        private var instance: Repo? = null
        fun getInstance(networkingManager: NetworkingManager,
                        dbManager: DBManager,
                        context: Context,
                        appSHP:SharedPreferences): Repo {
            return instance ?: Repo(networkingManager,dbManager,
         context,appSHP)
        }
    }

    //*********************************** RetroFit ************************************************
      override suspend fun getCurrentWeatherWithLocationInRepo(lat:Double, long:Double, unit:String): WeatherForecast {

        if(getSettingsSharedPreferences()?.language as Boolean){

            val weatherinrepo = networkingManager.getWeatherByLocation(lat,long,unit, Constants.languages.en.langValue)
           // Log.i(TAG, "getCurrentWeatherWithLocationInRepo: ${weatherinrepo.current.weather[0].description} ")
            return weatherinrepo
        }



        val weatherinrepo2 = networkingManager.getWeatherByLocation(lat,long,unit,Constants.languages.ar.langValue)
        Log.i("TAG", "getCurrentWeatherWithLocationInRepo: ${weatherinrepo2.current.weather[0].description} ")
        return weatherinrepo2
    }


    //**************************** Alerts *******************************************
    override fun getAllAlertsInRepo(): LiveData<List<AlertData>> {
        return dbManager.getAllStoredAlerts()
    }

     override fun insertAlertInRepo(alert: AlertData) {
        dbManager.insertAlert(alert)
    }

     override fun deleteAlertInRepo(alert: AlertData) {
        dbManager.deleteAlert(alert)
    }
    //***************************** Favorites ROOM *****************************************

     var storedWeathers: List<WeatherForecast>? = null
    var searchWeather : WeatherForecast? = null


    override fun storedLocations() = flow {
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
    override fun offlineFav() = flow {
        dbManager.getAll()
            .collect{
                storedWeathers = it
            }
        emit(storedWeathers)
    }
    override fun insertWeatherDB(weather: WeatherForecast) {
        dbManager.insertWeather(weather)
    }

    override fun deleteFavoriteWeather(weather: WeatherForecast) {
        dbManager.deleteWeather(weather)
    }

    //******************************* Home  Room ******************************************************
    override fun searchWithLatLong (latLong: LatLng) = flow {
         dbManager.search(latLong).collect{
             searchWeather = it
         }
        if(searchWeather !=null) {
            emit(searchWeather)
        }
    }
    override fun deletePreviousHome(loc:LatLng) {
        dbManager.deltePrevHome(loc)
    }

    //************************* SHARED PREFRENCE **********************************************
    override fun addSettingsToSharedPreferences(setting: Setting) {
        var prefEditor = sharedPreferences.edit()
        var gson= Gson()
        var settingStr = gson.toJson(setting)
        prefEditor.putString(Constants.MY_SETTINGS_PREFS,settingStr)
        prefEditor.commit()
        Log.i(TAG, "addSettingsToSharedPreferences: ${setting.location}")
    }

      override fun getSettingsSharedPreferences(): Setting? {
        var settingStr = sharedPreferences.getString(Constants.MY_SETTINGS_PREFS,"")
        var gson= Gson()
        var settingObj: Setting? = gson.fromJson(settingStr, Setting::class.java)
          Log.i(TAG, "get settings from sp: ${settingObj?.location}")
        return settingObj
    }

    override fun add_HomeLocToSP(latLong: LatLng) {
        var prefEditor = sharedPreferences.edit()
        var gson= Gson()
        var weatherStr = gson.toJson(latLong)
        prefEditor.putString(Constants.Home_Loc,weatherStr)
        prefEditor.commit()
    }
    override fun get_HomeLocSP(): LatLng? {
        var latLong = sharedPreferences.getString(Constants.Home_Loc,"")
        var gson= Gson()
        var location:LatLng? = gson.fromJson(latLong,LatLng::class.java)
        return location
    }
      override fun add_LatLongToSP(latLong: LatLng) {
        var prefEditor = sharedPreferences.edit()
        var gson= Gson()
        var weatherStr = gson.toJson(latLong)
        prefEditor.putString(Constants.MY_CURRENT_LOCATION,weatherStr)
        prefEditor.commit()
    }

      override fun get_LatLongSP(): LatLng? {
        var latLong = sharedPreferences.getString(Constants.MY_CURRENT_LOCATION,"")
        var gson= Gson()
        var location:LatLng? = gson.fromJson(latLong,LatLng::class.java)
        return location
      }


}