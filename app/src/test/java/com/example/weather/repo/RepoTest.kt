package com.example.weather.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weather.model.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RepoTest {
  private lateinit var weatherList: List<Weather>
  private lateinit var currentWeather: CurrentWeather
  private lateinit var hourlyList: List<HourlyWeather>
  private lateinit var dailyWeather: List<DailyWeather>
  private lateinit var repo:FakeRepo
  private lateinit var weatherForecast: WeatherForecast
    @get:Rule
    var instantTask = InstantTaskExecutorRule()

    @Before
    fun setup() {
      weatherList = listOf(Weather(22,"weather","des","img"))
      currentWeather =  CurrentWeather(1,2.2,4,7,12,5.5,weatherList )
      repo = FakeRepo()
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
  @Test
  fun AddFavTest()= runBlockingTest {
    repo.insertWeatherDB(weatherForecast)
    var result = mutableListOf<WeatherForecast>()
      repo.storedLocations().collect{
      result.add(it)
     }
    assertThat(result).contains(weatherForecast)
   //assertThat(result).isEmpty()
    assertThat(result).hasSize(2)

  }
  @Test
  fun getFavtest()= runBlockingTest {
    var result = mutableListOf<WeatherForecast>()
    repo.storedLocations().collect{
      result.add(it)
    }
    assertThat(result).hasSize(1)
    assertThat(result).doesNotContain(weatherForecast)
  }

  @Test
  fun deleteFavTest() = runBlockingTest{
    repo.insertWeatherDB(weatherForecast)
    repo.deleteFavoriteWeather(weatherForecast)
    var result = mutableListOf<WeatherForecast>()
    repo.storedLocations().collect{
      result.add(it)
    }
    assertThat(result).doesNotContain(weatherForecast)
    assertThat(result).contains(weatherForecast)

  }
  @Test fun addAlertstest() {

  }



}