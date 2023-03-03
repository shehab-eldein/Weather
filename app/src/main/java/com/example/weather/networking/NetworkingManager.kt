package com.example.weather.networking

import android.util.Log
import com.example.weather.model.WeatherForecast

private const val TAG = "NetworkingManager"

class NetworkingManager:NetworkingManagerInterFace {

    companion object{
        private var instance: NetworkingManager? = null
        fun getInstance(): NetworkingManager{
            return instance?: NetworkingManager()
        }
    }


    override suspend fun getWeatherByLocation(lat: Double, long: Double, unit: String, lang: String): WeatherForecast {


        val apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        Log.i(TAG, "called")
        //bec88e8dd2446515300a492c3862a10e
        //2e3a4ea22ac7c57d9e7a1502fe8bf2ea me
        //992213628dbceb7e7fb06cf59035697d
        val response = apiService.getWholeWeather(lat,long,unit,"minutely",lang,"2e3a4ea22ac7c57d9e7a1502fe8bf2ea")
        Log.i(TAG, "called2 :${response.errorBody()}")
        if(response.isSuccessful){
            var result = response.body() as WeatherForecast
            Log.i(TAG, "DONE ${result.current.weather[0].description} response")
        }
        else{
            Log.i(TAG, "FAIL response${response.errorBody().toString()}")
        }
        return response.body() as WeatherForecast
    }

}