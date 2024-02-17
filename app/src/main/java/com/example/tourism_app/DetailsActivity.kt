package com.example.tourism_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.DetailsActivityBinding
import com.example.tourism_app.ui.dashboard.DashboardFragment
import com.example.tourism_app.ui.notifications.NotificationsFragment
import com.example.tourism_app.ui.overview.OverviewFragment
import com.example.tourism_app.ui.profile.ProfileFragment
import com.google.android.material.tabs.TabLayout

class DetailsActivity: AppCompatActivity() {
    private lateinit var binding: DetailsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentActivity: Activity? = intent.getParcelableExtra("activityKey")
        setupPage(currentActivity!!)


        replaceFragment(OverviewFragment(currentActivity))

        // setting up the back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when(it.position) {
                        0 -> replaceFragment(OverviewFragment(currentActivity))
                        1 -> replaceFragment(ProfileFragment())
                        2 -> replaceFragment(NotificationsFragment())
                        3 -> replaceFragment(DashboardFragment())

                        else -> {}
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    private fun setupPage(currentActivity: Activity) {
        binding.tvActName.text = currentActivity.name
        binding.tvActCategory.text = currentActivity.category
        binding.tvActLocation.text = "Paris ${currentActivity.getArrondissement()}"
    }
}