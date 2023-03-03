package com.example.weather.networking


import com.example.weather.model.WeatherForecast
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("onecall")
    suspend fun getWholeWeather(@Query("lat") lat:Double,
                                   @Query("lon") long:Double,
                                   @Query("units") unit:String,
                                   @Query("exclude") exclude:String,
                                   @Query("lang") lang:String,
                                   @Query("appid")appid:String): Response<WeatherForecast>
}
object RetrofitHelper{
    const val baseURL = "https://api.openweathermap.org/data/2.5/"
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
