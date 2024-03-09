package com.example.tourism_app.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tourism_app.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {


    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[NotificationsViewModel::class.java]


        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        notificationsViewModel.setupViews(binding, this)

        val b = arguments
        val pseudo = b?.getString("pseudo", "null")
        binding.username.text = pseudo


        return root
    }

    companion object {
        fun newInstance(pseudo: String): NotificationsFragment {
            val f = NotificationsFragment()
            // Pass index input as an argument.
            val b = Bundle()
            b.putString("pseudo", pseudo)
            f.arguments = b
            return f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}