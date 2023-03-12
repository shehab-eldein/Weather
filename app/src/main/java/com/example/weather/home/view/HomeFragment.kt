package com.example.weather.home.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView

import com.example.weather.R
import com.example.weather.adapters.DailyAdapter
import com.example.weather.adapters.HourlyAdapter
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.helper.*
import com.example.weather.home.viewModel.ViewModelHome

import com.example.weather.model.WeatherForecast
import kotlinx.coroutines.*
import com.github.matteobattilana.weather.PrecipType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private  val TAG = "HomeFragment"
    lateinit var animLoading: LottieAnimationView
    lateinit var binding: FragmentHomeBinding
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var layoutManagerHourly: LinearLayoutManager
    lateinit var layoutManagerDaily: LinearLayoutManager
    lateinit var viewModel: ViewModelHome


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewModelHome::class.java)
        CurrentUser.settings = viewModel.getStoredSettings()!!






    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        animLoading = view.findViewById(R.id.animationLogo)
        animLoading
        animateBG(PrecipType.CLEAR)
        if (CurrentUser.isConnectedToNetwork) {
            getData()
        }else {
            animLoading.visibility = View.GONE
            //getData from room
        }

    }

    fun getData() {
        lifecycleScope.launch (Dispatchers.Main){
            val weather = viewModel.getWeather(CurrentUser.location.latitude,CurrentUser.location.longitude)
            updateUI(weather)
            initRecycler()

        }
    }
    fun animateBG(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            speed = 300
            emissionRate = 200f // snow count
            angle = 0 // The angle of the fall
            fadeOutPercent = 0.9f // When to fade out (0.0f-1.0f)
        }
    }
    fun updateUI(weather: WeatherForecast?){
        weather as WeatherForecast
        binding.currCity.text = LocalityManager.getAddressFromLatLng(requireActivity(),
                 CurrentUser.location.latitude
               ,CurrentUser.location.longitude
        )
        binding.currDate.text = Formmater.getDateFormat(weather.current.dt)
        binding.currTime.text = Formmater.getTimeFormat(weather.current.dt)
        binding.currTemp.text = weather.current.temp.toString()
        binding.currDesc.text = weather.current.weather[0].description
        binding.currHumidity.text = weather.current.humidity.toString()
        binding.currWindSpeed.text = weather.current.wind_speed.toString()
        binding.currClouds.text = weather.current.clouds.toString()
        binding.currPressure.text = weather.current.pressure.toString()
        binding.currUnit .text = UnitHandler.getUnitName(CurrentUser.settings).first
        binding.currWindUnit.text = UnitHandler.getUnitName(CurrentUser.settings).second
        val mainWeather =   weather.current.weather[0].main
        when(mainWeather.lowercase()) {
            "thunderstorm"->  binding.currIcon.setImageResource(R.drawable.lightning)
            "drizzle"    -> {
                binding.currIcon.setImageResource(R.drawable.drizzel)
                animateBG(PrecipType.RAIN)
            }
            "rain","squall" -> {
                 binding.currIcon.setImageResource(R.drawable.rain)
                 animateBG(PrecipType.RAIN)
            }
            "snow"    -> {
                binding.currIcon.setImageResource(R.drawable.snow)
                animateBG(PrecipType.SNOW)
            }
            "clouds"    ->{
                animateBG(PrecipType.RAIN)
                binding.currIcon.setImageResource(R.drawable.cloudy)
            }
            "haze" ,"mist","fog"  -> binding.currIcon.setImageResource(R.drawable.fog_haze)
            "smoke"  -> binding.currIcon.setImageResource(R.drawable.smoke)
            "dust","sand","tornado" -> binding.currIcon.setImageResource(R.drawable.sand)
            else ->  binding.currIcon.setImageResource(R.drawable.clear)
        }
        hourlyAdapter = HourlyAdapter(requireContext(),weather.hourly)
        dailyAdapter = DailyAdapter(requireContext(),weather.daily)
        animLoading.visibility = View.GONE


    }
    fun initRecycler(){
        layoutManagerHourly = LinearLayoutManager(context as Context,
            LinearLayoutManager.HORIZONTAL,false)
        layoutManagerDaily= LinearLayoutManager(context as Context,
            LinearLayoutManager.VERTICAL,false)
        binding.hourlyRecycler.adapter = hourlyAdapter
        binding.dailyRecycler.adapter = dailyAdapter
        binding.hourlyRecycler.layoutManager = layoutManagerHourly
        binding.dailyRecycler.layoutManager = layoutManagerDaily
    }




}