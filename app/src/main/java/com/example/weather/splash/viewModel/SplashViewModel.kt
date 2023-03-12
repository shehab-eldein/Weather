package com.example.weather.splash.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weather.helper.CurrentUser
import com.example.weather.model.Repo
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(var repo: Repo):ViewModel(){
    fun getLocatinSP(): LatLng? {
        if (repo.getSettingsSharedPreferences() != null) {
            CurrentUser.settings = repo.getSettingsSharedPreferences()!!
        }
        Log.i("HomeFragment", "getLocatinSP: ${repo.get_LatLongSP()?.latitude}")
       return  repo.get_LatLongSP()
    }
}