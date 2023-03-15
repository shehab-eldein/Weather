package com.example.weather.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.db.DBState
import com.example.weather.model.*
import com.example.weather.model.repo.RepoInterFace
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeRepo: RepoInterFace {

    val weatherList = listOf(Weather(22,"weather","des","img"))
    val currentWeather =  CurrentWeather(1,2.2,4,7,12,5.5,weatherList )
    val hourlyList = listOf(HourlyWeather(1,22.9,22.2,weatherList))
    val dailyWeather = listOf( DailyWeather(22,Temprature(2.1,2.2,2.3,0.5,0.0,5.5),weatherList))
    val  weatherForecast1 = WeatherForecast(
    1.1,
    1.1,
    "timeTest1",
    currentWeather,
    hourlyList,
    dailyWeather,
    null
    )

    //Local Data Source
    var weatherLocalDataSource = mutableListOf(weatherForecast1)
    var alertLocatDataSource = mutableListOf(AlertData(
        "address1"
        ,"22",
        "23","12:14",
        "44",22,22,10,"type"))
    //Observe on Local Data Source
    var observeAlerts = MutableLiveData<List<AlertData>>(alertLocatDataSource)

    //*************************** ALERTS *****************************************

    override fun getAllAlertsInRepo(): LiveData<List<AlertData>> {
       return observeAlerts
    }

    override fun insertAlertInRepo(alert: AlertData) {
       alertLocatDataSource.add(alert)
        observeAlerts.postValue(alertLocatDataSource)
    }

    override fun deleteAlertInRepo(alert: AlertData) {
        alertLocatDataSource.remove(alert)
    }

    //*************************** Fav *****************************************
    override fun storedLocations(): Flow<WeatherForecast>  = flow{
       for (weather in weatherLocalDataSource){
           emit(weather)
       }

    }

    override fun offlineFav(): Flow<List<WeatherForecast>?> = flow{

        emit(weatherLocalDataSource)
    }

    override fun insertWeatherDB(weather: WeatherForecast) {
       weatherLocalDataSource.add(weather)
    }

    override fun deleteFavoriteWeather(weather: WeatherForecast) {
       weatherLocalDataSource.remove(weather)
    }

    //*************************** Home *****************************************
    override fun searchWithLatLong(latLong: LatLng): Flow<WeatherForecast?> = flow{
        var resutl:WeatherForecast? = null
      for (weather in weatherLocalDataSource) {
          if (weather.lat == latLong.latitude && weather.lon == latLong.longitude) {
              resutl = weather

          }
      }
        emit(resutl)
    }

    override fun deletePreviousHome(loc: LatLng) {
        for (weather in weatherLocalDataSource) {

            if (weather.lat == loc.latitude && weather.lon == loc.longitude) {
                 weatherLocalDataSource.remove( weather)
            }
        }
    }

    //*************************** SP *****************************************
    override fun addSettingsToSharedPreferences(setting: Setting) {
        TODO("Not yet implemented")
    }

    override fun getSettingsSharedPreferences(): Setting? {
        TODO("Not yet implemented")
    }

    override fun add_HomeLocToSP(latLong: LatLng) {
        TODO("Not yet implemented")
    }

    override fun get_HomeLocSP(): LatLng? {
        TODO("Not yet implemented")
    }

    override fun add_LatLongToSP(latLong: LatLng) {
        TODO("Not yet implemented")
    }

    override fun get_LatLongSP(): LatLng? {
        TODO("Not yet implemented")
    }
    //*************************** Retrofit *****************************************
    override suspend fun getCurrentWeatherWithLocationInRepo(
        lat: Double,
        long: Double,
        unit: String
    ): WeatherForecast {
        TODO("Not yet implemented")
    }

}