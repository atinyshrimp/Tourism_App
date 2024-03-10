package com.example.tourism_app

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tourism_app.data.Activity
import com.example.tourism_app.databinding.DetailsActivityBinding
import com.example.tourism_app.ui.conditions.ConditionsFragment
import com.example.tourism_app.ui.history.HistoryFragment
import com.example.tourism_app.ui.overview.OverviewFragment
import com.example.tourism_app.ui.transport.TransportFragment
import com.google.android.material.tabs.TabLayout
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
        username = intent.getStringExtra("username").toString()
        setupPage()

        // default fragment is "Overview"
        replaceFragment(OverviewFragment(currentActivity))

        // setting up the back button
        binding.btnBack.setOnClickListener {
            finish()
        }

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

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    private fun setButtonIcons() {
        val likeBtn = binding.ibLikeBtn
        val visitBtn: Button = binding.visitBtn
        DatabaseManager.isActivityLiked(username, currentActivity.name!!) {isLiked ->
            if (isLiked) {
                likeBtn.setImageResource(R.drawable.ic_heart_filled_black_24dp)
                visitBtn.visibility = View.VISIBLE
                DatabaseManager.isActivityVisited(username, currentActivity.name!!) {isVisited ->
                    if (!isVisited) {
                        visitBtn.text = resources.getText(R.string.landmarkBtn)
                        visitBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gold))
                        visitBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(this, R.drawable.ic_eye_black_24dp), null)
                    } else {
                        visitBtn.text = resources.getText(R.string.visitedLandmark)
                        visitBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.half_gold))
                        visitBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(this, R.drawable.ic_low_vision_black_24dp), null)
                    }
                }
            } else {
                likeBtn.setImageResource(R.drawable.ic_heart_black_24dp)
                visitBtn.visibility = View.GONE
            }
        }
        likeBtn.invalidate()
        visitBtn.invalidate()
    }

    @SuppressLint("SetTextI18n")
    private fun setupPage() {
        binding.tvActName.text = currentActivity.name
        binding.tvActCategory.text = currentActivity.category
        binding.tvActLocation.text = "Paris ${currentActivity.getArrondissement()}"
        setButtonIcons()
        setActivityImage()

        binding.ibLikeBtn.setOnClickListener{
            handleLikeButtonClick()
        }

        binding.visitBtn.setOnClickListener{
            handleVisitButtonClick()
        }
    }

    private fun setActivityImage() {
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
    }

    private fun handleLikeButtonClick() {
        DatabaseManager.updateLikedActivity(username, currentActivity.name!!, applicationContext, ::setButtonIcons)
    }

    private fun handleVisitButtonClick() {
        DatabaseManager.updateVisitedActivity(username, currentActivity.name!!, applicationContext, ::setButtonIcons)
    }
}