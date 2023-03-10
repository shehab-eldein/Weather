package com.example.weather.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.Repo
import com.example.weather.model.Setting
import com.example.weather.model.WeatherForecast
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelHome @Inject constructor (var _repo: Repo): ViewModel() {

    private  val TAG = "ViewModelHome"

    suspend fun getWeather(lat: Double,long:Double): WeatherForecast{
        var weather:WeatherForecast? = null
        val job =viewModelScope.launch(Dispatchers.IO) {
           weather = _repo.getCurrentWeatherWithLocationInRepo(lat,long,"metric")
        }
        job.join()

        return weather as WeatherForecast

    }
    // TODO: get home when offline by search in data base


    fun getLocationSP() :LatLng? {
       var location = _repo.get_LatLongSP()
        return  location
    }
    fun getStoredSettings(): Setting?{
        return _repo.getSettingsSharedPreferences()
    }

}

