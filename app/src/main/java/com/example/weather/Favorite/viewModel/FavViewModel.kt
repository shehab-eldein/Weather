package com.example.weather.Favorite.viewModel

import androidx.lifecycle.*
import com.example.weather.db.DBState
import com.example.weather.model.Repo
import com.example.weather.model.WeatherForecast
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(var repo:Repo):ViewModel() {

    var dbState = MutableStateFlow<DBState>(DBState.Loading)


    fun addtoFav(weather:WeatherForecast) {
        repo.insertWeatherDB(weather)
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