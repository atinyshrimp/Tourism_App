package com.example.tourism_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}