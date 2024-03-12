package com.example.tourism_app.ui.notifications

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourism_app.DatabaseManager
import com.example.tourism_app.DetailsActivity
import com.example.tourism_app.MainActivity
import com.example.tourism_app.R
import com.example.tourism_app.data.Activity
import com.example.tourism_app.data.ActivityRecyclerAdapter
import com.example.tourism_app.data.VisitedActivityAdapter
import com.example.tourism_app.databinding.FragmentNotificationsBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NotificationsViewModel : ViewModel(), ActivityRecyclerAdapter.ActivityRecyclerEvent, ActivityRecyclerAdapter.LikeButtonClickListener,
    VisitedActivityAdapter.ActivityRecyclerEvent, VisitedActivityAdapter.LikeButtonClickListener  {
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var activityList: ArrayList<Activity>
    private lateinit var visitedList: ArrayList<Activity>
    private lateinit var fragment: NotificationsFragment
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var database3: DatabaseReference
    private lateinit var username: String
    private lateinit var adapter: ActivityRecyclerAdapter
    private lateinit var visitAdapter: VisitedActivityAdapter

    override fun onItemClick(position: Int) {
        val activity = activityList[position]
        openDetailsActivity(activity)
    }

    override fun onLikeButtonClicked(position: Int, currentItem: Activity) {
        DatabaseManager.updateLikedActivity(username, currentItem.name!!, fragment.requireContext())
        { updateRecyclerViews(position) }
    }


    fun setupViews(binding: FragmentNotificationsBinding, notificationsFragment: NotificationsFragment, pseudo : String) {
        fragment = notificationsFragment
        this.binding = binding
        username = pseudo

        binding.username.text = username

        // getting values for the Activity recycler view
        val activityRecyclerView = binding.activityList
        activityRecyclerView.layoutManager = LinearLayoutManager(fragment.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        // initializing the list of activities
        activityList = arrayListOf()
        getActivityData()

        // getting values for the visit recycler view
        val visitRecyclerView = binding.visitedList
        visitRecyclerView.layoutManager = LinearLayoutManager(fragment.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        // initializing the list of categories
        visitedList = arrayListOf()
        readVisitData()


        // make the user pic lead to Profile Fragment
        setupUserPicInteractivity()

    }
    private fun readVisitData() {
        //we need to know the name of the profile first to be in the right subsection
        //it is the parameter pseudo

        //we will first get the "Saved_lieu" from firebase, then in lieu we get the full info on those
        //we will retrieve data from the Saved_lieu part of the database and specifically for our current user (using pseudo)

        val visitRecyclerView = binding.visitedList
        database3 = FirebaseDatabase.getInstance().getReference("Saved_lieu").child(username)
        database3.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){

                if(snapshot.exists()){
                    //here we get the place names in order to then retrieve all their info from "Lieu"
                    visitedList.clear()
                    for (clientSnapshot in snapshot.children) {
                        //check if has been visited
                        val visit = clientSnapshot.child("visited").value
                        if(visit != null && Integer.valueOf(visit.toString())==1){
                            //we get the name of one of the favorite place
                            val nameFromDB = clientSnapshot.child("name").value
                            //we need to locate it in the "Lieu" part of the database now to retrieve all the data

                            //set reference in the correct place : in "Lieu"
                            database = FirebaseDatabase.getInstance().getReference("Lieu")
                            database.addValueEventListener(object : ValueEventListener{
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onDataChange(snapshot: DataSnapshot){
                                    //we will look at every children of Lieu to see if it is the Lieu with the correct name
                                    for (userSnapshot in snapshot.children) {
                                        val usernameFromDB = userSnapshot.child("name").getValue(String::class.java)
                                        if(usernameFromDB == nameFromDB){
                                            val visit = userSnapshot.getValue(Activity::class.java)
                                            visitedList.add(visit!!)
                                            visitRecyclerView.adapter?.notifyDataSetChanged()
                                        }
                                    }

                                    visitAdapter = VisitedActivityAdapter(visitedList, this@NotificationsViewModel, username, this@NotificationsViewModel)
                                    visitRecyclerView.adapter = visitAdapter
                                }
                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getActivityData() {
        readData()
    }

    private fun readData() {
        //we need to know the name of the profile first to be in the right subsection
        //it is the parameter pseudo

        //we will first get the "Saved_lieu" from firebase, then in lieu we get the full info on those
        //we will retrieve data from the Saved_lieu part of the database and specifically for our current user (using pseudo)

        val activityRecyclerView = binding.activityList
        database2 = FirebaseDatabase.getInstance().getReference("Saved_lieu").child(username)
        database2.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()){
                    //here we get the place names in order to then retrieve all their info from "Lieu"
                    activityList.clear()
                    for (clientSnapshot in snapshot.children) {
                        //we get the name of one of the favorite place
                        val nameFromDB = clientSnapshot.child("name").value
                        //we need to locate it in the "Lieu" part of the database now to retrieve all the data

                        //set reference in the correct place : in "Lieu"
                        database = FirebaseDatabase.getInstance().getReference("Lieu")
                        database.addValueEventListener(object : ValueEventListener{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onDataChange(snapshot: DataSnapshot){
                                //we will look at every children of Lieu to see if it is the Lieu with the correct name
                                for (userSnapshot in snapshot.children) {
                                    val usernameFromDB = userSnapshot.child("name").getValue(String::class.java)
                                    if (usernameFromDB == nameFromDB){
                                        val activity = userSnapshot.getValue(Activity::class.java)
                                        activityList.add(activity!!)
                                        activityRecyclerView.adapter?.notifyDataSetChanged()
                                    }
                                }

                                adapter = ActivityRecyclerAdapter(activityList, this@NotificationsViewModel, username, this@NotificationsViewModel)
                                activityRecyclerView.adapter = adapter
                            }
                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

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

    private fun updateRecyclerViews(position: Int) {
        // Remove the unliked item from the dataset
        val unlikedActivity = activityList.removeAt(position)

        // Notify the adapter that an item has been removed
        adapter.notifyItemRemoved(position)

        // Update the visitedList as well
        updateVisitedList(unlikedActivity.name!!)
    }

    private fun updateVisitedList(activityName: String) {
        val iterator = visitedList.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (activity.name == activityName) {
                iterator.remove()
            }
        }
        // Notify the adapter that the dataset has changed
        visitAdapter.notifyDataSetChanged()
    }


}