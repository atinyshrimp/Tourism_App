package com.example.tourism_app.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.FragmentOverviewBinding

class OverviewFragment(private val activity: Activity): Fragment() {

    private var _binding: FragmentOverviewBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val overviewViewModel =
            ViewModelProvider(this)[OverviewViewModel::class.java]

        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        overviewViewModel.setupModel(binding, activity)
        overviewViewModel.setupViews(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}