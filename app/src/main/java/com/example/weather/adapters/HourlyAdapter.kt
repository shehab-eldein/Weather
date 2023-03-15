package com.example.weather.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.HourlyItemLayoutBinding
import com.example.weather.helper.CurrentUser
import com.example.weather.helper.Formmater
import com.example.weather.helper.LocalityManager
import com.example.weather.helper.UnitHandler
import com.example.weather.model.HourlyWeather

class HourlyAdapter(var context: Context, var hourlyWeather:List<HourlyWeather>) : RecyclerView.Adapter<HourlyAdapter.HourlyHolder>(){

    class HourlyHolder(var binding: HourlyItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    lateinit var binding: HourlyItemLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = HourlyItemLayoutBinding.inflate(inflater, parent, false)
        return HourlyHolder(binding)

    }

    override fun onBindViewHolder(holder: HourlyHolder, position: Int) {
        val oneHourlyWeather:HourlyWeather = hourlyWeather[position]
        if (CurrentUser.settings.language) {
            holder.binding.hourlyTemp.text = oneHourlyWeather.temp.toString()
            holder.binding.hourlyWindSpeed.text = oneHourlyWeather.wind_speed.toString()
        } else {
            holder.binding.hourlyTemp.text = LocalityManager.convertToArabicNumber( oneHourlyWeather.temp.toString())
            holder.binding.hourlyWindSpeed.text = LocalityManager.convertToArabicNumber( oneHourlyWeather.wind_speed.toString())
        }

        holder.binding.hourlyTime.text = Formmater.getTimeFormat(oneHourlyWeather.dt)


        holder.binding.hourlyTempUnit.text = context.getString( UnitHandler.getUnitName(CurrentUser.settings).first)
        holder.binding.hourlyWindUnit.text =  context.getString( UnitHandler.getUnitName(CurrentUser.settings).second)
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

