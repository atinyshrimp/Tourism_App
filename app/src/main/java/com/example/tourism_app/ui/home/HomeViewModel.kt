package com.example.tourism_app.ui.home

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.DetailsActivity
import com.example.tourism_app.MainActivity
import com.example.tourism_app.data.Activity
import com.example.tourism_app.data.ActivityRecyclerAdapter
import com.example.tourism_app.data.Category
import com.example.tourism_app.data.CategoryAdapter
import com.example.tourism_app.R
import com.example.tourism_app.data.Hours
import com.example.tourism_app.databinding.FragmentHomeBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeViewModel : ViewModel(), ActivityRecyclerAdapter.ActivityRecyclerEvent {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var activityList: ArrayList<Activity>
    private lateinit var fragment: HomeFragment
    private lateinit var database: DatabaseReference

    override fun onItemClick(position: Int) {
        val activity = activityList[position]
        openDetailsActivity(activity)
    }

    fun setupViews(binding: FragmentHomeBinding, homeFragment: HomeFragment) {
        fragment = homeFragment
        this.binding = binding

        // Access UI components and perform setup
        val tabLayout = binding.activityTabs
        tabLayout.getTabAt(1)?.select()

        // getting values for the Activity recycler view
        val activityRecyclerView = binding.activityList
        activityRecyclerView.layoutManager = LinearLayoutManager(fragment.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        // initializing the list of activities
        activityList = arrayListOf()
        getActivityData(activityList, activityRecyclerView)

        // getting values for the Category recycler view
        val categoryRecyclerView = binding.categoryList
        categoryRecyclerView.layoutManager = LinearLayoutManager(fragment.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        // initializing the list of categories
        val categoryList = arrayListOf<Category>()
        getCategoryData(categoryList, categoryRecyclerView)

        // make the user pic lead to Profile Fragment
        setupUserPicInteractivity()

        // setupTabs(tabLayout)
    }

    private fun setupTabs(tabLayout: TabLayout) {
        // Setup listeners or event handling if needed
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab selection, filter the list accordingly
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselection if needed
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselection if needed
            }
        })
    }

    private fun getActivityData(activityList: ArrayList<Activity>, activityRecyclerView: RecyclerView) {
        readData()
    }

    private fun readData() {
        var bool = true
        val lieu = "Lieu"
        var i = 1
        database = FirebaseDatabase.getInstance().getReference("Lieu")
        while(i<3){
            database.child(lieu.plus(i)).get().addOnSuccessListener {
                if(it.exists()){
                    val name = it.child("name").value
                    val address = it.child("address").value
                    val description = it.child("description").value
                    val condition_free = it.child("condition_free").value
                    val monday = it.child("hours").child("monday").value
                    val tuesday = it.child("hours").child("tuesday").value
                    val wednesday = it.child("hours").child("wednesday").value
                    val thursday = it.child("hours").child("thursday").value
                    val friday = it.child("hours").child("friday").value
                    val saturday = it.child("hours").child("saturday").value
                    val sunday = it.child("hours").child("sunday").value
                    val hours = Hours(monday=monday.toString(), tuesday=tuesday.toString(),wednesday=wednesday.toString(), thursday=thursday.toString(), friday=friday.toString(),saturday=saturday.toString(), sunday=sunday.toString())

                    val category = it.child("category").value
                    val url = it.child("url").value

                    activityList.add(Activity(address=address.toString(),category=category.toString(), condition_free = condition_free.toString(),hours=hours, name=name.toString(),description = description.toString() ,url = url.toString() ))

                }
                else{
                    bool = false
                }

                binding.activityList.adapter = ActivityRecyclerAdapter(activityList, this)
            }.addOnFailureListener{
                bool = false
            }
            i++
        }

    }

    private fun getCategoryData(categoryList: ArrayList<Category>,
                                categoryRecyclerView: RecyclerView) {
        val categories = listOf(
            Category(fragment.resources.getStringArray(R.array.categories)[0], R.drawable.category_aesthetics),
            Category(fragment.resources.getStringArray(R.array.categories)[1], R.drawable.category_event),
            Category(fragment.resources.getStringArray(R.array.categories)[2], R.drawable.category_garden),
            Category(fragment.resources.getStringArray(R.array.categories)[3], R.drawable.category_museum)
        )

        for (element in categories) {
            categoryList.add(element)
        }

        categoryRecyclerView.adapter = CategoryAdapter(categoryList)
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