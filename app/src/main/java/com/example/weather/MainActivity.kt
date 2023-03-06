package com.example.weather

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.home.view.HomeFragment
import com.example.weather.settings.view.SettingsFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {


     val bottomChip by lazy { findViewById<ChipNavigationBar>(R.id.bottomChip) }

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.dashBoardContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
        bottomChip.setItemSelected(R.id.homeBtn)
        bottomChip.setOnItemSelectedListener { id->

            when(id) {
                 R.id.homeBtn ->
                     findNavController(this@MainActivity, R.id.dashBoardContainer).navigate(R.id.homeFragment)

                R.id.favBtn ->
                    findNavController(this@MainActivity, R.id.dashBoardContainer).navigate(R.id.favoriteFragment)

                else ->
                    findNavController(this@MainActivity, R.id.dashBoardContainer).navigate(R.id.settingsFragment2)
            }


        }
        handelNavigationVisual()

    }


    fun handelNavigationVisual() {
        navController.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                @NonNull controller: NavController,
                @NonNull destination: NavDestination,
                arguments: Bundle?
            ) {
                when (destination.id) {

                    R.id.splashFragment,
                    R.id.introFragment,
                    R.id.mapFragment
                    -> bottomChip.setVisibility(
                        View.GONE
                    )
                    else -> bottomChip.setVisibility(View.VISIBLE)
                }
            }
        })
    }








}