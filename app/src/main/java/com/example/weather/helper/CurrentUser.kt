package com.example.weather.helper

import com.example.weather.model.Setting
import com.google.android.gms.maps.model.LatLng

object  CurrentUser {
    var location = LatLng(30.111471,31.369408)
    var settings = Setting()
    var alertLocation = location
    var isConnectedToNetwork = true
}
