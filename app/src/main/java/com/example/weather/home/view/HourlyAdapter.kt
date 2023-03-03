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
import com.example.weather.model.HourlyWeather


class HourlyAdapter(var context: Context, var hourlyWeather:List<HourlyWeather>
//, var tempUnit:String
) : RecyclerView.Adapter<HourlyAdapter.HourlyWeatherViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_item_layout,parent,false)
        return HourlyWeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val oneHourlyWeather:HourlyWeather = hourlyWeather[position]
        holder.hourlyTime.text = Formmater.getTimeFormat(oneHourlyWeather.dt)
        holder.hourlyTemp.text = oneHourlyWeather.temp.toString()
        holder.hourlyWindSpeed.text = oneHourlyWeather.wind_speed.toString()
        holder.tempUnit.text = context.getString(R.string.Kelvin)
        holder.windUnit.text = context.getString(R.string.windMeter)
        /*
        when(this.tempUnit) {
            "standard" ->{
                holder.tempUnit.text = context.getString(R.string.Kelvin)
                holder.windUnit.text = context.getString(R.string.windMeter)
            }
            "metric" ->{
                holder.tempUnit.text = context.getString(R.string.Celsius)
                holder.windUnit.text = context.getString(R.string.windMeter)
            }
            "imperial" ->{
                holder.tempUnit.text = context.getString(R.string.Fahrenheit)
                holder.windUnit.text = context.getString(R.string.windMile)
            }
        }
         */
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/"+oneHourlyWeather.weather[0].icon+"@2x.png")
            .into(holder.hourlyIcon)
    }

    override fun getItemCount(): Int {
        return hourlyWeather.size-24
    }

    fun setHourlyWeatherList(hourlyWeatherList:List<HourlyWeather>){
        this.hourlyWeather = hourlyWeatherList
    }

    inner class HourlyWeatherViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        val hourlyTime: TextView  = view.findViewById(R.id.hourlyTime)
        val hourlyTemp: TextView  = view.findViewById(R.id.hourlyTemp)
        val hourlyWindSpeed: TextView = view.findViewById(R.id.hourlyWindSpeed)
        val hourlyIcon: ImageView = view.findViewById(R.id.hourlyIcon)
        val tempUnit: TextView = view.findViewById(R.id.hourlyTempUnit)
        val windUnit: TextView = view.findViewById(R.id.hourlyWindUnit)
    }

}

