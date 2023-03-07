package com.example.weather.alert.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.AlertData
import com.example.weather.model.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel(private val repo: Repo): ViewModel() {

    //alerts
    fun getAllAlertsInVM():LiveData<List<AlertData>>{
        return repo.getAllAlertsInRepo()
    }

    fun addAlertInVM(alert:AlertData){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertAlertInRepo(alert)
        }
    }

    fun removeAlertInVM(alert:AlertData){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteAlertInRepo(alert)
        }
    }

    /*
    //settings
    fun setSettingsSharedPrefs(settings: Setting){
        repo.addSettingsToSharedPreferences(settings)
    }

    fun getStoredSettings(): Settings? {
        return repo.getSettingsSharedPreferences()
    }

     */

}