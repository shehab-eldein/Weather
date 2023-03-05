package com.example.weather.home.view

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.safyweather.Constants
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.helper.Formmater
import com.example.weather.home.viewModel.MyFactory
import com.example.weather.home.viewModel.ViewModelHome

import com.example.weather.model.Repo
import com.example.weather.model.WeatherForecast
import com.example.weather.networking.NetworkingManager
import kotlinx.coroutines.*
import java.util.*
import androidx.navigation.Navigation.findNavController
import com.example.weather.helper.CurrentUser


private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {
    /*


    private var settings: Settings? = null
    var connectivity : ConnectivityManager? = null
    var info : NetworkInfo? = null
     */
    lateinit var animLoading: LottieAnimationView
    lateinit var binding: FragmentHomeBinding
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var layoutManagerHourly: LinearLayoutManager
    lateinit var layoutManagerDaily: LinearLayoutManager
    lateinit var viewModel: ViewModelHome
    lateinit var factory: MyFactory
   // val latLongArgs: HomeFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         factory = MyFactory( Repo(NetworkingManager.getInstance(),requireContext(),requireContext().getSharedPreferences(Constants.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)))
         viewModel = ViewModelProvider(this,factory).get(ViewModelHome::class.java)

        GlobalScope.launch (Dispatchers.Main){
           val weather = viewModel.getWeather(CurrentUser.lat,CurrentUser.long)
            Log.i(TAG, "onCreate:Enter ${weather.hourly[0].weather} ")
            updateUI(weather)
            initRecycler()
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        animLoading = view.findViewById(R.id.animationView)
        animLoading.visibility = View.GONE


    }



    fun updateUI(weather: WeatherForecast?){
        weather as WeatherForecast
       // binding.currCity.text =   weather.timezone
        binding.currCity.text = getAddressFromLatLng(CurrentUser.lat,CurrentUser.long)

        binding.currDate.text = Formmater.getDateFormat(weather.current.dt)
        binding.currTime.text = Formmater.getTimeFormat(weather.current.dt)
        binding.currTemp.text = weather.current.temp.toString()
        binding.currDesc.text = weather.current.weather[0].description
        binding.currHumidity.text = weather.current.humidity.toString()
        binding.currWindSpeed.text = weather.current.wind_speed.toString()
        binding.currClouds.text = weather.current.clouds.toString()
        binding.currPressure.text = weather.current.pressure.toString()
        //duplicated delete when remove the comment look for handel unit
        binding.currWindUnit.text = getString(R.string.windMile)
        //use picasso
        Glide.with(context as Context)
            .load("https://openweathermap.org/img/wn/"+weather.current.weather[0].icon+"@2x.png")
            .into(binding.currIcon)
       hourlyAdapter = HourlyAdapter(requireContext(),weather.hourly)
       // hourlyAdapter.setHourlyWeatherList(weather.hourly)
        dailyAdapter = DailyAdapter(requireContext(),weather.daily)
    }
    fun initRecycler(){

        layoutManagerHourly = LinearLayoutManager(context as Context,
            LinearLayoutManager.HORIZONTAL,false)
        layoutManagerDaily = LinearLayoutManager(context as Context)
        binding.hourlyRecycler.adapter = hourlyAdapter
        binding.dailyRecycler.adapter = dailyAdapter
        binding.hourlyRecycler.layoutManager = layoutManagerHourly
        binding.dailyRecycler.layoutManager = layoutManagerDaily
    }
    fun getAddressFromLatLng(lat:Double,longg:Double) : String{
       // Locale.setDefault(Locale("ar"))
        var geocoder = Geocoder(context as Context, Locale.getDefault())
        var addresses:List<Address>
        addresses = geocoder.getFromLocation(lat,longg,1) as List<Address>
        if(addresses.size>0) {
            return " ${addresses.get(0).subAdminArea},${addresses.get(0).adminArea} ,${addresses.get(0).countryName}  "
        }
        return ""
    }


}