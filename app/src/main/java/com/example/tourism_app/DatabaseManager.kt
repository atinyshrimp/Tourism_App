package com.example.tourism_app

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        fun isActivityVisited(username: String, activityName: String, callback: (Boolean) -> Unit) {
            val databaseReference: DatabaseReference =
                this.databaseReference.child("Saved_lieu").child(username)

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isVisited = false

                    for (userSnapshot in dataSnapshot.children) {
                        val nameFromDB = userSnapshot.child("name").getValue(String::class.java)
                        val visited = userSnapshot.child("visited").getValue(Int::class.java)

                        if (activityName == nameFromDB && visited == 1) {
                            isVisited = true
                            break
                        }
                    }

                    callback.invoke(isVisited)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error if needed
                }
            })
        }

        fun getVisitCount(activityName: String): Int {
            return 0
        }

        fun updateLikedActivity(username: String, activityName: String, applicationContext: Context, method: () -> Unit) {
            //we need the pseudo to save it at the right place
            //if already saved then delete it otherwise create new element in bdd
            var idDelete = ""
            //in the branch of the user :
            val savedlieuref = databaseReference.child("Saved_lieu").child(username)
            savedlieuref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isLieuLiked = false
                    for (userSnapshot in dataSnapshot.children) {
                        //we should be in the user section, looking at all the elements
                        val nameFromDB = userSnapshot.child("name").value
                        //we need to have the name of the lieu to compare : ex : Lieu1 instead of Colonne...
                        if(activityName == nameFromDB){
                            isLieuLiked = true
                            idDelete = userSnapshot.key.toString()
                            break
                        }
                    }
                    if (isLieuLiked) {
                        //we delete the element
                        val deleteLieu = databaseReference.child("Saved_lieu").child(username).child(idDelete)
                        val deleteTask = deleteLieu.removeValue()
                        deleteTask.addOnSuccessListener {
                            Toast.makeText(applicationContext,"Place removed from favorites", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(applicationContext,"Removing failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        //we create liked lieu element
                        val newUserRef = savedlieuref.push()
                        newUserRef.child("name").setValue(activityName)
                        newUserRef.child("visited").setValue(0)
                        Toast.makeText(applicationContext,"Place added to favorites", Toast.LENGTH_SHORT).show()
                    }

                    method()

                }
                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }

        fun updateVisitedActivity(username: String, activityName: String, applicationContext: Context, method: () -> Unit) {
            //need to determine whether it has already been visited (Saved_Lieu child : "visited" set to 1)
            var idVisit = ""

            //in the branch of the user :
            val savedLieuRef = databaseReference.child("Saved_lieu").child(username)
            savedLieuRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isLieuVisited = false
                    for (userSnapshot in dataSnapshot.children) {
                        //we should be in the user section, looking at all the elements
                        val nameFromDB = userSnapshot.child("name").value
                        //we need to have the name of the lieu to compare : ex : Lieu1 instead of Colonne...
                        if (activityName == nameFromDB){
                            idVisit = userSnapshot.key.toString()
                            val visited = userSnapshot.child("visited").value.toString()
                            if (Integer.valueOf(visited) ==1) {
                                isLieuVisited = true
                            }
                            break
                        }
                    }
                    if (isLieuVisited) {
                        //we put it to not visited by setting "visited" to 0
                        val unvisitLieu = databaseReference.child("Saved_lieu").child(username).child(idVisit)
                        Log.i(ContentValues.TAG, unvisitLieu.toString())
                        val unvisitTask = unvisitLieu.child("visited").setValue(0)
                        val deleteTask = unvisitLieu.child("date").removeValue()
                        unvisitTask.addOnSuccessListener {
                            Toast.makeText(applicationContext,"Place removed from visited", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(applicationContext,"Removing from visited failed", Toast.LENGTH_SHORT).show()
                        }


                        //we remove one visit from this place
                        //in "Lieu", the attribute "nbVisit" need to be nbVisit -1
                        var idPlace = ""
                        val LieuRef = databaseReference.child("Lieu")
                        //looping through this Lieu until we find one with the correct name
                        LieuRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (userSnapshot in dataSnapshot.children) {
                                    //we should be in the user section, looking at all the elements
                                    val nameFromDB = userSnapshot.child("name").value
                                    //we need to have the name of the lieu to compare : ex : Lieu1 instead of Colonne...
                                    if (activityName == nameFromDB) {
                                        idPlace = userSnapshot.key.toString()
                                        break
                                    }
                                }
                                val lieu = databaseReference.child("Lieu").child(idPlace)
                                val nbVisit = dataSnapshot.child(idPlace).child("nbVisit").value.toString()
                                val nbVisitUpdated = Integer.valueOf(nbVisit) -1
                                val minusOneTask = lieu.child("nbVisit").setValue(nbVisitUpdated)
                                minusOneTask.addOnSuccessListener {
                                    Toast.makeText(applicationContext,"Visit removed from counter", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener{
                                    Toast.makeText(applicationContext,"Removing from counter failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                    }
                    else {
                        //we set the value of "visited" to 1 to indicate that it has been visited
                        val visitLieu = databaseReference.child("Saved_lieu").child(username).child(idVisit)
                        //Log.i(TAG, visitLieu.toString()) in Samsam but not in idVisit
                        val visitTask = visitLieu.child("visited").setValue(1)
                        //on ajoute un attribut avec la date de la visite
                        val calendar = Calendar.getInstance()
                        val simpleDateFormat = SimpleDateFormat("dd/MM/YY", Locale.getDefault())
                        val dateTask = visitLieu.child("date").setValue(simpleDateFormat.format(calendar.time))

                        //Toast.makeText(applicationContext,"Place added to visited", Toast.LENGTH_SHORT).show()

                        //we add one to "nbVisit" in "Lieu"
                        var idPlace = ""
                        val LieuRef = databaseReference.child("Lieu")
                        //looping through this Lieu until we find one with the correct name
                        LieuRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (userSnapshot in dataSnapshot.children) {
                                    //we should be in the user section, looking at all the elements
                                    val nameFromDB = userSnapshot.child("name").value
                                    //we need to have the name of the lieu to compare : ex : Lieu1 instead of Colonne...
                                    if (activityName == nameFromDB) {
                                        idPlace = userSnapshot.key.toString()
                                        break
                                    }
                                }
                                val lieu = databaseReference.child("Lieu").child(idPlace)
                                val nbVisit = dataSnapshot.child(idPlace).child("nbVisit").value.toString()
                                val nbVisitUpdated = Integer.valueOf(nbVisit) +1
                                val minusOneTask = lieu.child("nbVisit").setValue(nbVisitUpdated)
                                minusOneTask.addOnSuccessListener {
                                    Toast.makeText(applicationContext,"Visit has been added to counter", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener{
                                    Toast.makeText(applicationContext,"Adding to counter failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })

                    }
                    method()
                }
                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }
}