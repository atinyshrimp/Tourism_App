package com.example.tourism_app

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.DetailsActivityBinding
import com.example.tourism_app.ui.conditions.ConditionsFragment
import com.example.tourism_app.ui.history.HistoryFragment
import com.example.tourism_app.ui.overview.OverviewFragment
import com.example.tourism_app.ui.transport.TransportFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class DetailsActivity: AppCompatActivity() {
    private lateinit var binding: DetailsActivityBinding
    private lateinit var currentActivity: Activity
    lateinit var username : String

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

        username = intent.getStringExtra("username").toString()

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

        var imageName = currentActivity.name
        if(imageName != null) {
            imageName = imageName.replace(" ", "")
            val storageRef = FirebaseStorage.getInstance().reference.child("LieuImage/$imageName.jpg")
            val localfile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imageView.setImageBitmap(bitmap)
            }
        }

        binding.imageButton.setOnClickListener{
            //we need the pseudo to save it at the right place
            //if already saved then delete it otherwise create new element in bdd
            val database = FirebaseDatabase.getInstance().reference
            var idDelete = ""
            //in the branch of the user :
            val savedlieuref = database.child("Saved_lieu").child(username)
            savedlieuref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isLieuLiked = false
                    for (userSnapshot in dataSnapshot.children) {
                        //we should be in the user section, looking at all the elements
                        val nameFromDB = userSnapshot.child("name").value
                        //we need to have the name of the lieu to compare : ex : Lieu1 instead of Colonne...
                        if(currentActivity.name==nameFromDB){
                            isLieuLiked = true
                            idDelete = userSnapshot.key.toString()
                            break
                        }
                    }
                    if(isLieuLiked){
                        //we delete the element
                        val deleteLieu = database.child("Saved_lieu").child(username).child(idDelete)
                        val deleteTask = deleteLieu.removeValue()
                        deleteTask.addOnSuccessListener {
                            Toast.makeText(applicationContext,"Place removed from favorites", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(applicationContext,"Removing failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        //we create liked lieu element
                        val newUserRef = savedlieuref.push()
                        newUserRef.child("name").setValue(currentActivity.name)
                        newUserRef.child("visited").setValue(0)
                        Toast.makeText(applicationContext,"Place added to favorites", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}