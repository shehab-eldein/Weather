package com.example.safyweather


object Constants {


    const val MY_SHARED_PREFERENCES = "WeatherSP"
    const val MY_CURRENT_LOCATION = "currentLocationSP"
    const val MY_SETTINGS_PREFS = "SettingsSP"

    enum class units(val unitsValue: Int) {
        standard (0),
        metric (1),
        imperial(2)
    }

    enum class languages(val langValue: String) {
        en ("en"),
        ar("ar")
    }

    const val weatherKey = ""
}