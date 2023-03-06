package com.example.weather.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import org.intellij.lang.annotations.Language
import java.io.Serializable
import java.sql.Timestamp
import java.util.*

@Entity(primaryKeys = arrayOf("lat", "lon"), tableName = "weathers")
data class WeatherForecast(@NonNull
                           var lat:Double,
                           @NonNull
                           var lon:Double,
                           var timezone:String,
                           var current:CurrentWeather,
                           var hourly:List<HourlyWeather>,
                           var daily:List<DailyWeather>,
                           val alerts: List<Alert>?): Serializable

data class CurrentWeather(var dt:Int,
                          var temp:Double,
                          var pressure:Int,
                          var humidity:Int,
                          var clouds:Int,
                          var wind_speed:Double,
                          var weather:List<Weather>)

data class HourlyWeather(var dt:Int,
                         var temp:Double,
                         var wind_speed:Double,
                         var weather:List<Weather>)

data class DailyWeather(var dt:Int,
                        var temp:Temprature,
                        var weather:List<Weather>)

data class Weather(var id:Int,
                   var main:String,
                   var description:String,
                   var icon:String)

data class Temprature(var day:Double,
                      var min:Double,
                      var max:Double,
                      var night:Double,
                      var eve:Double,
                      var morn:Double)

data class Alert (var senderName: String,
                  var event: String,
                  var start: Long,
                  var end: Long,
                  var description: String,
                  var tags: List<String>)




