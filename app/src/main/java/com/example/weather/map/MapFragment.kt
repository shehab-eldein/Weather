package com.example.weather.map


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.weather.R
import com.example.weather.db.DBManager
import com.example.weather.helper.Constants
import com.example.weather.helper.CurrentUser
import com.example.weather.helper.LocalityManager
import com.example.weather.model.repo.Repo
import com.example.weather.model.Setting
import com.example.weather.networking.NetworkingManager
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class MapFragment : Fragment() {

    private lateinit var navController: NavController
    private val args: MapFragmentArgs by navArgs()
    private lateinit var repo: Repo
    private var setting: Setting? = null

    private val callback = OnMapReadyCallback { googleMap ->
        moveCamera(googleMap)
        onMapClicked(googleMap)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        repo = Repo.getInstance(
            NetworkingManager.getInstance(), DBManager(requireContext()), requireContext(),
            requireContext().getSharedPreferences(
                Constants.MY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
      //  setting = repo.getSettingsSharedPreferences()
        setting = Setting()

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        navController = Navigation.findNavController(requireActivity(), R.id.dashBoardContainer)
        initAutoComplete()
    }
    fun initAutoComplete() {

        val apiKey = "AIzaSyATC4Zk0_xofsFUTm0GRIyNej3syHx5oro"

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        val autocompleteSupportFragment1 = childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment?



        autocompleteSupportFragment1!!.setPlaceFields(listOf(Place.Field.LAT_LNG,))

        autocompleteSupportFragment1.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                nextDestination(place.latLng)

            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(),"Some error occurred  ${status.statusMessage}", Toast.LENGTH_SHORT).show()
                Log.i("key", "onError: ${status.statusMessage}")
            }
        })



    }

    fun moveCamera(map: GoogleMap, location: LatLng = LatLng(CurrentUser.location.latitude, CurrentUser.location.longitude)) {
        map.addMarker(MarkerOptions().position(location))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    fun onMapClicked(map: GoogleMap) {
        map.setOnMapClickListener {
            map.clear()
            moveCamera(map, LatLng(it.latitude, it.longitude))

            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage(getString(R.string.saveLocation))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.save)) { dialog, id ->
                    //there ara another argument here
                    nextDestination(it)
                    dialog.cancel()
                }
                .setNegativeButton(getString(R.string.Cancel)) { dialog, id -> dialog.cancel() }
            val alert = dialogBuilder.create()
            alert.show()
        }
    }

    fun nextDestination(loc: LatLng) {




        if (args.isHome) {
            CurrentUser.location = loc
            repo.addSettingsToSharedPreferences(setting!!)
            repo.add_LatLongToSP(LatLng(loc.latitude,loc.longitude))
            CurrentUser.location = LatLng(loc.latitude,loc.longitude)
            val action = MapFragmentDirections.actionMapFragmentToHomeFragment()
            navController.navigate(action)

        }
        if (args.isSeetings){
            CurrentUser.location = loc
            repo.addSettingsToSharedPreferences(setting!!)
            repo.add_LatLongToSP(LatLng(loc.latitude,loc.longitude))
            CurrentUser.location = LatLng(loc.latitude,loc.longitude)
            val action = MapFragmentDirections.actionMapFragmentToSettingsFragment2()
            navController.navigate(action)
        }

        if(args.isAlert) {
            CurrentUser.alertLocation = loc
            val action = MapFragmentDirections.actionMapFragmentToAlertFragment()
            navController.navigate(action)
        }


        else if (!args.isAlert  && !args.isHome && !args.isSeetings){
            val action = MapFragmentDirections.actionMapFragmentToFavoriteFragment()
                .setLongt(loc.longitude.toFloat())
                .setLat(loc.latitude.toFloat())
                .setName(
                    LocalityManager.getAddressFromLatLng(
                        requireContext(),
                        loc.latitude,
                        loc.longitude
                    )
                )
            navController.navigate(action)
        }




    }


}

