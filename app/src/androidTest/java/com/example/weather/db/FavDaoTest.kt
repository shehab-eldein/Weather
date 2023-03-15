package com.example.weather.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weather.model.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavDaoTest {

    private lateinit var db:WeatherDatabase
    private lateinit var dao: WeatherDAO

    private lateinit var weatherList: List<Weather>
    private lateinit var currentWeather: CurrentWeather
    private lateinit var hourlyList: List<HourlyWeather>
    private lateinit var dailyWeather: List<DailyWeather>
    private lateinit var weatherForecast: WeatherForecast


    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.weatherDAO()
        weatherList = listOf(Weather(22,"weather","des","img"))
        currentWeather =  CurrentWeather(1,2.2,4,7,12,5.5,weatherList )
        hourlyList = listOf(HourlyWeather(1,22.9,22.2,weatherList))
        dailyWeather = listOf( DailyWeather(22,Temprature(2.1,2.2,2.3,0.5,0.0,5.5),weatherList))
        weatherForecast = WeatherForecast(
            1.1,
            2.2,
            "timeTest",
            currentWeather,
            hourlyList,
            dailyWeather,
            null
        )

    }

    @After
    fun tearDown (){
        db.close()
    }

    @Test
    fun addFavTest (){
        dao.insert(weatherForecast)
       val FavList = dao.getAll()
        assertThat(FavList).contains(weatherForecast)
        // assertThat(FavList).isEmpty()
        // assertThat(FavList).hasSize(2)

    }
    @Test
    fun delteFavTest (){
        dao.insert(weatherForecast)
         dao.deleteByLatLong(weatherForecast.lat,weatherForecast.lon)
        val FavList = dao.getAll()
         assertThat(FavList).isEmpty()
        assertThat(FavList).hasSize(0)
        assertThat(FavList).doesNotContain(weatherForecast)
        //assertThat(FavList).contains(weatherForecast)
    }
    @Test
    fun getFavTest (){
        dao.insert(weatherForecast)

        val FavList = dao.getAll()
        assertThat(FavList).contains(weatherForecast)
        // assertThat(FavList).isEmpty()
        //assertThat(FavList).hasSize(0)
        //assertThat(FavList).doesNotContain(weatherForecast)
    }

}