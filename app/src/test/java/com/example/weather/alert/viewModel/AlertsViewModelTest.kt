package com.example.weather.alert.viewModel

import com.example.weather.getOrAwaitValueTest
import com.example.weather.model.AlertData
import com.example.weather.model.repo.Repo
import com.example.weather.repo.FakeRepo
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test


class AlertsViewModelTest{
    private lateinit var viewModel: AlertsViewModel
    private lateinit var  alert:AlertData

    @Before
    fun setup() {
       // viewModel = AlertsViewModel(FakeRepo)
         alert = AlertData(
            "address1"
            ,"22",
            "23","12:14",
            "",22,22,10,"type")
    }


    @Test
    fun `insert alert with empty field ,return error`() {
        viewModel.addAlertInVM(alert)
        val value = viewModel.getAllAlertsInVM().getOrAwaitValueTest()
        assertThat(value).isEmpty()
    }
}