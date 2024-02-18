package com.example.tourism_app

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val database = Firebase.database.reference

        //Switching from register to login
        val switchToRegister = findViewById<Button>(R.id.registerButtonL)
        switchToRegister.setOnClickListener {
            // Start the new activity
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.loginButtonL)
        loginButton.setOnClickListener {
            val usernameEntered = findViewById<EditText>(R.id.usernameTextL).text.toString()
            val enteredPassword = findViewById<EditText>(R.id.passwordTextL).text.toString()
            val clientsRef = database.child("Client")
            clientsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isUserFound = false
                    for (clientSnapshot in dataSnapshot.children) {
                        val mailFromDB = clientSnapshot.child("mail").getValue()
                        val passwordFromDB = clientSnapshot.child("passwd").getValue()
                        val usernameFromDB = clientSnapshot.child("pseudo").getValue()

                        val mailFromDBString = mailFromDB?.toString()
                        val passwordFromDBString = passwordFromDB?.toString()
                        val usernameFromDBString = usernameFromDB?.toString()

                        if ((usernameEntered == usernameFromDBString ||usernameEntered == mailFromDBString) && enteredPassword == passwordFromDBString) {
                            isUserFound = true
                            break // Exit the loop once user is found
                        }
                    }
                    if (isUserFound) {
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, MainActivity2::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Wrong password or username", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    Toast.makeText(applicationContext, "ERROR DATA FETCH", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}