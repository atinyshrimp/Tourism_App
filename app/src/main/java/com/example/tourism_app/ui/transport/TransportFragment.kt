package com.example.tourism_app.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.FragmentTransportBinding

class TransportFragment(
    private val activity: Activity
): Fragment() {
    private var _binding: FragmentTransportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val transportViewModel =
            ViewModelProvider(this)[TransportViewModel::class.java]

        _binding = FragmentTransportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        transportViewModel.setupModel(binding, activity)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}