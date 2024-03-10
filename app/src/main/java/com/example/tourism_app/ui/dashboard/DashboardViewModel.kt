package com.example.tourism_app.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.DetailsActivity
import com.example.tourism_app.MainActivity
import com.example.tourism_app.data.Activity
import com.example.tourism_app.data.ActivityMapRecycler
import com.example.tourism_app.data.Category
import com.example.tourism_app.data.CategoryAdapter
import com.example.tourism_app.R
import com.example.tourism_app.databinding.FragmentDashboardBinding
import com.example.tourism_app.databinding.FragmentHomeBinding
import com.example.tourism_app.ui.home.HomeFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardViewModel : ViewModel() , ActivityMapRecycler.ActivityRecyclerEvent{

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var activityList: ArrayList<Activity>
    private lateinit var fragment: DashboardFragment
    private lateinit var database: DatabaseReference
    lateinit var username: String

    override fun onItemClick(position: Int) {
        val activity = activityList[position]
        openDetailsActivity(activity)
    }

    fun setupViews(binding: FragmentDashboardBinding, dashboardFragment: DashboardFragment, user: String) {
        fragment = dashboardFragment
        this.binding = binding

        // getting values for the Activity recycler view
        val activityRecyclerView = binding.activityList
        activityRecyclerView.layoutManager = LinearLayoutManager(fragment.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        // initializing the list of activities
        activityList = arrayListOf()
        getActivityData(user)
    }

    private fun getActivityData(user:String) {
        readData(user)
    }

    private fun readData(user:String) {
        val activityRecyclerView = binding.activityList
        database = FirebaseDatabase.getInstance().getReference("Lieu")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()){
                    activityList.clear()
                    for (activitySnapshot in snapshot.children){
                        val activity = activitySnapshot.getValue(Activity::class.java)
                        activityList.add(activity!!)
                    }
                    activityRecyclerView.adapter = ActivityMapRecycler(activityList, this@DashboardViewModel, user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun openDetailsActivity(activity: Activity){
        val intent = Intent(fragment.context, DetailsActivity::class.java)
        intent.putExtra("activityKey", activity)
        intent.putExtra("username",username)
        fragment.context?.startActivity(intent)
    }
}