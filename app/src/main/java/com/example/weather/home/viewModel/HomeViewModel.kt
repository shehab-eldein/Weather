package com.example.weather.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.db.DBState
import com.example.weather.model.Repo
import com.example.weather.model.Setting
import com.example.weather.model.WeatherForecast
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelHome @Inject constructor (var _repo: Repo): ViewModel() {

    private  val TAG = "ViewModelHome"
    var dbState = MutableStateFlow<DBState>(DBState.Loading)
    suspend fun getWeather(lat: Double,long:Double): WeatherForecast{
        var weather:WeatherForecast? = null
        val job =viewModelScope.launch(Dispatchers.IO) {
           weather = _repo.getCurrentWeatherWithLocationInRepo(lat,long,"metric")
        }
        job.join()

        return weather as WeatherForecast

    }
    // TODO: get home when offline by search in data base

    fun getHome(latLng: LatLng):MutableStateFlow<DBState> {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.searchWithLatLong(latLng)
                .catch { dbState.value = DBState.onFail(it) }
                .collect{ dbState.value = DBState.onSuccessWeather(it as WeatherForecast) }
        }
        return dbState
    }
    fun addHome(weather:WeatherForecast) {
        _repo.insertWeatherDB(weather)
    }
    fun addHomeLocSp(loc:LatLng){
        _repo.add_HomeLocToSP(loc)
    }
    fun getHomeLocSP(): LatLng? {
       return _repo.get_HomeLocSP()
    }

    fun getStoredSettings(): Setting?{
        return _repo.getSettingsSharedPreferences()
    }
    fun deletePrevHome(loc:LatLng) {
        _repo.deletePreviousHome(loc)
    }

}

