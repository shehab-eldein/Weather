package com.example.weather.splash

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.example.weather.R
import com.example.weather.databinding.FragmentSplashBinding
import com.example.weather.db.DBManager
import com.example.weather.helper.Constants.MY_SHARED_PREFERENCES
import com.example.weather.helper.CurrentUser
import com.example.weather.model.Repo
import com.example.weather.model.Setting
import com.example.weather.networking.NetworkingManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var navController: NavController
    private var locationSp: LatLng? = null
    private var setting: Setting? = null
    private lateinit var nameText:TextView
    private lateinit var repo:Repo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        navController = Navigation.findNavController(requireActivity(), R.id.dashBoardContainer)

        /*
        Handler().postDelayed({
            //navController.navigate(R.id.action_splashFragment_to_introFragment)
        },2000)

         */
        //make ViewModel here instead of repo direct

        repo = Repo.getInstance(
            NetworkingManager.getInstance(), DBManager(requireContext())
            ,requireContext(),
            requireContext().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE))

         if (repo.getSettingsSharedPreferences() != null) {
             CurrentUser.settings = repo.getSettingsSharedPreferences()!!
        }
        locationSp = repo.get_LatLongSP()

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