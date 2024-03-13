package com.example.tourism_app.ui.history

import androidx.lifecycle.ViewModel
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.FragmentHistoryBinding

class HistoryViewModel: ViewModel() {
    private lateinit var currentActivity: Activity
    private lateinit var binding: FragmentHistoryBinding

    fun setupModel(binding: FragmentHistoryBinding, activity: Activity) {
        currentActivity = activity
        this.binding = binding

        setupViews()
    }

    private fun setupViews() {
        binding.tvDescription.text = currentActivity.description

        // setting the activity's URL
        currentActivity.makeTextViewClickable(binding.textView5, binding.root.context)
    }
}