package com.example.weather.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Build
import java.util.*

class LocalityManager {
    companion object {
        fun setLocale(context: Context, lang:String) {
            update(context,lang)
        }

        fun update(context: Context, language: String) {
            updateResources(context, language)
            var appContext: Context = context.applicationContext
            if (context != appContext) {
                updateResources(appContext, language)
            }
        }

        @SuppressLint("ObsoleteSdkInt")
        fun updateResources(context: Context, language: String) {
            var locale = Locale(language)
            Locale.setDefault(locale)
            var resources: Resources = context.resources
            var config = Configuration(resources.configuration)
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLocale(locale)
            } else {
                config.locale = locale
            }
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        fun getAddressFromLatLng(context: Context,lat:Double,longg:Double) : String{
             //Locale.setDefault(Locale("ar"))
            var geocoder = Geocoder(context , Locale.getDefault())
            var addresses:List<Address>
            addresses = geocoder.getFromLocation(lat,longg,1) as List<Address>
            if(addresses.size>0) {
                if (addresses.get(0).subAdminArea == null) {
                    return "${addresses.get(0).adminArea}, ${addresses.get(0).countryName}  "
                }
                return " ${addresses.get(0).subAdminArea}, ${addresses.get(0).adminArea}, ${addresses.get(0).countryName}  "
            }
            return ""
        }

        fun convertToArabicNumber(englishNumberInput: String): String {
            val arabicNumbers = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
            val englishNumbers = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            val builder = StringBuilder()
            for (i in englishNumberInput) {
                if (englishNumbers.contains(i)) {
                    builder.append(arabicNumbers[englishNumbers.indexOf(i)])
                } else {
                    builder.append(i) // point
                }
            }
            return builder.toString()
        }

    }

}

