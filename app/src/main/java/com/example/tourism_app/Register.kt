package com.example.tourism_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Register : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val switchToLogin = findViewById<Button>(R.id.loginButtonR)
        switchToLogin.setOnClickListener {
            // Start the new activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.registerButtonR)
        loginButton.setOnClickListener {
            val usernameEntered = findViewById<EditText>(R.id.usernameTxtR).text.toString()
            val mailEntered = findViewById<EditText>(R.id.emailTxtR).text.toString()
            val passwordEntered = findViewById<EditText>(R.id.passwordTxtR).text.toString()
            val passwordConfEntered = findViewById<EditText>(R.id.passwordConfTxtR).text.toString()

            // Check if passwords match and meet criteria
            if (passwordEntered != passwordConfEntered) {
                Toast.makeText(applicationContext, "Passwords don't match!", Toast.LENGTH_SHORT).show()
            }
            else if(usernameEntered=="" || mailEntered == "" || passwordEntered == ""){
                Toast.makeText(applicationContext, "Empty fields remaining !", Toast.LENGTH_SHORT).show()
            }
            else if (passwordEntered.length < 4) {
                Toast.makeText(applicationContext, "Password must be at least 4 characters long!", Toast.LENGTH_SHORT).show()
            } else {
                // Check if username and email already exist in the database
                val usersRef = database.child("Client")
                usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var isUsernameTaken = false
                        var isEmailTaken = false

                        for (userSnapshot in dataSnapshot.children) {
                            val usernameFromDB = userSnapshot.child("pseudo").getValue(String::class.java)
                            val emailFromDB = userSnapshot.child("mail").getValue(String::class.java)

                            if (usernameFromDB == usernameEntered) {
                                isUsernameTaken = true
                                break
                            }

                            if (emailFromDB == mailEntered) {
                                isEmailTaken = true
                                break
                            }
                        }

                        if (isUsernameTaken) {
                            Toast.makeText(applicationContext, "Username is already taken!", Toast.LENGTH_SHORT).show()
                        } else if (isEmailTaken) {
                            Toast.makeText(applicationContext, "Email is already registered!", Toast.LENGTH_SHORT).show()
                        } else {
                            // Add new user to the database
                            val newUserRef = usersRef.push()
                            newUserRef.child("pseudo").setValue(usernameEntered)
                            newUserRef.child("mail").setValue(mailEntered)
                            newUserRef.child("passwd").setValue(passwordEntered)
                            Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "ERROR: Unable to register!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

    }
}