package com.example.tourism_app.ui.notifications

import android.content.Intent
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


class NotificationsViewModel : ViewModel(), ActivityRecyclerAdapter.ActivityRecyclerEvent, ActivityRecyclerAdapter.LikeButtonClickListener  {
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var activityList: ArrayList<Activity>
    private lateinit var fragment: NotificationsFragment
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var username: String
    private lateinit var name_lieu: String
    private lateinit var adapter: ActivityRecyclerAdapter

    override fun onItemClick(position: Int) {
        val activity = activityList[position]
        openDetailsActivity(activity)
    }

    override fun onLikeButtonClicked(position: Int, currentItem: Activity) {
        //we need the pseudo to save it at the right place
        //if already saved then delete it otherwise create new element in bdd
        val database = FirebaseDatabase.getInstance().reference
        //in the branch of the user :
        var idDelete = ""
        val savedlieuref = database.child("Saved_lieu").child(username)
        savedlieuref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isLieuLiked = false
                for (userSnapshot in dataSnapshot.children) {
                    //we should be in the user section, looking at all the elements
                    val nameFromDB = userSnapshot.child("name").value
                    //we need to have the name of the lieu to compare : ex : Lieu1 instead of Colonne...
                    if(currentItem.name == nameFromDB){
                        isLieuLiked = true
                        idDelete = userSnapshot.key.toString()
                        break
                    }
                }
                if(isLieuLiked) {
                    //we delete the element
                    val deleteLieu = database.child("Saved_lieu").child(username).child(idDelete)
                    val deleteTask = deleteLieu.removeValue()
                }
                else {
                    //we create liked lieu element
                    val newUserRef = savedlieuref.push()
                    newUserRef.child("name").setValue(currentItem.name)
                    newUserRef.child("visited").setValue(0)
                }
                adapter.notifyItemChanged(position)

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    fun setupViews(binding: FragmentNotificationsBinding, notificationsFragment: NotificationsFragment, pseudo : String) {
        fragment = notificationsFragment
        this.binding = binding
        username = pseudo


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

        val activityRecyclerView = binding.activityList
        database2 = FirebaseDatabase.getInstance().getReference("Saved_lieu").child(pseudo)
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
                            override fun onDataChange(snapshot: DataSnapshot){
                                //we will look at every children of Lieu to see if it is the Lieu with the correct name
                                for (userSnapshot in snapshot.children) {
                                    val usernameFromDB = userSnapshot.child("name").getValue(String::class.java)
                                    if(usernameFromDB == nameFromDB){
                                        val activity = userSnapshot.getValue(Activity::class.java)
                                        activityList.add(activity!!)
                                        activityRecyclerView.adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                    adapter = ActivityRecyclerAdapter(activityList, this@NotificationsViewModel, pseudo, this@NotificationsViewModel)
                    activityRecyclerView.adapter = adapter
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