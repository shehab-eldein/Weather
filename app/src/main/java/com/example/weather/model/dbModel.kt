package com.example.weather.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(primaryKeys = arrayOf("lat", "lon"), tableName = "addresses")
data class Location(var locatinName:String,
                    @NonNull
                          var lat:Double,
                    @NonNull
                          var lon:Double)

/*
@Entity(tableName = "alerts")
data class AlertData(var fromDate: Date,
                     var toDate: Date,
                     var notifyType:Boolean = true){
    @PrimaryKey(autoGenerate = true)
    var pKey:Int = 0
}

 */