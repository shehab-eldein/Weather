package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.weather.alert.AlertManager
import com.example.weather.alert.viewModel.AlertsFactory
import com.example.weather.alert.viewModel.AlertsViewModel
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.db.DBManager
import com.example.weather.helper.Constants
import com.example.weather.home.view.HomeFragment
import com.example.weather.model.AlertData
import com.example.weather.model.Repo
import com.example.weather.networking.NetworkingManager
import com.example.weather.settings.view.SettingsFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


     val bottomChip by lazy { findViewById<ChipNavigationBar>(R.id.bottomChip) }

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: AlertsViewModel
    lateinit var factory: AlertsFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = AlertsFactory( Repo(
            NetworkingManager.getInstance(),
            DBManager.getInstance(this),this,this.getSharedPreferences(
                Constants.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE))
        )
        viewModel = ViewModelProvider(this,factory).get(AlertsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.dashBoardContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
        bottomChip.setItemSelected(R.id.homeBtn)
        bottomChip.setOnItemSelectedListener { id->

            when(id) {
                 R.id.homeBtn ->
                     findNavController(this@MainActivity, R.id.dashBoardContainer).navigate(R.id.homeFragment)

                R.id.favBtn ->
                    findNavController(this@MainActivity, R.id.dashBoardContainer).navigate(R.id.favoriteFragment)

                R.id.alertBtn ->
                   findNavController(this@MainActivity, R.id.dashBoardContainer).navigate(R.id.alertFragment)
                else ->
                    findNavController(this@MainActivity, R.id.dashBoardContainer).navigate(R.id.settingsFragment2)
            }


        }
        handelNavigationVisual()

        activateAlerts()
    }
    private fun activateAlerts() {
        val alertsManager = AlertManager(this)
        var alerts : List<AlertData>
        lifecycleScope.launch {
             viewModel.getAllAlertsInVM().observe(this@MainActivity) { it ->
                it.forEach {
                    Log.i("alertsssMain", "activateAlerts:Fireee ")
                    alertsManager.fireAlert(it)
                }
            }
        }
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