package com.example.weather.alert.viewModel
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.Favorite.view.OnFavWeatherClickListener
import com.example.weather.alert.view.OnDeleteAlertListener
import com.example.weather.databinding.ItemAlertBinding
import com.example.weather.model.AlertData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AlertsAdapter (private val lifeCycleScopeInput: CoroutineScope,var onClickHandler: OnDeleteAlertListener
)
    : ListAdapter<AlertData, AlertsAdapter.AlertViewHolder>(AlertDiffUtil()){


    class AlertViewHolder(var binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {}

class AlertDiffUtil : DiffUtil.ItemCallback<AlertData>() {
    override fun areItemsTheSame(oldItem: AlertData, newItem: AlertData): Boolean {
        return (oldItem.idHashLongFromLonLatStartStringEndStringAlertType == newItem.idHashLongFromLonLatStartStringEndStringAlertType)
    }

    override fun areContentsTheSame(oldItem: AlertData, newItem: AlertData): Boolean {
        return oldItem == newItem
    }

}

    lateinit var binding: ItemAlertBinding

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parent.context)

        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemAlertBinding.inflate(inflater, parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.tvLocationAlert.text = current.address

        holder.binding.imgDeleteAlert.setOnClickListener {
            onClickHandler.onRemoveBtnClick(current)
        }

        holder.binding.tvStartTime.text = current.startString



    }

}