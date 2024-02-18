package com.example.tourism_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tourism_app.databinding.ActivityMain2Binding
import com.example.tourism_app.ui.dashboard.DashboardFragment
import com.example.tourism_app.ui.home.HomeFragment
import com.example.tourism_app.ui.notifications.NotificationsFragment
import com.example.tourism_app.ui.profile.ProfileFragment
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // default fragment is Home
        replaceFragment(HomeFragment())

        binding.navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_dashboard -> replaceFragment(DashboardFragment())
                R.id.navigation_notifications -> replaceFragment(NotificationsFragment())
                R.id.navigation_profile -> replaceFragment(ProfileFragment())

                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main2, fragment)
        fragmentTransaction.commit()
    }
}