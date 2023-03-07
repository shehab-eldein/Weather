package com.example.weather.Intro

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safyweather.Constants
import com.example.weather.R
import com.example.weather.databinding.FragmentIntroBinding
import com.example.weather.db.DBManager
import com.example.weather.helper.CurrentUser
import com.example.weather.model.Repo
import com.example.weather.model.Setting
import com.example.weather.networking.NetworkingManager
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng


class IntroFragment : Fragment() {

    lateinit var binding: FragmentIntroBinding
    private val TAG = "IntroFragment"
    val PERMISSION_ID = 100
    lateinit var fusedLocation: FusedLocationProviderClient
    lateinit var initialDialog: Dialog
    lateinit var navController: NavController
    var connectivity : ConnectivityManager? = null
    // TODO: change network info
    var info : NetworkInfo? = null
    private lateinit var setting: Setting
    private lateinit var repo: Repo
    lateinit var gpsLocation: RadioButton
    lateinit var startBtn: Button

    val locationCallBack = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            Log.i("TAG", "onLocationResult:")
            var loc = p0?.lastLocation as Location
            Log.i("TAG", "lattttttt :"+loc.latitude+" looooooong : "+loc.longitude)
            setting.location = 1
            // TODO: View Model For Intro
            //Add Defult Settings
            repo.addSettingsToSharedPreferences(setting)
            repo.add_LatLongToSP(LatLng(loc.latitude,loc.longitude))
            CurrentUser.location = LatLng(loc.latitude,loc.longitude)

            val action = IntroFragmentDirections.actionIntroFragmentToHomeFragment2()
            navController.navigate(action)


        }
        /*
          // repo.addSettingsToSharedPreferences(settings as com.example.weather.model.Settings)
    val action = InitialFragmentDirections.actionInitialFragmentToHomeFragment(
    loc.latitude.toFloat(),loc.longitude.toFloat(), arrayOfUnits[settings?.unit as Int],true)
            */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentIntroBinding.inflate(layoutInflater)
        animateBG(PrecipType.SNOW)
        setting = Setting()

        navController = Navigation.findNavController(requireActivity(), R.id.dashBoardContainer)
        repo  = Repo(NetworkingManager.getInstance(), DBManager(requireContext())
            ,requireContext(),
            requireContext().getSharedPreferences(Constants.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE))





    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())

        initDialog()
        startBtnClicked()


        return binding.root

    }
    fun animateBG(type:PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            speed = 300
            emissionRate = 50f // snow count
            angle = 0 // The angle of the fall
            fadeOutPercent = .70f // When to fade out (0.0f-1.0f)
        }
    }
    fun initDialog() {
        initialDialog = Dialog(requireContext())
        initialDialog.setContentView(R.layout.intro_dialog)
        initialDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initialDialog
        gpsLocation  = initialDialog.findViewById(R.id.gpsLocation)
        startBtn = initialDialog.findViewById(R.id.initialSetupBtn)
        initialDialog.show()
    }
    fun startBtnClicked() {
        startBtn.setOnClickListener {
            connectivity = context?.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager

            //check connection
            if ( connectivity != null) {
                info = connectivity!!.activeNetworkInfo

                if (info != null) {
                    if (info!!.state == NetworkInfo.State.CONNECTED) {
                        if(gpsLocation.isChecked) {
                            getFreshLocationRequest()
                            Log.i(TAG, "get location finnnnished")
                        }
                        else{
                            //there an argument here
                            val action =  IntroFragmentDirections.actionIntroFragmentToMapFragment()
                            navController.navigate(action)
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "check the network connection!", Toast.LENGTH_LONG).show()
                    }
                }
            }
            initialDialog.dismiss()
        }
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
        Log.i(TAG, "requestNewLocationData: ")
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