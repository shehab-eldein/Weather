    package com.example.weather.Favorite.view

    import android.content.Context
    import android.provider.Settings.Global.getString
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.constraintlayout.widget.ConstraintLayout
    import androidx.core.content.ContentProviderCompat.requireContext
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.example.weather.R
    import com.example.weather.alert.viewModel.AlertsAdapter
    import com.example.weather.databinding.FavoriteItemLayoutBinding
    import com.example.weather.databinding.ItemAlertBinding
    import com.example.weather.helper.Formmater
    import com.example.weather.helper.LocalityManager
    import com.example.weather.home.view.DailyAdapter
    import com.example.weather.home.view.HourlyAdapter
    import com.example.weather.model.Location
    import com.example.weather.model.WeatherForecast
    import java.util.Formatter

    class FavAdapter(var context: Context,var favWeatherList:List<WeatherForecast>
    ,var onClickHandler:OnFavWeatherClickListener
           ): RecyclerView.Adapter<FavAdapter.FavHolder>() {
        lateinit var layoutManagerHourly: LinearLayoutManager
        lateinit var layoutManagerDaily: LinearLayoutManager
        lateinit var hourlyAdapter: HourlyAdapter
        lateinit var dailyAdapter: DailyAdapter


        class FavHolder(var binding: FavoriteItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

        lateinit var binding: FavoriteItemLayoutBinding
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHolder {
            val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = FavoriteItemLayoutBinding.inflate(inflater, parent, false)
            return FavAdapter.FavHolder(binding)

        }

        override fun onBindViewHolder(holder: FavHolder, position: Int) {

            hourlyAdapter = HourlyAdapter(context,favWeatherList[position].hourly)

            dailyAdapter = DailyAdapter(context,favWeatherList[position].daily)
            initRecycler()
           holder.binding.currTime.text = Formmater.getTimeFormat(favWeatherList[position].current.dt)
            holder.binding.currDate.text =  Formmater.getDateFormat(favWeatherList[position].current.dt)
            holder.binding.currCity.text = LocalityManager.getAddressFromLatLng(context,favWeatherList[position].lat,favWeatherList[position].lon)
            //holder.binding.currHumidity.text = favWeatherList[position].current.humidity.toString()
            //holder.binding.currWindSpeed.text = favWeatherList[position].current.wind_speed.toString()
            //holder.binding.currClouds.text = favWeatherList[position].current.clouds.toString()
           //holder.binding.currPressure.text = favWeatherList[position].current.pressure.toString()
            //duplicated delete when remove the comment look for handel unit
            //holder.binding.currWindUnit.text  = "M/h"
            //holder.binding.currWindSpeed.text = favWeatherList[position].current.wind_speed.toString()



            Glide.with(context as Context)
                .load("https://openweathermap.org/img/wn/"+favWeatherList[position].current.weather[0].icon+"@2x.png")
                .into(holder.binding.currIcon)

            holder.binding.currTemp.text =  favWeatherList[position].current.temp.toString()
            holder.binding.currDesc.text =  favWeatherList[position].current.weather[0].main
            holder.binding.deleteIcon.setOnClickListener{ onClickHandler.onRemoveBtnClick(favWeatherList[position]) }
            //holder.binding.Con.setOnClickListener { onClickHandler.onFavItemClick(favWeatherList[position])}
           // hourlyAdapter = HourlyAdapter(context,favWeatherList[position].hourly)

           // dailyAdapter = DailyAdapter(context,favWeatherList[position].daily)



/*





            //use picasso
            hourlyAdapter = HourlyAdapter(requireContext(),weather.hourly)
            // hourlyAdapter.setHourlyWeatherList(weather.hourly)
            dailyAdapter = DailyAdapter(requireContext(),weather.daily)
            animLoading.visibility = View.GONE

 */

        }
        fun initRecycler(){

            layoutManagerHourly = LinearLayoutManager(context as Context,
                LinearLayoutManager.HORIZONTAL,false)
            layoutManagerDaily= LinearLayoutManager(context as Context,
                LinearLayoutManager.HORIZONTAL,false)
            layoutManagerDaily = LinearLayoutManager(context as Context)
           // binding.hourlyRecycler.adapter = hourlyAdapter
            binding.dailyRecycler.adapter = dailyAdapter
            //binding.hourlyRecycler.layoutManager = layoutManagerHourly
            binding.dailyRecycler.layoutManager = layoutManagerDaily



        }

        override fun getItemCount(): Int {
            return favWeatherList.size
        }

        @JvmName("setFavWeatherList1")
        fun setFavWeatherList(weatherList:List<WeatherForecast>){
            this.favWeatherList = weatherList
        }

        /*
        inner class FavoriteViewHolder( view: View): RecyclerView.ViewHolder(view) {
            val favConstraint: ConstraintLayout = view.findViewById(R.id.favConstraint)
            val addressName: TextView = view.findViewById(R.id.currCity)
            val removeFav: ImageView = view.findViewById(R.id.removeFav)
            val temp:TextView = view.findViewById(R.id.currTemp)
            val time:TextView = view.findViewById(R.id.testTime)
            val desc:TextView = view.findViewById(R.id.currDesc)
            val icon:ImageView = view.findViewById(R.id.currIcon)





        }

         */
    }

