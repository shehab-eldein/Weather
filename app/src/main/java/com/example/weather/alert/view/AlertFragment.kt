package com.example.weather.alert.view


import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.alert.viewModel.AlertsViewModel
import com.example.weather.databinding.FragmentAlertBinding
import com.example.weather.db.DBManager
import com.example.weather.helper.Constants
import com.example.weather.helper.CurrentUser
import com.example.weather.helper.Formmater
import com.example.weather.helper.LocalityManager
import com.example.weather.model.AlertData
import com.example.weather.model.Repo
import com.example.weather.networking.NetworkingManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class AlertFragment : Fragment(),OnDeleteAlertListener {
    private  val TAG = "AlertFragment"
    lateinit var binding: FragmentAlertBinding
    lateinit var alertsAdapter: AlertsAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    lateinit var builder: AlertDialog.Builder
    lateinit var dialogView: View
    lateinit var alertDialog: AlertDialog
    lateinit var viewModel: AlertsViewModel
    lateinit var myView: View




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AlertsViewModel::class.java)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myView = view
        initFrag()
        activateFABAlerts()
        setupAlertsAdapter()

    }


    private fun initFrag() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
       sharedPreferencesEditor = sharedPreferences.edit()
        builder = AlertDialog.Builder(activity)
        dialogView = layoutInflater.inflate(R.layout.alert_dialog, null)


    }


    private fun activateFABAlerts() {

        binding.fabAlerts.setOnClickListener {

            if(CurrentUser.isConnectedToNetwork) {

                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    val builder = AlertDialog.Builder(requireContext())

                    builder.setMessage(getString(R.string.permissionRequired))
                        .setTitle(getString(R.string.permissionTitle))

                    builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
                if (!Settings.canDrawOverlays(requireContext())) {

                    val builder = AlertDialog.Builder(requireContext())

                    builder.setMessage(getString(R.string.permissionRequired))
                        .setTitle(getString(R.string.permissionTitle))

                    builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        intent.data = Uri.parse("package:" + requireActivity().packageName)
                        startActivityForResult(intent, 0)
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
                if ((ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                            ) && (Settings.canDrawOverlays(requireContext()))
                ) {
                    showAddAlertDialog();
                }
            }
           else {

                val snackbar = Snackbar.make(myView!!, getString(R.string.connectToUse), Snackbar.LENGTH_LONG)
                snackbar.duration.times(200)
                snackbar.show()
           }


        }
    }

    private fun showAddAlertDialog() {

        (dialogView.parent as ViewGroup?)?.removeView(dialogView)

        builder.setView(dialogView)
        val textViewStartDate = dialogView.findViewById<TextView>(R.id.tv_start_date)
       // val textViewEndDate = dialogView.findViewById<TextView>(R.id.tv_end_date)
        val tvLocation = dialogView.findViewById<TextView>(R.id.tv_location)
        textViewStartDate.setOnClickListener { showDatePicker(textViewStartDate) }
       // textViewEndDate.setOnClickListener { showDatePicker(textViewEndDate) }
        tvLocation.setOnClickListener { tvLocationClicked() }
        builder.setPositiveButton(getString(R.string.save)) { _, i -> saveClicked(textViewStartDate, textViewStartDate) }
        builder.setNegativeButton(getString(R.string.Cancel)) { dialogInterface, i -> dialogInterface.dismiss() }

        alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.rgb(29,41,86)))
       // alertDialog.window?.setBackgroundDrawable(Drawable.createFromPath("@drawable/rounded_corners"))

        alertDialog.setOnShowListener(OnShowListener {
            alertDialog .getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
            alertDialog .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        })
        alertDialog.show()


    }


    private fun showDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { datePicker, year, month, day ->
                val date = day.toString() + " " + NativeDate.getMonth(month) + ", " + year
                showTimePicker(textView, date)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }
    private fun showTimePicker(textView: TextView, date: String) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            activity,
            { timePicker, hourOfDay, minute ->
                val time = "$hourOfDay:$minute"
                val dateTime = "$date $time"
                textView.text = dateTime
            }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], false
        )
        timePickerDialog.show()
    }
    class NativeDate {
        companion object {
            fun getMonth(month: Int): String {
                val months = arrayOf(
                    "January",
                    "February",
                    "March",
                    "April",
                    "May",
                    "June",
                    "July",
                    "August",
                    "September",
                    "October",
                    "November",
                    "December"
                )
                return months[month]
            }
        }
    }

    private fun tvLocationClicked() {
        sharedPreferencesEditor.putBoolean("tvLocationClicked", true)
        sharedPreferencesEditor.apply()

        findNavController().navigate(AlertFragmentDirections.actionAlertFragmentToMapFragment().setIsAlert(true))


    }

    private fun saveClicked(textViewStartDate: TextView, textViewEndDate: TextView) {
        if (dialogView.findViewById<TextView>(R.id.tv_location).text.isNotEmpty() && textViewStartDate.text.isNotEmpty() && textViewEndDate.text.isNotEmpty() && (dialogView.findViewById<RadioButton>(
                R.id.rbNotification
            ).isChecked || dialogView.findViewById<RadioButton>(R.id.rbAlarm).isChecked)
        ) {


            val dateStart = Formmater.getDateFormat(textViewStartDate.text.toString())
            val unixTimeDTStart = dateStart?.time?.div(1000)


            lifecycleScope.launch {
                if (unixTimeDTStart != null

                  ) {

                    var alertType = ""
                    if (dialogView.findViewById<RadioButton>(R.id.rbNotification).isChecked) {
                        alertType = "notification"
                    } else if (dialogView.findViewById<RadioButton>(R.id.rbAlarm).isChecked) {
                        alertType = "alarm"
                    }

                    val alertItem = AlertData(
                        address = LocalityManager.getAddressFromLatLng(requireContext(),CurrentUser.alertLocation.latitude,CurrentUser.alertLocation.longitude),
                        longitudeString = CurrentUser.alertLocation.longitude.toString()
                        ,
                        latitudeString = CurrentUser.alertLocation.latitude.toString()
                        ,
                        startString = textViewStartDate.text.toString(),
                        endString = textViewEndDate.text.toString(),
                        startDT = unixTimeDTStart.toInt(),
                        endDT = 11
                        ,
                        idHashLongFromLonLatStartStringEndStringAlertType = (

                                CurrentUser.location.longitude.toString()+CurrentUser.location.latitude.toString()
                                        + textViewStartDate.text.toString() + textViewEndDate.text.toString() + alertType


                                ).hashCode()
                            .toLong(),
                        alertType = alertType
                    )

                    viewModel.addAlertInVM(alertItem)



                }
            }

        } else {
            Log.i(TAG, "saveClicked: not all fields are clicked")
        }
    }

    private fun setupAlertsAdapter() {
        val mlayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        alertsAdapter = AlertsAdapter(lifecycleScope,this)

        binding.rvAlerts.apply {
            layoutManager = mlayoutManager
            adapter = alertsAdapter
        }

        //get all alertFrom Repo
        viewModel.getAllAlertsInVM().observe(viewLifecycleOwner) {
            alertsAdapter.submitList(it)
        }





    }

    override fun onPause() {

        super.onPause()
        if (sharedPreferences.getBoolean("tvLocationClicked", false)) {

            sharedPreferencesEditor.putBoolean("tvLocationClicked", false)

            sharedPreferencesEditor.putString(
                "start_date",
                dialogView.findViewById<TextView>(R.id.tv_start_date).text.toString()
            )
            sharedPreferencesEditor.putString(
                "end_date",
                //dialogView.findViewById<TextView>(R.id.tv_end_date).text.toString()
            "s"
            )
            sharedPreferencesEditor.putString(
                "ALERT_ADDRESS",
                dialogView.findViewById<TextView>(R.id.tv_location).text.toString()
            )

            var alartTypeSelected = ""

            if (dialogView.findViewById<RadioButton>(R.id.rbNotification).isChecked) {
                alartTypeSelected = "notification"
            } else if (dialogView.findViewById<RadioButton>(R.id.rbAlarm).isChecked) {
                alartTypeSelected = "alarm"
            }

            sharedPreferencesEditor.putString("alarm_type_selected", alartTypeSelected)

            sharedPreferencesEditor.apply()

            alertDialog.dismiss()
        }

    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Log.i(TAG, "onViewStateRestored: ")

        if (sharedPreferences.getString("start_date", "")!!
                .isNotEmpty() || sharedPreferences.getString("end_date", "")!!
                .isNotEmpty() || sharedPreferences.getString("alarm_type_selected", "")!!
                .isNotEmpty() || sharedPreferences.getString("ALERT_ADDRESS", "")!!.isNotEmpty()
        ) {
            showAddAlertDialog()
            if (sharedPreferences.getString("start_date", "")!!.isNotEmpty()) {
            //    dialogView.findViewById<TextView>(R.id.tv_start_date).text =
                    sharedPreferences.getString("start_date", "")
            }
            if (sharedPreferences.getString("end_date", "")!!.isNotEmpty()) {
              //  dialogView.findViewById<TextView>(R.id.tv_end_date).text = sharedPreferences.getString("end_date", "")
            }
            if (sharedPreferences.getString("ALERT_ADDRESS", "")!!.isNotEmpty()) {
                dialogView.findViewById<TextView>(R.id.tv_location).text =
                    sharedPreferences.getString("ALERT_ADDRESS", "")
            }
            if (sharedPreferences.getString("alarm_type_selected", "") == "notification") {
                dialogView.findViewById<RadioButton>(R.id.rbNotification).isChecked = true
            } else if (sharedPreferences.getString("alarm_type_selected", "") == "alarm") {
                dialogView.findViewById<RadioButton>(R.id.rbAlarm).isChecked = true
            }



            sharedPreferencesEditor.putString("start_date", "")
            sharedPreferencesEditor.putString("end_date", "")
            sharedPreferencesEditor.putString("alarm_type_selected", "")
            sharedPreferencesEditor.putString("ALERT_ADDRESS", "")

            sharedPreferencesEditor.apply()


        }


    }

    override fun onRemoveBtnClick(alert: AlertData) {
       viewModel.removeAlertInVM(alert)
    }


}