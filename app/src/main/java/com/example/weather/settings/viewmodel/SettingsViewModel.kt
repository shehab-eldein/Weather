package com.example.weather.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weather.model.repo.Repo
import com.example.weather.model.Setting
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (private val repo: Repo):ViewModel(){
    fun setSettingsSharedPrefs(settings: Setting){
        repo.addSettingsToSharedPreferences(settings)
    }
    fun getStoredSettings(): Setting?{
        return repo.getSettingsSharedPreferences()

    }

    fun addLocSP(latLng: LatLng){
        repo.add_LatLongToSP(latLng)
    }

}