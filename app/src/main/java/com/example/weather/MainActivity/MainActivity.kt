package com.example.weather.MainActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.weather.R
import com.example.weather.alert.AlertManager
import com.example.weather.alert.viewModel.AlertsViewModel
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.helper.CurrentUser
import com.example.weather.helper.LocalityManager
import com.example.weather.home.viewModel.ViewModelHome
import com.example.weather.helper.MyState
import com.google.android.material.snackbar.Snackbar
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val bottomChip by lazy { findViewById<ChipNavigationBar>(R.id.bottomChip) }
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    // TODO: add to main View Model
    lateinit var alertViewModel: AlertsViewModel
    lateinit var hviewModel: ViewModelHome
    lateinit var mainViewModel: MainActivityViewModel
    lateinit var constraintLayout : ConstraintLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initVar()
        handelNavigation()
        activateAlerts()
        handelLanguage()
        onNetworkStateChange()
        networkStateOnStart()


    }
    private fun initVar() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hviewModel = ViewModelProvider(this).get(ViewModelHome::class.java)
        mainViewModel = ViewModelProvider(this).get((MainActivityViewModel::class.java))
        constraintLayout = findViewById(R.id.main)
        alertViewModel = ViewModelProvider(this).get(AlertsViewModel::class.java)
    }
    private fun networkStateOnStart() {
        CurrentUser.isConnectedToNetwork = mainViewModel.checkForInternet(this@MainActivity)
    }
    private fun onNetworkStateChange() {
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.networkState.collectLatest {

                when (it) {
                    MyState.Fetched -> {
                        val snackbar = Snackbar.make(constraintLayout, "Connected to internet", Snackbar.LENGTH_LONG)
                        snackbar.show()
                        CurrentUser.isConnectedToNetwork = true

                    }
                    MyState.Error -> {
                        val snackbar = Snackbar.make(constraintLayout, "Lost connection to internet", Snackbar.LENGTH_LONG)
                        snackbar.show()
                        CurrentUser.isConnectedToNetwork = false

                    }
                }
            }
        }
    }
    private fun handelLanguage() {

        if (hviewModel.getStoredSettings()?.language == false) {
            LocalityManager.setLocale(this,"ar")

        }else {
            LocalityManager.setLocale(this,"en")

        }

    }
    private fun handelNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.dashBoardContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
        bottomChip.setItemSelected(R.id.homeBtn)
        bottomChip.setOnItemSelectedListener { id->

            when(id) {

                R.id.homeBtn ->

                    findNavController(this@MainActivity,
                        R.id.dashBoardContainer
                    ).navigate(R.id.homeFragment)

                R.id.favBtn ->
                    findNavController(this@MainActivity,
                        R.id.dashBoardContainer
                    ).navigate(R.id.favoriteFragment)

                R.id.alertBtn ->
                    findNavController(this@MainActivity,
                        R.id.dashBoardContainer
                    ).navigate(R.id.alertFragment)
                else ->
                    findNavController(this@MainActivity,
                        R.id.dashBoardContainer
                    ).navigate(R.id.settingsFragment2)
            }



        }
    }
    private fun activateAlerts() {
        val alertsManager = AlertManager(this)
        lifecycleScope.launch {
             alertViewModel.getAllAlertsInVM().observe(this@MainActivity) { it ->
                it.forEach {
                    Log.i("alertsssMain", "activateAlerts:Fireee ")
                    alertsManager.fireAlert(it)
                }
            }
        }
    }















}