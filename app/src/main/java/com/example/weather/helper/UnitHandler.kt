package com.example.weather.helper


import com.example.weather.R
import com.example.weather.model.Setting

object UnitHandler {



     fun getUnitName (settings:Setting) : Pair<Int,Int>{

          var str = Pair(R.string.F,R.string.mh)
          when(settings.unit) {
               0 -> str = Pair(R.string.K,R.string.ms)
               1->  str =  Pair(R.string.C,R.string.ms)

          }
          return str
     }


}