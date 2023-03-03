package com.example.weather.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.safyweather.Constants
import com.example.weather.networking.NetworkingManager
import com.google.gson.Gson

private const val TAG = "Repo"
class Repo(var networkingManager: NetworkingManager,
          // var localSource: LocalSourceInterface,
           var context: Context,
           var sharedPreferences: SharedPreferences
)  {


    companion object{
        private var instance:Repo? = null
        fun getInstance(networkingManager: NetworkingManager,
                       // localSource:LocalSourceInterface,
                        context: Context,
                        appSHP:SharedPreferences):Repo{
            return instance?: Repo(networkingManager,//localSource,
         context,appSHP)
        }
    }

      suspend fun getCurrentWeatherWithLocationInRepo(lat:Double, long:Double, unit:String): WeatherForecast {
        Log.i(TAG, "getCurrentWeatherWithLocationInRepoooooooooooooo: ")

         /*
        if(getSettingsSharedPreferences()?.language as Boolean){

            var weatherinrepo = networkingManager.getWeatherByLocation(lat,long,unit,arrayOfLangs[0])
            Log.i(TAG, "getCurrentWeatherWithLocationInRepo: ${weatherinrepo.current.weather[0].description} ")
            return weatherinrepo
        }

          */

        var weatherinrepo2 = networkingManager.getWeatherByLocation(lat,long,unit,Constants.languages.en.toString())
        Log.i("TAG", "getCurrentWeatherWithLocationInRepo: ${weatherinrepo2.current.weather[0].description} ")
        return weatherinrepo2
    }


    /*
     val storedAddresses: LiveData<List<WeatherAddress>>
        get() = localSource.getAllAddresses()



     fun getAllWeathersInRepo(): LiveData<List<WeatherForecast>> {
        return localSource.getAllStoredWeathers()
    }

     fun getOneWeather(lat: Double, long: Double): LiveData<WeatherForecast> {
        return localSource.getWeatherWithLatLong(lat,long)
    }

     fun insertFavoriteAddress(address: WeatherAddress) {
        localSource.insertFavoriteAddress(address)
    }

     fun deleteFavoriteAddress(address: WeatherAddress) {
        localSource.deleteFavoriteAddress(address)
    }

     fun insertWeather(weather: WeatherForecast) {
        localSource.insertWeather(weather)
    }

     fun deleteWeather(weather: WeatherForecast) {
        localSource.deleteWeather(weather)
    }
    fun getAllAlertsInRepo(): LiveData<List<AlertData>> {
        return localSource.getAllStoredAlerts()
    }

    fun insertAlertInRepo(alert: AlertData) {
        localSource.insertAlert(alert)
    }

    fun deleteAlertInRepo(alert: AlertData) {
        localSource.deleteAlert(alert)
    }
    */

      fun addSettingsToSharedPreferences(setting: Setting) {
        var prefEditor = sharedPreferences.edit()
        var gson= Gson()
        var settingStr = gson.toJson(setting)
        prefEditor.putString(Constants.MY_SETTINGS_PREFS,settingStr)
        prefEditor.commit()
    }

      fun getSettingsSharedPreferences(): Setting? {
        var settingStr = sharedPreferences.getString(Constants.MY_SETTINGS_PREFS,"")
        var gson= Gson()
        var settingObj:Setting? = gson.fromJson(settingStr,Setting::class.java)
        return settingObj
    }

      fun addWeatherToSharedPreferences(weather: WeatherForecast) {
        var prefEditor = sharedPreferences.edit()
        var gson= Gson()
        var weatherStr = gson.toJson(weather)
        prefEditor.putString(Constants.MY_CURRENT_WEATHER_OBJ,weatherStr)
        prefEditor.commit()
    }

      fun getWeatherSharedPreferences(): WeatherForecast? {
        var weatherStr = sharedPreferences.getString(Constants.MY_CURRENT_WEATHER_OBJ,"")
        var gson= Gson()
        var weatherObj:WeatherForecast? = gson.fromJson(weatherStr,WeatherForecast::class.java)
        return weatherObj
      }


}