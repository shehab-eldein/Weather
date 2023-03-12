package com.example.weather.db

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.example.weather.model.WeatherForecast

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM weathers")
    fun getAll():List<WeatherForecast>

    @Insert(onConflict = REPLACE)
    fun insert(weather:WeatherForecast)

    @Delete
    fun delte(weather: WeatherForecast)

    @Query("SELECT * FROM weathers WHERE lat == :latt AND lon == :longg")
    fun searchWithLatLong(latt:Double, longg:Double): WeatherForecast

    @Query("DELETE FROM weathers WHERE lat == :latt AND lon == :longg")
    fun deleteByLatLong(latt:Double, longg:Double)
}
