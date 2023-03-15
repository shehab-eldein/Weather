package com.example.weather.model.repo

import androidx.lifecycle.LiveData
import com.example.weather.model.AlertData
import com.example.weather.model.Setting
import com.example.weather.model.WeatherForecast
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface RepoInterFace  {
    //*********************************** RetroFit ************************************************
    suspend fun getCurrentWeatherWithLocationInRepo(
        lat: Double,
        long: Double,
        unit: String
    ): WeatherForecast

    //**************************** Alerts *******************************************
    fun getAllAlertsInRepo(): LiveData<List<AlertData>>
    fun insertAlertInRepo(alert: AlertData)
    fun deleteAlertInRepo(alert: AlertData)
    //**************************** Fav *******************************************
    fun storedLocations(): Flow<WeatherForecast>
    fun offlineFav(): Flow<List<WeatherForecast>?>
    fun insertWeatherDB(weather: WeatherForecast)
    fun deleteFavoriteWeather(weather: WeatherForecast)

    //******************************* Home  Room ******************************************************
    fun searchWithLatLong(latLong: LatLng): Flow<WeatherForecast?>
    fun deletePreviousHome(loc: LatLng)

    //************************* SHARED PREFRENCE **********************************************
    fun addSettingsToSharedPreferences(setting: Setting)
    fun getSettingsSharedPreferences(): Setting?
    fun add_HomeLocToSP(latLong: LatLng)
    fun get_HomeLocSP(): LatLng?
    fun add_LatLongToSP(latLong: LatLng)
    fun get_LatLongSP(): LatLng?
}