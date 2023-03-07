package com.example.weather.db

import android.content.Context
import androidx.room.*
import com.example.weather.helper.WeatherConverter
import com.example.weather.model.AlertData
import com.example.weather.model.Location
import com.example.weather.model.WeatherForecast


@Database(entities = arrayOf(WeatherForecast::class), version = 3)
@TypeConverters(WeatherConverter::class)
abstract class WeatherDatabase :RoomDatabase(){


    abstract fun addressesDao(): WeatherDAO


    companion object{
        private var weatherDatabase:WeatherDatabase? = null

        fun getInstance(context: Context):WeatherDatabase{
            return weatherDatabase ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java, "WeatherDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
                weatherDatabase = instance
                instance
            }
        }
    }

}