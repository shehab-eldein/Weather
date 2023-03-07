package com.example.weather.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

import com.example.weather.Intro.IntroFragmentDirections
import com.example.weather.R
import com.example.weather.db.DBManager
import com.example.weather.helper.Constants
import com.example.weather.helper.CurrentUser
import com.example.weather.helper.LocalityManager
import com.example.weather.model.Repo
import com.example.weather.model.Setting
import com.example.weather.networking.NetworkingManager
import com.example.weather.settings.view.SettingsFragmentDirections

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapFragment : Fragment() {

    private lateinit var navController: NavController

    // private lateinit var favViewModelFactory:FavoriteViewModelFactory
    // private lateinit var favViewModel:FavoriteViewModel
    private val args: MapFragmentArgs by navArgs()
    private lateinit var repo: Repo

    // TODO: duplicated setting object we need setting class
    private var setting: Setting? = null
    private val callback = OnMapReadyCallback { googleMap ->
        moveCamera(googleMap)
        onMapClicked(googleMap)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        repo = Repo.getInstance(
            NetworkingManager.getInstance(), DBManager(requireContext()), requireContext(),
            requireContext().getSharedPreferences(
                Constants.MY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
        setting = Setting()
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        navController = Navigation.findNavController(requireActivity(), R.id.dashBoardContainer)
    }

    fun moveCamera(
        map: GoogleMap,
        location: LatLng = LatLng(
            CurrentUser.location.latitude,
            CurrentUser.location.longitude
        )
    ) {
        map.addMarker(MarkerOptions().position(location))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

    }

    fun onMapClicked(map: GoogleMap) {
        map.setOnMapClickListener {
            //get clicked lat and long adress name to put it in favorite
            var addressName =
                LocalityManager.getAddressFromLatLng(requireContext(), it.latitude, it.longitude)

            //clear all marker
            map.clear()
            moveCamera(map, LatLng(it.latitude, it.longitude))


            // TODO:  Duplicated alertDialog make in seperate class
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage(getString(R.string.saveLocation))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.save)) { dialog, id ->
                    //there ara another argument here
                    nextDestination(it)
                    dialog.cancel()
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, id -> dialog.cancel() }
            val alert = dialogBuilder.create()
            alert.show()
        }
    }

    fun nextDestination(loc: LatLng) {

        repo.add_LatLongToSP(loc)
        if (args.isHome) {
            CurrentUser.location = loc
            repo.addSettingsToSharedPreferences(setting!!)
            repo.add_LatLongToSP(LatLng(loc.latitude,loc.longitude))
            CurrentUser.location = LatLng(loc.latitude,loc.longitude)

            // TODO: Remove current user and use args
            val action = MapFragmentDirections.actionMapFragmentToHomeFragment2()
            navController.navigate(action)
            //Navigation.findNavController(requireActivity(), R.id.dashBoardContainer).navigate(R.id.homeFragment)
        } else {
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
/*
setting.location = 1
//Add Defult Settings
repo.addSettingsToSharedPreferences(setting)
repo.add_LatLongToSP(LatLng(loc.latitude,loc.longitude))
CurrentUser.location = LatLng(loc.latitude,loc.longitude)

val action = IntroFragmentDirections.actionIntroFragmentToHomeFragment2()
navController.navigate(action)

 */
