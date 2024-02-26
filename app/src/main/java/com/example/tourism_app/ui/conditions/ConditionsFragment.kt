package com.example.tourism_app.ui.conditions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.FragmentConditionsBinding

class ConditionsFragment(
    private val activity: Activity
) : Fragment() {
    private var _binding: FragmentConditionsBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val conditionsViewModel =
            ViewModelProvider(this)[ConditionsViewModel::class.java]

        _binding = FragmentConditionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        conditionsViewModel.setupModel(binding, activity)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}