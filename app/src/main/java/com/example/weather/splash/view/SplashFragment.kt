package com.example.weather.splash.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.databinding.FragmentSplashBinding
import com.example.weather.helper.CurrentUser
import com.example.weather.helper.LocalityManager
import com.example.weather.home.viewModel.ViewModelHome
import com.example.weather.model.Setting
import com.example.weather.splash.viewModel.SplashViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var navController: NavController
    private var locationSp: LatLng? = null
    lateinit var viewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        navController = Navigation.findNavController(requireActivity(), R.id.dashBoardContainer)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        locationSp = viewModel.getLocatinSP()
        viewModel



        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)
        binding.logoText.typeWrite(viewLifecycleOwner, "Clima App", 200L)



    }

    fun TextView.typeWrite(lifecycleOwner: LifecycleOwner, text: String, intervalMs: Long) {

        this@typeWrite.text = ""

        lifecycleOwner.lifecycleScope.launch {
            delay(1500)
            repeat(text.length) {
                delay(intervalMs)
                this@typeWrite.text = text.take(it + 1)
            }
            delay(1150)

           NavigateTo()
        }
    }

    fun NavigateTo() {
        if(locationSp == null){
            navController.navigate(R.id.action_splashFragment_to_introFragment)
        }
        else{
            CurrentUser.location = locationSp as LatLng
            val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment2()
            navController.navigate(action)
        }
    }


}