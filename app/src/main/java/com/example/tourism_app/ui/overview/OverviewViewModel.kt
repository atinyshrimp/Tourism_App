package com.example.tourism_app.ui.overview

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.tourism_app.R
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.FragmentOverviewBinding
import java.util.Calendar

class OverviewViewModel: ViewModel() {
    private lateinit var currentActivity: Activity
    private lateinit var binding: FragmentOverviewBinding

    fun setupModel(binding: FragmentOverviewBinding, activity: Activity) {
        currentActivity = activity
        this.binding = binding
    }

    fun setupViews(fragment: OverviewFragment) {
        binding.tvAddress.text = currentActivity.address
        binding.tvHours.text = currentActivity.hours
        setupStatus(fragment)
    }

    private fun setupStatus(fragment: OverviewFragment){
        // Get the current time
        val calendar: Calendar = Calendar.getInstance()
        val currentHour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute: Int = calendar.get(Calendar.MINUTE)
        val status: TextView = binding.tvStatus

        // Convert the current time to a single integer representing minutes since midnight
        val currentTimeInMinutes = currentHour * 60 + currentMinute

        // Parse the time range
        val timeRange = currentActivity.hours!!
        val parts = timeRange.split(" - ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val startTimeStr = parts[0]
        val endTimeStr = parts[1]


        // Parse the start and end times
        val startTimeParts = startTimeStr.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val startHour = startTimeParts[0].toInt()
        val startMinute = startTimeParts[1].toInt()
        val startTimeInMinutes = startHour * 60 + startMinute

        val endTimeParts = endTimeStr.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val endHour = endTimeParts[0].toInt()
        val endMinute = endTimeParts[1].toInt()
        val endTimeInMinutes = endHour * 60 + endMinute

        // Define a buffer time in minutes (adjust as needed)
        val bufferTime = 45

        // Check if the current time is within the time range
        if (currentTimeInMinutes >= startTimeInMinutes &&
            currentTimeInMinutes < endTimeInMinutes - bufferTime
        ) {
            status.text = "Open"
            status.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.olympic_green))
        } else {
            // Check if the current time is nearer the start
            if (currentTimeInMinutes > endTimeInMinutes - bufferTime && currentTimeInMinutes < endTimeInMinutes) {
                status.text = "Closes soon"
                status.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.olympic_yellow))
            }
            else if (currentTimeInMinutes >= startTimeInMinutes - bufferTime && currentTimeInMinutes < startTimeInMinutes) {
                status.text = "Opens soon"
                status.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.olympic_yellow))
            }  else {
                status.text = "Closed"
                status.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.olympic_pink))
            }
        }
    }
}