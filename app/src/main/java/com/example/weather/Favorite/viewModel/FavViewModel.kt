package com.example.weather.Favorite.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.weather.db.DBState
import com.example.weather.model.Repo
import com.example.weather.model.WeatherForecast
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavViewModel(var repo:Repo):ViewModel() {

    var dbState = MutableStateFlow<DBState>(DBState.Loading)


    fun addtoFav(weather:WeatherForecast) {

      repo.insertFavoriteWeather(weather)
  }
  fun delete(weather:WeatherForecast) {
    repo.deleteFavoriteWeather(weather)
  }

   fun getAllFav(): MutableStateFlow<DBState> {
       var weatherList = mutableListOf<WeatherForecast>()
       viewModelScope.launch(Dispatchers.IO) {
           repo.storedLocations().catch {
               dbState.value = DBState.onFail(it)
           }.collect{
               weatherList.add(it)
               dbState.value = DBState.onSuccessList(weatherList)
           }
       }
        return dbState
  }
    fun searchResult(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.searchWithLatLong(latLng).catch { dbState.value = DBState.onFail(it) }
                .collect{
                    dbState.value = DBState.onSuccessWeather(it as WeatherForecast)
                }
        }
    }
    suspend fun getWeather(lat: Double,long:Double): WeatherForecast{
        var weather:WeatherForecast? = null
        val job =viewModelScope.launch(Dispatchers.IO) {
            weather = repo.getCurrentWeatherWithLocationInRepo(lat,long,"metric")

        }
        job.join()
        // while (proudectList.isEmpty()){}
        return weather as WeatherForecast

    }

}