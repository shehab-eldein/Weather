package com.example.weather.Favorite.view

import android.app.AlertDialog
import android.app.Service
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.Favorite.viewModel.FavViewModel
import com.example.weather.R
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.db.DBState
import com.example.weather.helper.CurrentUser
import com.example.weather.model.WeatherForecast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavoriteFragment : Fragment()
    ,OnFavWeatherClickListener {

    private  val TAG = "FavoriteFragment"
    private lateinit var navController: NavController
    private lateinit var favAdapter: FavAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var favViewModel:FavViewModel
    private lateinit var binding: FragmentFavoriteBinding
    val args :FavoriteFragmentArgs by navArgs()

    var weatherForecast:WeatherForecast? = null
    var myView:View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        initVar(view)
        setupFavRecycler()
        addBtnClicked()

        if(CurrentUser.isConnectedToNetwork) {
            onlineState()
        }else {
            offlineState()
        }



    }
    fun onlineState() {
        lifecycleScope.launch(Dispatchers.IO){
            if (args.lat.toDouble() != 0.0) {
                insertWeather()
            }
            withContext(Dispatchers.Main) {
                getUpdatedWeather()
            }
        }
    }
    fun showSnackBar(str:String) {
        val snackbar = Snackbar.make(myView!!, str, Snackbar.LENGTH_LONG)
        snackbar.duration.times(200)
        snackbar.show()
    }
    fun offlineState() {
          showSnackBar(getString(R.string.snackFAvUpdate))
        lifecycleScope.launch(){
            favViewModel.getOfflineFav().collectLatest {
                when (it) {
                    is DBState.onFail -> { } //hide loader show alert
                    is DBState.onSuccessList -> {
                        favAdapter.setWeatherList(it.weatherList)
                        Log.i(TAG, "offlineState: ${it.weatherList.size}")
                    }
                    else -> { }//Still loading
                }
                favAdapter.notifyDataSetChanged()
            }

        }

    }

    fun initVar(view:View) {
        binding = FragmentFavoriteBinding.bind(view)
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        navController = Navigation.findNavController(requireActivity(),R.id.dashBoardContainer)

    }
    fun addBtnClicked() {

        binding.floatingAddFav.setOnClickListener {
            if (CurrentUser.isConnectedToNetwork) {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToMapFragment()
                    .setIsHome(false)
                navController.navigate(action)

            } else {
                showSnackBar(getString(R.string.connectToUse))
            }

        }
    }
    suspend fun insertWeather() {
        weatherForecast = favViewModel.getWeather(args.lat.toDouble(),args.longt.toDouble())
        if (weatherForecast !=null )
            favViewModel.addtoFav(weatherForecast as WeatherForecast)
    }
    fun deleteWeather(weather: WeatherForecast) {
        lifecycleScope.launch(Dispatchers.IO) {
            favViewModel.delete(weather)
            withContext(Dispatchers.Main) {

                getUpdatedWeather()
            }

        }
    }
    suspend fun getUpdatedWeather() {
    lifecycleScope.launch() {
            favViewModel.getAllFav().collectLatest{

                when (it) {
                    is DBState.onFail -> { } //hide loader show alert
                    is DBState.onSuccessList -> { favAdapter.setWeatherList(it.weatherList) }
                    else -> { }//Still loading
                }
                favAdapter.notifyDataSetChanged()
            }

            delay(1000)
        }.job.join()
    }


    fun setupFavRecycler(){
        favAdapter = FavAdapter(requireContext(), emptyList(),this)
        binding.favoriteRecycler.adapter = favAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.favoriteRecycler.layoutManager = layoutManager
    }

    override fun onRemoveBtnClick(weather: WeatherForecast) {

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage(getString(R.string.deleteMsg))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.delete)) { dialog, id ->
                deleteWeather(weather)
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.Cancel)) { dialog, id -> dialog.cancel() }
        val alert = dialogBuilder.create()
        alert.show()

    }


    // TODO: dynamic background 





}