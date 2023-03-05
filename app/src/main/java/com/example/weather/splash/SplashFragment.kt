package com.example.weather.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.databinding.FragmentSplashBinding
import com.example.weather.model.Setting
import com.example.weather.model.WeatherForecast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var navController: NavController
    private var currentWeather: WeatherForecast? = null
    private var setting: Setting? = null
    private lateinit var nameText:TextView
    //private lateinit var repo:RepositoryInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        navController = Navigation.findNavController(requireActivity(), R.id.dashBoardContainer)
        Handler().postDelayed({
            //navController.navigate(R.id.action_splashFragment_to_introFragment)
        },2000)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameText = view.findViewById(R.id.logoText)
           nameText.typeWrite(viewLifecycleOwner, "Clima App", 200L)

    }
    fun TextView.typeWrite(lifecycleOwner: LifecycleOwner, text: String, intervalMs: Long) {
        this@typeWrite.text = ""
        lifecycleOwner.lifecycleScope.launch {
            repeat(text.length) {
                delay(intervalMs)
                this@typeWrite.text = text.take(it + 1)
            }
            delay(1000)
            navController.navigate(R.id.action_splashFragment_to_introFragment)
        }
    }


}