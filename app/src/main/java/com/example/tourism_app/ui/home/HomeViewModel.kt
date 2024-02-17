package com.example.tourism_app.ui.home

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.DetailsActivity
import com.example.tourism_app.data.Activity
import com.example.tourism_app.data.ActivityRecyclerAdapter
import com.example.tourism_app.data.Category
import com.example.tourism_app.data.CategoryAdapter
import com.example.tourism_app.R
import com.example.tourism_app.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout

class HomeViewModel : ViewModel(), ActivityRecyclerAdapter.ActivityRecyclerEvent {

    private lateinit var activityList: ArrayList<Activity>
    private lateinit var fragment: HomeFragment

    override fun onItemClick(position: Int) {
        val activity = activityList[position]
        openDetailsActivity(activity)
    }

    fun setupViews(binding: FragmentHomeBinding, homeFragment: HomeFragment) {
        fragment = homeFragment

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
        getCategoryData(fragment, categoryList, categoryRecyclerView)

        // setupTabs(tabLayout)
    }

    fun setupTabs(tabLayout: TabLayout) {
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

        // test Activity elements until Firebase liaison
        val activities = listOf(
            Activity(name="Musée du Louvre", address="8 rue Sainte-Anne, 75001 Paris",
                description = "Experience the Louvre, the world's largest and most visited art museum, nestled in the heart of Paris. Home to over 35,000 works of art spanning from ancient civilizations to the 19th century, this iconic institution showcases the pinnacle of human creativity. Marvel at renowned masterpieces, including Leonardo da Vinci's Mona Lisa, the ancient Greek sculpture Venus de Milo, and the striking Winged Victory of Samothrace. The Louvre's architectural grandeur, from the medieval fortress to the glass pyramid entrance, adds to the allure of this cultural gem. Dive into a rich tapestry of history and artistry as you explore the Louvre's vast collections, making it an essential destination for any art and history enthusiast.",
                transport = null, hours="09:00 - 18:00", category="Museum"),
            Activity(name="Parc des Buttes-Chaumont", address="1 Rue Botzaris, 75019 Paris",
                description = "A beautiful public park with hills, bridges, and a lake.",
                transport = null, hours="07:00 - 22:00", category="Garden")
        )

        for (element in activities) {
            activityList.add(element)
        }

        activityRecyclerView.adapter = ActivityRecyclerAdapter(activityList, this)
    }

    private fun getCategoryData(fragment: HomeFragment,
                                categoryList: ArrayList<Category>,
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
}