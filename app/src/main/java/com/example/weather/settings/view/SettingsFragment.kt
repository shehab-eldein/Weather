package com.example.weather.settings.view

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.safyweather.Constants
import com.example.safyweather.Constants.MY_SHARED_PREFERENCES
import com.example.weather.Favorite.view.FavoriteFragmentDirections
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.db.DBManager
import com.example.weather.helper.LocalityManager
import com.example.weather.home.view.HomeFragment
import com.example.weather.map.MapFragment
import com.example.weather.model.Repo
import com.example.weather.model.Setting
import com.example.weather.networking.NetworkingManager
import com.example.weather.settings.viewmodel.SettingsViewModel
import com.example.weather.settings.viewmodel.SettingsViewModelFactory


class SettingsFragment : Fragment() {

    lateinit var settingsViewModel: SettingsViewModel
    lateinit var settingsViewModelFactory: SettingsViewModelFactory
    lateinit var binding: FragmentSettingsBinding
    private var settings: Setting? = null
    lateinit var navController: NavController
    // TODO: Duplicated internet Connection Settings
    var connectivity : ConnectivityManager? = null
    var info : NetworkInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        navController = Navigation.findNavController(requireActivity(),R.id.dashBoardContainer)
        settingsViewModelFactory = SettingsViewModelFactory(
            Repo(
                NetworkingManager.getInstance(), DBManager(requireContext()),requireContext()
                ,requireContext().getSharedPreferences(Constants.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            )
        )


        settingsViewModel = ViewModelProvider(this,settingsViewModelFactory).get(SettingsViewModel::class.java)
        settings = settingsViewModel.getStoredSettings()
        initDesign()
        initLogic()

    }

    private fun initLogic() {
        notificationActive()
        notificationDeActivate()
        gpsActive()
        mapActive()
        standardUnit()
        metricUnit()
        imperialUnit()
        arLang()
        enLang()
    }
    private fun enLang() {
        binding.englishLang.setOnClickListener{
            settings?.language = true
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)
            LocalityManager.setLocale(requireContext(),"en")
        }
    }
    private fun arLang() {
        binding.arabicLang.setOnClickListener{
            settings?.language = false
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)
            LocalityManager.setLocale(requireContext(),"ar")
        }
    }
    private fun imperialUnit() {
        binding.imperialUnit.setOnClickListener{
            settings?.unit = 2
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)
        }
    }
    private fun metricUnit() {
        binding.metricUnit.setOnClickListener{
            settings?.unit = 1
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)
        }
    }
    private fun standardUnit() {
        binding.standardUnit.setOnClickListener{
            settings?.unit = 0
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)
        }
    }
    private fun mapActive() {
        binding.map.setOnClickListener{
            settings?.location = 2
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)
            connectivity = context?.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
            if ( connectivity != null) {
                info = connectivity!!.activeNetworkInfo
                Log.i("TAG", "connectivity != null")
                if (info != null) {
                    if (info!!.state == NetworkInfo.State.CONNECTED) {
                        //threre is an boolean argument here

                        val action = SettingsFragmentDirections.actionSettingsFragment2ToMapFragment()
                        navController.navigate(action)

                       // Navigation.findNavController(requireActivity(), R.id.dashBoardContainer).navigate(R.id.mapFragment)
                    }
                    else{
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setMessage(getString(R.string.networkWarning))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                                dialog.cancel()
                            }
                        val alert = dialogBuilder.create()
                        alert.show()
                    }
                }
            }
        }
    }
    private fun gpsActive() {
        binding.GPS.setOnClickListener{
            settings?.location = 1
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)
            connectivity = context?.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
            if ( connectivity != null) {
                info = connectivity!!.activeNetworkInfo
                Log.i("TAG", "connectivity != null")
                if (info != null) {
                    if (info!!.state == NetworkInfo.State.CONNECTED) {

                    }
                    else{
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setMessage(getString(R.string.networkWarning))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                                dialog.cancel()
                            }
                        val alert = dialogBuilder.create()
                        alert.show()
                    }
                }
            }
        }
    }
    private fun notificationDeActivate() {
        binding.notiDisable.setOnClickListener{
            settings?.notification = false
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)

            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage(getString(R.string.warning))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                    dialog.cancel()
                }
            val alert = dialogBuilder.create()
            alert.show()
        }
    }
    private fun notificationActive() {
        binding.notiEnable.setOnClickListener{
            settings?.notification = true
            settingsViewModel.setSettingsSharedPrefs(settings as Setting)
        }
    }


    fun initDesign(){
        //UNIT
        if(settings?.unit as Int == 0){binding.standardUnit.isChecked = true}
        else if(settings?.unit as Int == 1){binding.metricUnit.isChecked = true}
        else{binding.metricUnit.isChecked = true}

        //LOCATION
        if(settings?.location as Int == 1){binding.GPS.isChecked = true}
        else if(settings?.location as Int == 2){binding.map.isChecked = true}
        //NOTIFICATION
        if(settings?.notification as Boolean){binding.notiEnable.isChecked = true}
        else{binding.notiDisable.isChecked = true}

        if(settings?.language as Boolean){ binding.englishLang.isChecked = true }
        else{ binding.arabicLang.isChecked = true }




    }


}