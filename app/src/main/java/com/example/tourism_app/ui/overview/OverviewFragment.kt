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

class OverviewFragment(activity: Activity): Fragment() {

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

        val textView: TextView = binding.textOverview
        overviewViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}