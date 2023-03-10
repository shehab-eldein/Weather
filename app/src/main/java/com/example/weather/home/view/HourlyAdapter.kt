package com.example.weather.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.DailyItemLayoutBinding
import com.example.weather.databinding.HourlyItemLayoutBinding
import com.example.weather.helper.Formmater
import com.example.weather.model.HourlyWeather

class HourlyAdapter(var context: Context, var hourlyWeather:List<HourlyWeather>
//, var tempUnit:String
) : RecyclerView.Adapter<HourlyAdapter.HourlyHolder>(){

    class HourlyHolder(var binding: HourlyItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    lateinit var binding: HourlyItemLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = HourlyItemLayoutBinding.inflate(inflater, parent, false)
        return HourlyHolder(binding)

    }

    override fun onBindViewHolder(holder: HourlyHolder, position: Int) {
        val oneHourlyWeather:HourlyWeather = hourlyWeather[position]
        holder.binding.hourlyTime.text = Formmater.getTimeFormat(oneHourlyWeather.dt)
        holder.binding.hourlyTemp.text = oneHourlyWeather.temp.toString()
        holder.binding.hourlyWindSpeed.text = oneHourlyWeather.wind_speed.toString()
        holder.binding.hourlyTempUnit.text = context.getString(R.string.Kelvin)
        holder.binding.hourlyWindUnit.text = context.getString(R.string.windMeter)
        // TODO: Units  
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

        val mainWeather =   hourlyWeather[position].weather[0].main

        when(mainWeather.lowercase()) {
            "thunderstorm"->  holder.binding.hourlyIcon .setImageResource(R.drawable.lightning)
            "drizzle"    -> holder.binding.hourlyIcon.setImageResource(R.drawable.drizzel)
            "rain","squall"    -> holder.binding.hourlyIcon.setImageResource(R.drawable.rain)
            "snow"    -> holder.binding.hourlyIcon.setImageResource(R.drawable.snow)
            "clouds"    -> holder.binding.hourlyIcon.setImageResource(R.drawable.cloudy)
            "haze" ,"mist","fog"  -> holder.binding.hourlyIcon.setImageResource(R.drawable.fog_haze)
            "smoke"  -> holder.binding.hourlyIcon.setImageResource(R.drawable.smoke)
            "dust","sand","tornado" -> holder.binding.hourlyIcon.setImageResource(R.drawable.sand)
            else ->  holder.binding.hourlyIcon.setImageResource(R.drawable.clear)
        }

       

    }

    override fun getItemCount(): Int {
        return hourlyWeather.size -24
    }





}

