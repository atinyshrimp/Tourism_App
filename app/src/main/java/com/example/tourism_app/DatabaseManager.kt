package com.example.tourism_app

import com.google.firebase.database.*

class DatabaseManager {
    companion object {
        // Assume you have a reference to your Firebase database
        private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

        // Static method to check if the activity is liked/saved by the user
        fun isActivityLiked(username: String, activityName: String, callback: (Boolean) -> Unit) {
            val userReference = databaseReference.child("Saved_lieu").child(username)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isLiked = false

                    for (userSnapshot in dataSnapshot.children) {
                        val nameFromDB = userSnapshot.child("name").getValue(String::class.java)

                        // Check if the activity name matches
                        if (activityName == nameFromDB) {
                            isLiked = true
                            break
                        }
                    }

                    // Callback with the result
                    callback(isLiked)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error if needed
                }
            })
        }
    }
}