package com.example.weather.model

import androidx.room.Entity

@Entity(tableName = "AlertsTable", primaryKeys = ["idHashLongFromLonLatStartStringEndStringAlertType"])
data class AlertData(
    val address: String,
    val longitudeString: String,
    val latitudeString: String,
    val startString: String,
    val endString: String,
    val startDT: Int,
    val endDT: Int,
    val idHashLongFromLonLatStartStringEndStringAlertType: Long,
    val alertType: String
)