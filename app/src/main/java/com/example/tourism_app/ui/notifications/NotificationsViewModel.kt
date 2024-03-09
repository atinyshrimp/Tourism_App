package com.example.tourism_app.ui.notifications

import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.getIntentOld
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourism_app.DetailsActivity
import com.example.tourism_app.MainActivity
import com.example.tourism_app.R
import com.example.tourism_app.data.Activity
import com.example.tourism_app.data.ActivityRecyclerAdapter
import com.example.tourism_app.databinding.FragmentNotificationsBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NotificationsViewModel : ViewModel(), ActivityRecyclerAdapter.ActivityRecyclerEvent  {
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var activityList: ArrayList<Activity>
    private lateinit var fragment: NotificationsFragment
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var name_lieu: String

    override fun onItemClick(position: Int) {
        val activity = activityList[position]
        openDetailsActivity(activity)
    }

    fun setupViews(binding: FragmentNotificationsBinding, notificationsFragment: NotificationsFragment, pseudo : String) {
        fragment = notificationsFragment
        this.binding = binding


        // getting values for the Activity recycler view
        val activityRecyclerView = binding.activityList
        activityRecyclerView.layoutManager = LinearLayoutManager(fragment.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        // initializing the list of activities
        activityList = arrayListOf()
        getActivityData(pseudo)

        // make the user pic lead to Profile Fragment
        setupUserPicInteractivity()

    }

    private fun getActivityData(pseudo : String) {
        readData(pseudo)
    }

    private fun readData(pseudo : String) {
        //we need to know the name of the profile first to be in the right subsection
        //it is the parameter pseudo

        //we will first get the "Saved_lieu" from firebase, then in lieu we get the full info on those
        //we will retrieve data from the Saved_lieu part of the database and specifically for our current user (using pseudo)
        /*val activityRecyclerView = binding.activityList
        database = FirebaseDatabase.getInstance().getReference("Lieu")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()){

                    activityList.clear()
                    for (activitySnapshot in snapshot.children){
                        val activity = activitySnapshot.getValue(Activity::class.java)
                        activityList.add(activity!!)
                    }
                    activityRecyclerView.adapter = ActivityRecyclerAdapter(activityList, this@NotificationsViewModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })*/



        val activityRecyclerView = binding.activityList
        database2 = FirebaseDatabase.getInstance().getReference("Saved_lieu").child(pseudo)
        database2.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()){
                    //here we get the place names in order to then retrieve all their info from "Lieu"
                    activityList.clear()
                    for (clientSnapshot in snapshot.children) {
                        //we get the name of one of the favorite place
                        val nameFromDB = clientSnapshot.child("name").getValue()
                        //we need to locate it in the "Lieu" part of the database now to retrieve all the data

                        //set reference in the correct place : the correct lieu in "Lieu"
                        database = FirebaseDatabase.getInstance().getReference("Lieu").child(nameFromDB.toString())
                        database.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot){
                                if(snapshot.exists()){
                                    val activity = snapshot.getValue(Activity::class.java)
                                    activityList.add(activity!!)
                                    activityRecyclerView.adapter?.notifyDataSetChanged()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                    activityRecyclerView.adapter = ActivityRecyclerAdapter(activityList, this@NotificationsViewModel)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



    }

/*
    private val _text = MutableLiveData<String>().apply {
        value = "This is like Fragment"
    }
    val text: LiveData<String> = _text */

    private fun openDetailsActivity(activity: Activity){
        val intent = Intent(fragment.context, DetailsActivity::class.java)
        intent.putExtra("activityKey", activity)
        fragment.context?.startActivity(intent)
    }

    private fun setupUserPicInteractivity() {
        val userPicBtn: ShapeableImageView = binding.ivUser
        val parentAct: MainActivity = fragment.activity as MainActivity

        userPicBtn.setOnClickListener {
            parentAct.navbarView.selectedItemId = R.id.navigation_profile
        }
    }

}