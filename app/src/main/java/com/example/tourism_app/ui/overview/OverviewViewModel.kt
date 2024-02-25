package com.example.tourism_app.ui.overview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.tourism_app.R
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.FragmentOverviewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OverviewViewModel: ViewModel() {
    private lateinit var currentActivity: Activity
    private lateinit var binding: FragmentOverviewBinding

    fun setupModel(binding: FragmentOverviewBinding, activity: Activity) {
        currentActivity = activity
        this.binding = binding

        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun displaySchedules() {
        val context = binding.root.context
        val currentDay = getCurrentDay()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.details_card, null)
        val titleTextView: TextView = dialogView.findViewById(R.id.tvActivityName)
        val scheduleTextView: TextView = dialogView.findViewById(R.id.tvSchedules)

        val scheduleText = StringBuilder()

        // Build the schedule text with all days
        currentActivity.hours?.let { hours ->
            for (day in listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")) {
                val schedule = when (day) {
                    "Monday" -> hours.monday
                    "Tuesday" -> hours.tuesday
                    "Wednesday" -> hours.wednesday
                    "Thursday" -> hours.thursday
                    "Friday" -> hours.friday
                    "Saturday" -> hours.saturday
                    "Sunday" -> hours.sunday
                    else -> null
                }

                // Highlight the current day in the schedule
                // Highlight the current day in the schedule
                val formattedDay = if (day == currentDay) {
                    // Apply bold and change the text color for the current day
                    "<font color='${ContextCompat.getColor(context, R.color.gold)}'><b>$day: $schedule</b></font><br> "
                } else {
                    // Leave other days as they are
                    "$day: $schedule<br>"
                }

                scheduleText.append(formattedDay)
            }
        }

        titleTextView.text = "${currentActivity.name}'s\nWeekly schedule:"
        scheduleTextView.text = Html.fromHtml(scheduleText.toString(), Html.FROM_HTML_MODE_LEGACY)

        // Create the AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        builder.setCancelable(true) // Set to true if you want the dialog to be dismissible by clicking outside
        val dialog = builder.create()

        // Show the dialog
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun getCurrentDay(): String {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    private fun setupStatus(){
        val context = binding.root.context

        // Get the current time
        val calendar: Calendar = Calendar.getInstance()
        val currentHour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute: Int = calendar.get(Calendar.MINUTE)
        val status: TextView = binding.tvStatus

        // Convert the current time to a single integer representing minutes since midnight
        val currentTimeInMinutes = currentHour * 60 + currentMinute

        // Parse the time range
        val timeRange = currentActivity.getTodaySchedule()!!
        if (timeRange != "closed") {
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
                status.setTextColor(ContextCompat.getColor(context, R.color.olympic_green))
            } else {
                // Check if the current time is nearer the start
                if (currentTimeInMinutes > endTimeInMinutes - bufferTime && currentTimeInMinutes < endTimeInMinutes) {
                    status.text = "Closes soon"
                    status.setTextColor(ContextCompat.getColor(context, R.color.olympic_yellow))
                }
                else if (currentTimeInMinutes >= startTimeInMinutes - bufferTime && currentTimeInMinutes < startTimeInMinutes) {
                    status.text = "Opens soon"
                    status.setTextColor(ContextCompat.getColor(context, R.color.olympic_yellow))
                }  else {
                    status.text = "Closed"
                    status.setTextColor(ContextCompat.getColor(context, R.color.olympic_pink))
                }
            }
        }

    }

    private fun setupViews() {
        binding.tvAddress.text = currentActivity.address
        val todaySchedule = currentActivity.getTodaySchedule()
        if (todaySchedule != "closed") {
            binding.tvHours.text = todaySchedule
        } else {
            binding.tvHours.text = ""
        }

        // make "Show Details" clickable
        val detailsBtn = binding.tvHourDetails
        detailsBtn.setOnClickListener {
            displaySchedules()
        }

        setupStatus()

        // setting the activity's URL
        val info: TextView = binding.textView8
        currentActivity.makeTextViewClickable(info, binding.root.context)
    }
}