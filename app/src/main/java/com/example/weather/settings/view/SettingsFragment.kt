package com.example.weather.settings.view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.Intro.IntroFragmentDirections
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.helper.CurrentUser
import com.example.weather.helper.LocalityManager
import com.example.weather.model.Setting
import com.example.weather.settings.viewmodel.SettingsViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private  val TAG = "SettingsFragment"
    lateinit var settingsViewModel: SettingsViewModel
    lateinit var binding: FragmentSettingsBinding
    private var settings: Setting? = null
    lateinit var navController: NavController
   lateinit var myView: View
    lateinit var fusedLocation: FusedLocationProviderClient
    val PERMISSION_ID = 100

    val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val loc = p0.lastLocation as Location
            settingsViewModel.addLocSP(LatLng(loc.latitude, loc.longitude))
            CurrentUser.location = LatLng(loc.latitude, loc.longitude)


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        navController = Navigation.findNavController(requireActivity(),R.id.dashBoardContainer)
        myView = view
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        settings = settingsViewModel.getStoredSettings()
        initDesign()
        radioBTnLogic()

    }



    private fun radioBTnLogic() {
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
    fun showSnackBar(str:String) {
        val snackbar = Snackbar.make(myView!!, str, Snackbar.LENGTH_LONG)
        snackbar.duration.times(200)
        snackbar.show()
    }
    fun restartApp() {
        requireActivity().finish()
        requireActivity().startActivity(requireActivity().intent)
        requireActivity().overridePendingTransition(0,2)
    }
    private fun enLang() {
        binding.englishLang.setOnClickListener{
            if(CurrentUser.isConnectedToNetwork) {
                settings?.language = true
                settingsViewModel.setSettingsSharedPrefs(settings as Setting)
                LocalityManager.setLocale(requireContext(),"en")
                restartApp()

            } else {
                showSnackBar(getString(R.string.connectToUse))
            }


        }
    }
    private fun arLang() {
        binding.arabicLang.setOnClickListener{

            if(CurrentUser.isConnectedToNetwork) {
                settings?.language = false
                settingsViewModel.setSettingsSharedPrefs(settings as Setting)
                LocalityManager.setLocale(requireContext(),"ar")
                restartApp()

            } else {
                showSnackBar(getString(R.string.connectToUse))
            }

        }
    }
    private fun imperialUnit() {
        binding.imperialUnit.setOnClickListener{
            if(CurrentUser.isConnectedToNetwork) {
                settings?.unit = 2
                settingsViewModel.setSettingsSharedPrefs(settings as Setting)

            } else {
                showSnackBar(getString(R.string.connectToUse))
            }

        }
    }
    private fun metricUnit() {
        binding.metricUnit.setOnClickListener{
            if(CurrentUser.isConnectedToNetwork) {
                settings?.unit = 1
                settingsViewModel.setSettingsSharedPrefs(settings as Setting)

            } else {
                showSnackBar(getString(R.string.connectToUse))
            }

        }
    }
    private fun standardUnit() {
        binding.standardUnit.setOnClickListener{

            if(CurrentUser.isConnectedToNetwork) {
                settings?.unit = 0
                settingsViewModel.setSettingsSharedPrefs(settings as Setting)

            } else {
                showSnackBar(getString(R.string.connectToUse))
            }

        }
    }
    private fun mapActive() {
        binding.map.setOnClickListener{
            settings?.location = 2
            settingsViewModel.setSettingsSharedPrefs(settings!!)
            if(CurrentUser.isConnectedToNetwork) {
                val action = SettingsFragmentDirections.actionSettingsFragment2ToMapFragment().setIsSeetings(true)
                navController.navigate(action)
            } else {
                showSnackBar(getString(R.string.connectToUse))
            }

                    }

                }


    private fun gpsActive() {

        binding.GPS.setOnClickListener {
            if (CurrentUser.isConnectedToNetwork) {

                getFreshLocationRequest()
                settings?.location = 1
                settingsViewModel.setSettingsSharedPrefs(settings!!)
            }else {
                showSnackBar(getString(R.string.connectToUse))
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
        else{binding.imperialUnit.isChecked = true}

        //LOCATION
        if(settings?.location as Int == 1){binding.GPS.isChecked = true}
        else if(settings?.location as Int == 2){binding.map.isChecked = true}
        //NOTIFICATION
        if(settings?.notification as Boolean){binding.notiEnable.isChecked = true}
        else{binding.notiDisable.isChecked = true}

        if(settings?.language as Boolean){ binding.englishLang.isChecked = true }
        else{ binding.arabicLang.isChecked = true }




    }
    fun checkPermissions():Boolean{
        Log.i(TAG, "checkPermissions: ")
        return ActivityCompat.checkSelfPermission(context as Context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context as Context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled():Boolean{
        var lm: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun enableLocationSettings(){
        var settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(settingsIntent)
    }

    fun requestLocationPermissions(){
        Log.i("TAG", "requestPermissions: ")
        requestPermissions(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_ID)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionsResult: ")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_ID){
            if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "onRequestPermissionsResult: oooooooooooooooooooooooooooooooooooooooooooooo")
                requestNewLocationData()
            }
            else{
                Toast.makeText(context as Context, "permission deneyed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData(){
        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(5)
        locationRequest.setFastestInterval(0)
        locationRequest.setNumUpdates(1)

        fusedLocation.requestLocationUpdates(locationRequest,locationCallBack, Looper.myLooper())
    }

    fun getFreshLocationRequest(){
        if(checkPermissions()) {
            Log.i(TAG, "if --> permissions checked successfully ")
            if(isLocationEnabled()) {
                Log.i(TAG, "if --> permissions enabeled successfully ")
                requestNewLocationData();
            }
            else{
                enableLocationSettings();
            }
        }
        else{
            Log.i(TAG, "else: --> request permissions ")
            Toast.makeText(activity, "noPermission", Toast.LENGTH_SHORT).show();
            requestLocationPermissions()
        }
    }



}