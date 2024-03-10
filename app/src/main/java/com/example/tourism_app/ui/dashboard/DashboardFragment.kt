package com.example.tourism_app.ui.dashboard

import android.os.Bundle

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tourism_app.R
import com.example.tourism_app.databinding.FragmentDashboardBinding
import com.example.tourism_app.ui.home.HomeViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory


class DashboardFragment (private val user: String) : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        dashboardViewModel.username = user
        dashboardViewModel.setupViews(binding, this, user)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Paris and move the camera
        val paris = LatLng(48.85667037963867, 2.3521103858947754)
        mMap.addMarker(MarkerOptions().position(paris).title("Marker in Paris"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris))
    }
}