package com.example.tourism_app.ui.transport

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourism_app.data.Activity
import com.example.tourism_app.data.details.transports.BusAdapter
import com.example.tourism_app.data.details.transports.RERAdapter
import com.example.tourism_app.data.details.transports.SubwayAdapter
import com.example.tourism_app.data.details.transports.TrainAdapter
import com.example.tourism_app.databinding.FragmentTransportBinding

class TransportViewModel: ViewModel() {
    private lateinit var binding: FragmentTransportBinding
    private lateinit var currentActivity: Activity

    fun setupModel(binding: FragmentTransportBinding, activity: Activity) {
        this.binding = binding
        currentActivity = activity

        setupViews()
    }

    private fun setupViews() {
        // setting the activity's URL
        currentActivity.makeTextViewClickable(binding.tvUrl, binding.root.context)

        // Check and set visibility based on transport values
        if (currentActivity.transport?.bus.isNullOrEmpty()) {
            binding.tvBuses.visibility = View.GONE
            binding.rvBuses.visibility = View.GONE
        } else {
            setupBusView()
        }

        if (currentActivity.transport?.rer.isNullOrEmpty()) {
            binding.tvRER.visibility = View.GONE
            binding.rvRER.visibility = View.GONE
        } else {
            setupRERView()
        }

        if (currentActivity.transport?.metro.isNullOrEmpty()) {
            binding.tvSubways.visibility = View.GONE
            binding.rvSubways.visibility = View.GONE
        } else {
            setupSubwayView()
        }

        if (currentActivity.transport?.train.isNullOrEmpty()) {
            binding.tvTrains.visibility = View.GONE
            binding.rvTrains.visibility = View.GONE
        }
        else {
            setupTrainView()
        }
    }

    private fun setupBusView() {
        val recyclerView = binding.rvBuses
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.HORIZONTAL, false)

        // Set the adapter for the RecyclerView
        recyclerView.adapter = BusAdapter(currentActivity.getBuses())
    }

    private fun setupRERView() {
        val recyclerView = binding.rvRER
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.HORIZONTAL, false)

        // Set the adapter for the RecyclerView
        recyclerView.adapter = RERAdapter(currentActivity.getRERs())
    }

    private fun setupSubwayView() {
        val recyclerView = binding.rvSubways
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.VERTICAL, false)

        // Set the adapter for the RecyclerView
        recyclerView.adapter = SubwayAdapter(currentActivity.getSubways())
    }

    private fun setupTrainView() {
        val recyclerView = binding.rvTrains
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = TrainAdapter(currentActivity.getTrains())
    }
}