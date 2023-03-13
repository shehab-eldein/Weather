package com.example.weather.alert.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.AlertData
import com.example.weather.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor ( val repo: Repo): ViewModel() {

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



}