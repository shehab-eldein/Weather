package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.weather.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navigationDrawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var navController: NavController
    lateinit var appBar: AppBarConfiguration
    private lateinit var mainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        navigationDrawer = mainBinding.mainDrawer
        navigationView = mainBinding.mainNavView
        toolbar = mainBinding.mainToolbar

        //Binding fragment
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.findNavController()
        appBar = AppBarConfiguration(setOf(R.id.homeFragment,R.id.favoriteFragment,R.id.settingsFragment,R.id.alertsFragment),navigationDrawer)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController,appBar)
        navigationView.setupWithNavController(navController)


    }
    //?
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBar) || super.onSupportNavigateUp()
    }
    //?
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}