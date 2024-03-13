package com.example.tourism_app.ui.home

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.DatabaseManager
import com.example.tourism_app.DetailsActivity
import com.example.tourism_app.MainActivity
import com.example.tourism_app.data.Activity
import com.example.tourism_app.data.ActivityRecyclerAdapter
import com.example.tourism_app.data.Category
import com.example.tourism_app.data.CategoryAdapter
import com.example.tourism_app.R
import com.example.tourism_app.databinding.FragmentHomeBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel :
    ViewModel(),
    ActivityRecyclerAdapter.ActivityRecyclerEvent,
    ActivityRecyclerAdapter.LikeButtonClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var activityList: ArrayList<Activity>
    private lateinit var fragment: HomeFragment
    private lateinit var database: DatabaseReference
    lateinit var username: String
    private lateinit var adapter: ActivityRecyclerAdapter

    override fun onItemClick(position: Int) {
        val activity = activityList[position]
        openDetailsActivity(activity)
    }

    override fun onLikeButtonClicked(position: Int, currentItem: Activity) {
        DatabaseManager.updateLikedActivity(username, currentItem.name!!, fragment.requireContext()
        ) { adapter.notifyItemChanged(position) }
    }

    fun setupViews(binding: FragmentHomeBinding, homeFragment: HomeFragment, user: String) {
        fragment = homeFragment
        this.binding = binding

        binding.username.text = username

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
        getActivityData(user)

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

        setupTabs(tabLayout)
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

    private fun getActivityData(user:String) {
        readData(user)
    }

    private fun readData(user:String) {
        val activityRecyclerView = binding.activityList
        database = FirebaseDatabase.getInstance().getReference("Lieu")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()){
                    activityList.clear()
                    for (activitySnapshot in snapshot.children){
                        val activity = activitySnapshot.getValue(Activity::class.java)
                        activityList.add(activity!!)
                    }
                    adapter = ActivityRecyclerAdapter(activityList,
                        this@HomeViewModel, user, this@HomeViewModel)
                    activityRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getCategoryData(categoryList: ArrayList<Category>,
                                categoryRecyclerView: RecyclerView) {
        val categories = listOf(
            Category(fragment.resources.getStringArray(R.array.categories)[0]),
            Category(fragment.resources.getStringArray(R.array.categories)[1]),
            Category(fragment.resources.getStringArray(R.array.categories)[2]),
            Category(fragment.resources.getStringArray(R.array.categories)[3])
        )


        for (element in categories) {
            categoryList.add(element)
        }

        categoryRecyclerView.adapter = CategoryAdapter(categoryList)
    }

    private fun openDetailsActivity(activity: Activity){
        val intent = Intent(fragment.context, DetailsActivity::class.java)
        intent.putExtra("activityKey", activity)
        intent.putExtra("username",username)
        fragment.requireContext().startActivity(intent)
    }

    private fun setupUserPicInteractivity() {
        val userPicBtn: ShapeableImageView = binding.ivUser
        val parentAct: MainActivity = fragment.activity as MainActivity

        userPicBtn.setOnClickListener {
            parentAct.navbarView.selectedItemId = R.id.navigation_profile
        }
    }
}