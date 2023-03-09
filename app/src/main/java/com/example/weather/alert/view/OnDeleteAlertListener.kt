package com.example.weather.alert.view

import com.example.weather.model.AlertData

interface OnDeleteAlertListener {
    fun onRemoveBtnClick(alert: AlertData)

}