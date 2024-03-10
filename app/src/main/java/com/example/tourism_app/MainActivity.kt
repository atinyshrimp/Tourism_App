package com.example.tourism_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tourism_app.databinding.ActivityMain2Binding
import com.example.tourism_app.ui.dashboard.DashboardFragment
import com.example.tourism_app.ui.home.HomeFragment
import com.example.tourism_app.ui.notifications.NotificationsFragment
import com.example.tourism_app.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    lateinit var navbarView: BottomNavigationView
    private lateinit var pseudo : String
    private lateinit var mail : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the pseudo and mail values from the intent
        pseudo = intent.getStringExtra("pseudo").toString()
        mail = intent.getStringExtra("mail").toString()

        // Default fragment is Home
        replaceFragment(HomeFragment(pseudo))

        navbarView = binding.navView
        navbarView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment(pseudo))
                R.id.navigation_dashboard -> replaceFragment(DashboardFragment(pseudo))
                R.id.navigation_notifications -> {
                    val notificationsFragment = NotificationsFragment.newInstance(pseudo)
                    replaceFragment(notificationsFragment)
                }
                R.id.navigation_profile -> {
                    val profileFragment = ProfileFragment.newInstance(pseudo, mail) // Pass both pseudo and mail
                    replaceFragment(profileFragment)
                }
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