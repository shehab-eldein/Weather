    package com.example.weather.Favorite.view

    import android.content.Context
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.airbnb.lottie.LottieAnimationView
    import com.example.weather.R
    import com.example.weather.databinding.FavoriteItemLayoutBinding
    import com.example.weather.helper.CurrentUser
    import com.example.weather.helper.Formmater
    import com.example.weather.helper.LocalityManager
    import com.example.weather.helper.UnitHandler
    import com.example.weather.adapters.DailyAdapter
    import com.example.weather.adapters.HourlyAdapter
    import com.example.weather.model.WeatherForecast
    import com.github.matteobattilana.weather.PrecipType

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

            return FavHolder(binding)

        }

        fun animateBG(type: PrecipType) {
            binding.weatherView2.apply {
                setWeatherData(type)
                speed = 300
                emissionRate = 200f // snow count
                angle = 0 // The angle of the fall
                fadeOutPercent = 0.9f // When to fade out (0.0f-1.0f)
            }
        }
        override fun onBindViewHolder(holder: FavHolder, position: Int) {


            hourlyAdapter = HourlyAdapter(context,favWeatherList[position].hourly)

            dailyAdapter = DailyAdapter(context,favWeatherList[position].daily)
            initRecycler()
            if (CurrentUser.settings.language) {
               holder.binding.currTemp.text = favWeatherList[position].current.temp.toString()

            } else {
                binding.currTemp.text = LocalityManager.convertToArabicNumber(favWeatherList[position].current.temp.toString())

            }
           holder.binding.currTime.text = Formmater.getTimeFormat(favWeatherList[position].current.dt)
            holder.binding.currDate.text =  Formmater.getDateFormat(favWeatherList[position].current.dt)
            holder.binding.currCity.text = LocalityManager.getAddressFromLatLng(context,favWeatherList[position].lat,favWeatherList[position].lon)
           holder.binding.currUnit .text = context.getString(  UnitHandler.getUnitName(CurrentUser.settings).first)
           // holder.binding.currTemp.text =  favWeatherList[position].current.temp.toString()
            holder.binding.currDesc.text =  favWeatherList[position].current.weather[0].description
            holder.binding.deleteIcon.setOnClickListener{ onClickHandler.onRemoveBtnClick(favWeatherList[position]) }
            val mainWeather =   favWeatherList[position].current.weather[0].main
            when(mainWeather.lowercase()) {
                "thunderstorm"->  holder.binding.currIcon.setImageResource(R.drawable.lightning)
                "drizzle"    -> {
                    holder.binding.currIcon.setImageResource(R.drawable.drizzel)
                    animateBG(PrecipType.RAIN)
                }

                "rain","squall"    -> {
                    holder.binding.currIcon.setImageResource(R.drawable.rain)
                    animateBG(PrecipType.RAIN)

                }
                "snow"    -> {
                    holder.binding.currIcon.setImageResource(R.drawable.snow)
                    animateBG(PrecipType.SNOW)
                }
                "clouds"    -> {
                    holder.binding.currIcon.setImageResource(R.drawable.cloudy)
                    animateBG(PrecipType.RAIN)
                }
                "haze" ,"mist","fog"  -> holder.binding.currIcon.setImageResource(R.drawable.fog_haze)
                "smoke"  -> holder.binding.currIcon.setImageResource(R.drawable.smoke)
                "dust","sand","tornado" -> holder.binding.currIcon.setImageResource(R.drawable.sand)
                else ->  holder.binding.currIcon.setImageResource(R.drawable.clear)
            }


        }
        fun initRecycler(){

            layoutManagerHourly = LinearLayoutManager(context as Context,
                LinearLayoutManager.HORIZONTAL,false)
            layoutManagerDaily= LinearLayoutManager(context as Context,
                LinearLayoutManager.HORIZONTAL,false)
            layoutManagerDaily = LinearLayoutManager(context as Context)
            binding.dailyRecycler.adapter = dailyAdapter
            binding.dailyRecycler.layoutManager = layoutManagerDaily



        }

        override fun getItemCount(): Int {
            return favWeatherList.size
        }

        fun setWeatherList(weatherList:List<WeatherForecast>){
            this.favWeatherList = weatherList
        }





    }

