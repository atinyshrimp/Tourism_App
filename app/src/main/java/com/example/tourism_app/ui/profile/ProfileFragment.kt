package com.example.tourism_app.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tourism_app.R
import com.example.tourism_app.databinding.FragmentNotificationsBinding
import com.example.tourism_app.databinding.FragmentProfileBinding
import com.example.tourism_app.ui.notifications.NotificationsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var usernameTextView: TextView
    private lateinit var mailTextView: TextView
    private lateinit var pseudo: String
    private lateinit var mail: String
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pseudo = arguments?.getString("pseudo") ?: ""
        val mail = arguments?.getString("mail") ?: ""

        usernameTextView = view.findViewById<TextView>(R.id.username_txt)
        mailTextView = view.findViewById<TextView>(R.id.mail_txt)

        usernameTextView.text = "Username : "+pseudo
        mailTextView.text = "Mail : "+mail

        binding.changeUserBtn.setOnClickListener {
            toggleInputVisibility(true)
        }
        binding.changeMailBtn.setOnClickListener {
            toggleInputVisibility(false)}

    }
    private fun toggleInputVisibility(isPseudo: Boolean) {
        val editText = if (isPseudo) binding.inputUsername else binding.inputMail
        val newVisibility = if (editText.visibility == View.VISIBLE) {
            if (isPseudo) {
                updatePseudoInDatabase(editText.text.toString())
            } else {
                updateMailInDatabase(editText.text.toString())
            }
            View.GONE
        } else {
            View.VISIBLE
        }
        editText.visibility = newVisibility
    }


    private fun updatePseudoInDatabase(newPseudo: String) {
        val oldPseudo = usernameTextView.text.toString().substringAfter(": ").trim() // Extract old pseudo from TextView
        val clientRef = FirebaseDatabase.getInstance().getReference("Client")
        clientRef.orderByChild("pseudo").equalTo(oldPseudo).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (clientSnapshot in dataSnapshot.children) {
                        clientSnapshot.ref.child("pseudo").setValue(newPseudo)
                            .addOnSuccessListener {
                                usernameTextView.text = "User : $newPseudo"
                            }
                            .addOnFailureListener {
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }
    @SuppressLint("SuspiciousIndentation")
    private fun updateMailInDatabase(newMail: String) {
        val oldMail = mailTextView.text.toString().substringAfter(": ").trim() // Extract old mail from TextView
        val userId = FirebaseAuth.getInstance().currentUser?.uid // Get the current user's ID
        val clientRef = FirebaseDatabase.getInstance().getReference("Client")
            clientRef.orderByChild("mail").equalTo(oldMail).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (clientSnapshot in dataSnapshot.children) {
                        clientSnapshot.ref.child("mail").setValue(newMail)
                            .addOnSuccessListener {
                                mailTextView.text = "Mail : $newMail"
                            }
                            .addOnFailureListener {
                            }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }



    companion object {
        fun newInstance(pseudo: String, mail : String): ProfileFragment {
            val f = ProfileFragment()
            val b = Bundle()
            b.putString("pseudo", pseudo)
            b.putString("mail", mail)
            f.arguments = b
            return f
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}