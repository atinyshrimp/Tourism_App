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

        //readData()
        // test Activity elements until Firebase liaison
        val activities = listOf(
            Activity(name="Musée du Louvre", address="8 rue Sainte-Anne, 75001 Paris",
                reason = "Experience the Louvre, the world's largest and most visited art museum, nestled in the heart of Paris. Home to over 35,000 works of art spanning from ancient civilizations to the 19th century, this iconic institution showcases the pinnacle of human creativity. Marvel at renowned masterpieces, including Leonardo da Vinci's Mona Lisa, the ancient Greek sculpture Venus de Milo, and the striking Winged Victory of Samothrace. The Louvre's architectural grandeur, from the medieval fortress to the glass pyramid entrance, adds to the allure of this cultural gem. Dive into a rich tapestry of history and artistry as you explore the Louvre's vast collections, making it an essential destination for any art and history enthusiast.",
                condition_free = "everyone under 18yo and every EU resident under 26yo",
                hours = Hours(
                    monday = "09:00 - 18:00",
                    tuesday = "closed",
                    wednesday = "09:00 - 18:00",
                    thursday = "09:00 - 18:00",
                    friday = "09:00 - 21:45",
                    saturday = "09:00 - 18:00",
                    sunday = "09:00 - 18:00"),
                category="Museum"),
            Activity(name="Parc des Buttes-Chaumont", address="1 Rue Botzaris, 75019 Paris",
                reason = "A beautiful public park with hills, bridges, and a lake.",
                condition_free = "For everyone",
                hours=Hours(
                    monday = "07:00 - 20:00",
                    tuesday = "07:00 - 20:00",
                    wednesday = "07:00 - 20:00",
                    thursday = "07:00 - 20:00",
                    friday = "07:00 - 20:00",
                    saturday = "07:00 - 20:00",
                    sunday = "07:00 - 20:00"),
                category="Garden")
        )



        for (element in activities) {
            activityList.add(element)
        }

        activityRecyclerView.adapter = ActivityRecyclerAdapter(activityList, this)
    }

    private fun readData() {
        var bool: Boolean = true
        var lieu: String = "Lieu"
        var i: Int =0
        database = FirebaseDatabase.getInstance().getReference("Lieu")
        while(bool){
            database.child(lieu.plus(i)).get().addOnSuccessListener {
                if(it.exists()){
                    val name = it.child("name").value
                    val address = it.child("address").value
                    val description = it.child("description").value
                    val condition_free = it.child("condition_free").value
                    val hours = it.child("hours").child("friday").value
                    val category = it.child("category").value
                    //activityList.add(Activity(name=name.toString(),address=address.toString(), ))

                }
                else{
                    bool = false
                }
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