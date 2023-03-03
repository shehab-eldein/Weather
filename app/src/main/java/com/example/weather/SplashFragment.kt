package com.example.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.weather.databinding.FragmentSplashBinding
import com.example.weather.model.Settings
import com.example.weather.model.WeatherForecast


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var navController: NavController
    private var currentWeather: WeatherForecast? = null
    private var settings: Settings? = null
    //private lateinit var repo:RepositoryInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /*
        var repo:Repo = Repo(NetworkingManager.getInstance(),requireContext(), requireContext().getSharedPreferences (
            MY_SHARED_PREFERENCES, Context.MODE_PRIVATE))

        CoroutineScope(Dispatchers.IO).launch {
            var result = repo.getCurrentWeatherWithLocationInRepo(44.34,10.99, "metric")
           // Log.i("TAG", "onViewCreated: ${result.hourly[0].weather}")
            withContext(Dispatchers.Main){
                Log.i("TAG", "onViewCreated: ${result.hourly[0].weather}")
            }
        }

         */

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}