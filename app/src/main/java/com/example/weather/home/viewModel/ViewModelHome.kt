package com.example.weather.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.Repo
import com.example.weather.model.WeatherForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ViewModelHome"
class ViewModelHome(var _repo: Repo): ViewModel() {

    suspend fun getWeather(lat: Double,long:Double): WeatherForecast{
        var weather:WeatherForecast? = null
        val job =viewModelScope.launch(Dispatchers.IO) {
           weather = _repo.getCurrentWeatherWithLocationInRepo(lat,long,"metric")

        }
        job.join()
        // while (proudectList.isEmpty()){}

        Log.i(TAG, "getWeather: ${weather!!.hourly[0].weather}")

        return weather as WeatherForecast

    }
}
/*
 var repo: Repo = Repo(
            NetworkingManager.getInstance(),requireContext(), requireContext().getSharedPreferences (
                MY_SHARED_PREFERENCES, Context.MODE_PRIVATE))

        var result:WeatherForecast?
        CoroutineScope(Dispatchers.IO).launch {
             result = repo.getCurrentWeatherWithLocationInRepo(44.34,10.99, "metric")
            //Log.i("TAG", "onViewCreated: ${result.hourly.size}")

            while (result == null){}
            withContext(Dispatchers.Main){

                //Log.i("TAG", "home fragment: ${result.hourly[0].weather}")
                updateUI(result)
            }

        }
 */