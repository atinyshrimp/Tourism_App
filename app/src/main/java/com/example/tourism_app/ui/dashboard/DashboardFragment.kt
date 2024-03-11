package com.example.tourism_app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tourism_app.R
import com.example.tourism_app.databinding.FragmentDashboardBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


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

        //move the camera
        val paris = LatLng(48.85167037963867, 2.3421103858947756)
        val zoom = 12f

        val markerView = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.marker_layout, null)
        val cardview= markerView.findViewById<CardView>(R.id.marker_or)
        val bmp=Bitmap.createScaledBitmap(viewToBmp(cardview)!!, cardview.width, cardview.height, false)
        val iconOk=BitmapDescriptorFactory.fromBitmap(bmp)

        googleMap.addMarker(MarkerOptions().position(paris).icon(iconOk))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(paris, zoom),
            2000,
            object : CancelableCallback {
                override fun onFinish() {}
                override fun onCancel() {}
            })
    }

    private fun viewToBmp(view: View): Bitmap?{
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap= Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        view.layout(0,0,view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }

}