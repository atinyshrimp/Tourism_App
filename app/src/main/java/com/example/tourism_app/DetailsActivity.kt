package com.example.tourism_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.DetailsActivityBinding
import com.example.tourism_app.ui.conditions.ConditionsFragment
import com.example.tourism_app.ui.history.HistoryFragment
import com.example.tourism_app.ui.overview.OverviewFragment
import com.example.tourism_app.ui.transport.TransportFragment
import com.google.android.material.tabs.TabLayout

class DetailsActivity: AppCompatActivity() {
    private lateinit var binding: DetailsActivityBinding
    private lateinit var currentActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // getting the Activity from the RecyclerView item
        currentActivity = intent.getParcelableExtra("activityKey")!!
        setupPage()

        // default fragment is "Overview"
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
                        1 -> replaceFragment(HistoryFragment(currentActivity))
                        2 -> replaceFragment(ConditionsFragment(currentActivity))
                        3 -> replaceFragment(TransportFragment(currentActivity))

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

    @SuppressLint("SetTextI18n")
    private fun setupPage() {
        binding.tvActName.text = currentActivity.name
        binding.tvActCategory.text = currentActivity.category
        binding.tvActLocation.text = "Paris ${currentActivity.getArrondissement()}"
    }
}