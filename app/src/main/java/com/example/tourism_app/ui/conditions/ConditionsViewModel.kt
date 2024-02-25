package com.example.tourism_app.ui.conditions

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.data.Activity
import com.example.tourism_app.data.details.ConditionsAdapter
import com.example.tourism_app.databinding.FragmentConditionsBinding

class ConditionsViewModel : ViewModel() {
    private lateinit var currentActivity: Activity
    private lateinit var binding: FragmentConditionsBinding
    private lateinit var fragment: ConditionsFragment

    fun setupModel(binding: FragmentConditionsBinding, activity: Activity, fragment: ConditionsFragment) {
        currentActivity = activity
        this.binding = binding
        this.fragment = fragment

        setupViews()
    }

    private fun setupViews() {
        // initializing list of conditions
        val recyclerView = binding.rvConditions
        recyclerView.layoutManager = LinearLayoutManager(fragment.context,
            LinearLayoutManager.VERTICAL, false)
        getConditions(recyclerView)
    }

    private fun getListFromString(): ArrayList<String> {
        return if (currentActivity.condition_free?.contains("and") == true) {
            currentActivity.condition_free?.split(" and ") as ArrayList<String>
        } else {
            val list: ArrayList<String> = arrayListOf()
            list.add(currentActivity.condition_free!!)
            list
        }
    }

    private fun getConditions(recyclerView: RecyclerView) {
        val conditionList = getListFromString()

        recyclerView.adapter = ConditionsAdapter(conditionList)
    }
}