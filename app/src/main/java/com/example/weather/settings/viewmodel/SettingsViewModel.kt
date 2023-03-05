package com.example.weather.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weather.model.Repo
import com.example.weather.model.Setting


class SettingsViewModel (private val repo: Repo):ViewModel(){
    fun setSettingsSharedPrefs(settings: Setting){
        repo.addSettingsToSharedPreferences(settings)
    }
    fun getStoredSettings(): Setting?{
        return repo.getSettingsSharedPreferences()
    }
}