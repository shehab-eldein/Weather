package com.example.weather.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.DailyItemLayoutBinding
import com.example.weather.databinding.FavoriteItemLayoutBinding
import com.example.weather.helper.Formmater
import com.example.weather.model.DailyWeather


class DailyAdapter(var context: Context, var dailyWeather:List<DailyWeather>,
// var tempUnit:String
  ):
    RecyclerView.Adapter<DailyAdapter.DailyHolder>() {


    class DailyHolder(var binding: DailyItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    lateinit var binding: DailyItemLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DailyItemLayoutBinding.inflate(inflater, parent, false)
        return DailyHolder(binding)

    }

        override fun onBindViewHolder(holder: DailyHolder, position: Int) {
            val oneDailyWeather: DailyWeather = dailyWeather[position]

            holder.binding.dailyDate.text = Formmater.getDayFormat(oneDailyWeather.dt)
            holder.binding.dailyDesc.text = oneDailyWeather.weather[0].description
            holder.binding.dailyTemp.text = "${oneDailyWeather.temp.max}/${oneDailyWeather.temp.min}"
            holder.binding.dailyUnit.text = context.getString(R.string.Kelvin)
            /*
            when(this.tempUnit) {
                "standard" ->{
                    holder.dailyUnit.text = context.getString(R.string.Kelvin)
                }
                "metric" ->{
                    holder.dailyUnit.text = context.getString(R.string.Celsius)
                }
                "imperial" ->{
                    holder.dailyUnit.text = context.getString(R.string.Fahrenheit)
                }
            }


             */

            val mainWeather =   dailyWeather[position].weather[0].main

            when(mainWeather.lowercase()) {
                "thunderstorm"->  holder.binding.dailyIcon.setImageResource(R.drawable.lightning)
                "drizzle"    -> holder.binding.dailyIcon.setImageResource(R.drawable.drizzel)
                "rain","squall"    -> holder.binding.dailyIcon.setImageResource(R.drawable.rain)
                "snow"    -> holder.binding.dailyIcon.setImageResource(R.drawable.snow)
                "clouds"    -> holder.binding.dailyIcon.setImageResource(R.drawable.cloudy)
                "haze" ,"mist","fog"  -> holder.binding.dailyIcon.setImageResource(R.drawable.fog_haze)
                "smoke"  -> holder.binding.dailyIcon.setImageResource(R.drawable.smoke)
                "dust","sand","tornado" -> holder.binding.dailyIcon.setImageResource(R.drawable.sand)
                else ->  holder.binding.dailyIcon.setImageResource(R.drawable.clear)
            }

        }

        override fun getItemCount(): Int {
            return dailyWeather.size-1
        }





    }
