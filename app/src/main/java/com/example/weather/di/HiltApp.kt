package com.example.weather.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.weather.db.AlertsDAO
import com.example.weather.db.DBManager
import com.example.weather.db.WeatherDAO
import com.example.weather.db.WeatherDatabase
import com.example.weather.helper.Constants.MY_SHARED_PREFERENCES
import com.example.weather.helper.NetworkStatusTracker
import com.example.weather.networking.NetworkingManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltApp {

    @Singleton
    @Provides
    fun getAppDB(context: Application): WeatherDatabase {
        return WeatherDatabase.getInstance(context)
    }
    @Singleton
    @Provides
    fun getAppNetworking(context: Application): NetworkingManager {
        return NetworkingManager.getInstance()
    }
    @Singleton
    @Provides
    fun getAppDBManager(context: Application): DBManager {
        return DBManager.getInstance(context)
    }
    @Singleton
    @Provides
    fun getAppContext(context: Application): Context {
        return context
    }
    @Singleton
    @Provides
    fun getAppSP(context: Application): SharedPreferences {
        return  context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }


    @Singleton
    @Provides
    fun getDao(weatherDatabase: WeatherDatabase):WeatherDAO {
        return weatherDatabase.addressesDao()
    }
    @Singleton
    @Provides
    fun getAlertDao(weatherDatabase: WeatherDatabase):AlertsDAO {
        return weatherDatabase.alertsDao()
    }
    @Singleton
    @Provides
    fun networkChecker(context: Application):NetworkStatusTracker {
        return NetworkStatusTracker(context)
    }
}