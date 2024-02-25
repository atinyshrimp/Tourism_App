package com.example.tourism_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.FirebaseStorageKtxRegistrar
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream

class SavedActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)
        storage = Firebase.storage
        includesForCreateReference()
    }
    private fun  includesForCreateReference(){
        val storage = Firebase.storage
        var storageRef = storage.reference
        var spaceRef = storageRef.child("burren.jpeg")
        val stream = FileInputStream(File("burren.jpeg"))
        var uploadTask = storageRef.putStream(stream)
    }



}