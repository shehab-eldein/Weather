package com.example.weather.helper

import com.example.weather.model.Setting

object UnitHandler {

     fun getUnitName (settings:Setting) : Pair<String,String>{

          var str = Pair("°F","m/h")
          when(settings.unit) {
               0 -> str = Pair("°K","m/s")
               1->  str =  Pair("°C","m/s")

          }
          return str
     }
}