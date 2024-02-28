package com.example.tourism_app.ui.transport

import androidx.lifecycle.ViewModel
import com.example.tourism_app.data.Activity
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
    }
}