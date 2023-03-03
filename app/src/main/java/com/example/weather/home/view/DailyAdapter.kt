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
import com.example.weather.helper.Formmater
import com.example.weather.model.DailyWeather


class DailyAdapter(var context: Context, var dailyWeather:List<DailyWeather>,
// var tempUnit:String
  ):
    RecyclerView.Adapter<DailyAdapter.DailyWeatherViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_item_layout,parent,false)
            return DailyWeatherViewHolder(view)
        }

        override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
            val oneDailyWeather: DailyWeather = dailyWeather[position]
            holder.dailyDate.text = Formmater.getDayFormat(oneDailyWeather.dt)
            holder.dailyDesc.text = oneDailyWeather.weather[0].description
            holder.dailyTemp.text = "${oneDailyWeather.temp.max}/${oneDailyWeather.temp.min}"
            holder.dailyUnit.text = context.getString(R.string.Kelvin)
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
            //Change to picasso
            Glide.with(context)
                .load("https://openweathermap.org/img/wn/"+oneDailyWeather.weather[0].icon+"@2x.png")
                .into(holder.dailyIcon)

        }

        override fun getItemCount(): Int {
            return dailyWeather.size-1
        }

        fun setDailyWeatherList(dailyWeatherList:List<DailyWeather>){
            this.dailyWeather = dailyWeatherList
        }

        inner class DailyWeatherViewHolder(private val view: View): RecyclerView.ViewHolder(view){
            val dailyDate: TextView = view.findViewById(R.id.dailyDate)
            val dailyDesc: TextView = view.findViewById(R.id.dailyDesc)
            val dailyTemp: TextView = view.findViewById(R.id.dailyTemp)
            val dailyIcon: ImageView = view.findViewById(R.id.dailyIcon)
            val dailyUnit: TextView  = view.findViewById(R.id.dailyUnit)
        }
    }
