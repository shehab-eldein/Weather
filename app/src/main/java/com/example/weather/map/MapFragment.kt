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
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.R

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
   // private val fragmentType:MapFragmentArgs by navArgs()

    // TODO: duplicated setting object we need setting class
    private var setting: com.example.weather.model.Setting? = null
    private val callback = OnMapReadyCallback { googleMap ->
        moveCamera(googleMap)
        onMapClicked(googleMap)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
      //  navController = Navigation.findNavController(activity as AppCompatActivity, R.id.nav_host_fragment)
    }

    fun moveCamera(map:GoogleMap, location: LatLng = LatLng(30.128430, 31.323850)) {
        map.addMarker(MarkerOptions().position(location))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15f))

    }
    fun onMapClicked(map: GoogleMap) {
        map.setOnMapClickListener{
            //get clicked lat and long adress name to put it in favorite
            var addressName = getAddressFromLatLng(it.latitude,it.longitude)

            //clear all marker
            map.clear()
            moveCamera(map,LatLng(it.latitude, it.longitude))


            // TODO:  Duplicated alertDialog make in seperate class
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage(getString(R.string.saveLocation))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.save)) { dialog, id ->
                    //there ara another argument here
                   // val action =  MapFragmentDirections.actionMapFragmentToHomeFragment2(it.latitude.toFloat(),it.longitude.toFloat(),)
                    //navController.navigate(action)
                    dialog.cancel()
                }
                .setNegativeButton(getString(R.string.cancel)) {dialog, id -> dialog.cancel()}
            val alert = dialogBuilder.create()
            alert.show()
        }
    }
    fun getAddressFromLatLng(lat:Double,longg:Double) : String{
        var geocoder = Geocoder(context as Context, Locale.getDefault())
        var addresses:List<Address>

        addresses = geocoder.getFromLocation(lat,longg,1) as List<Address>
        if(addresses.size>0) {
            return addresses.get(0).getAddressLine(0)
        }
        return ""
    }
}