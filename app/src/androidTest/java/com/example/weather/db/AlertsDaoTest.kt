package com.example.weather.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weather.getOrAwaitValueTest
import com.example.weather.model.AlertData
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertsDaoTest {
    private lateinit var db:WeatherDatabase
    private lateinit var dao: AlertsDAO
    private lateinit var alertData:AlertData

    @get:Rule
    var instantTask = InstantTaskExecutorRule()

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.alertsDao()
        alertData = AlertData(
            "address1"
            ,"22",
            "23","12:14",
            "",22,22,10,"type")
    }
    @After
    fun tearDown (){
        db.close()
    }
    @Test
    fun addAlertTest () = runBlockingTest{
        dao.insertAlert (alertData)
        val alertList = dao.getAllStoredAlerts().getOrAwaitValueTest()
        assertThat(alertList).contains(alertData)
        // assertThat(alertList).isEmpty()
        // assertThat(alertList).hasSize(2)

    }

    @Test
    fun delteAlertTest (){
        dao.insertAlert(alertData)
        dao.deleteAlert(alertData)
        val aletList = dao.getAllStoredAlerts().getOrAwaitValueTest()
        assertThat(aletList).isEmpty()
        assertThat(aletList).hasSize(0)
        assertThat(aletList).doesNotContain(alertData)
       // assertThat(aletList).contains(alertData)
    }
    @Test
    fun getAlertsTest ()= runBlockingTest{
        dao.deleteAlert(alertData)
        val aletList = dao.getAllStoredAlerts().getOrAwaitValueTest()
        assertThat(aletList).doesNotContain(alertData)
        // assertThat(FavList).isEmpty()
        //assertThat(FavList).hasSize(0)
        //assertThat(FavList).doesNotContain(weatherForecast)
    }
}