package com.example.weather.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.weather.model.AlertData

@Dao
interface AlertsDAO {
    @Query("SELECT * FROM AlertsTable")
    // TODO: stateFlow
    fun getAllStoredAlerts(): LiveData<List<AlertData>>

    @Insert(onConflict = REPLACE)
    fun insertAlert(alert: AlertData)

    @Delete
    fun deleteAlert(alert: AlertData)
}