package com.example.weather.model

data class Setting(var language:Boolean = true,
                   var unit:Int = 0,
                   var location:Int = 1,
                   var notification:Boolean = true)