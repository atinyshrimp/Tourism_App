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

    fun setupModel(binding: FragmentConditionsBinding, activity: Activity) {
        currentActivity = activity
        this.binding = binding

        setupViews()
    }

    private fun setupViews() {
        // initializing list of conditions
        val recyclerView = binding.rvConditions
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.VERTICAL, false)
        getConditions(recyclerView)

        // setting the activity's URL
        currentActivity.makeTextViewClickable(binding.textView2, binding.root.context)
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