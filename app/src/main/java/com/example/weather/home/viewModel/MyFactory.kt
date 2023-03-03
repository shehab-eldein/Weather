package com.example.weather.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


import com.example.weather.model.Repo

@Suppress("UNCHEKED CAST")
class MyFactory(private var repo: Repo): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>) : T{

            return if (modelClass.isAssignableFrom(ViewModelHome::class.java))
            {
                ViewModelHome(repo) as T
            }
            else{
                throw java.lang.IllegalArgumentException("View modle class not found")
            }


        }


}