package com.example.tourism_app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.Activity
import com.example.tourism_app.ActivityRecyclerAdapter
import com.example.tourism_app.Category
import com.example.tourism_app.CategoryAdapter
import com.example.tourism_app.R
import com.example.tourism_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var activityRecyclerView : RecyclerView
    private lateinit var activityList : ArrayList<Activity>

    private lateinit var categoryList : ArrayList<Category>
    private lateinit var categoryRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // getting values for the Activity recycler view
        activityRecyclerView = binding.activityList
        activityRecyclerView.layoutManager = LinearLayoutManager(this.context,
                                                                LinearLayoutManager.HORIZONTAL,
                                                                false)

        // initializing the list of activities
        activityList = arrayListOf<Activity>()
        getActivityData()

        // getting values for the Category recycler view
        categoryRecyclerView = binding.categoryList
        categoryRecyclerView.layoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        // initializing the list of categories
        categoryList = arrayListOf<Category>()
        getCategoryData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getActivityData() {

        // test Activity elements until Firebase liaison
        val activities = listOf(
            Activity(name="Louvre", address="8 rue Sainte-Anne, 75001 Paris",
            description = "Experience the Louvre, the world's largest and most visited art museum, nestled in the heart of Paris. Home to over 35,000 works of art spanning from ancient civilizations to the 19th century, this iconic institution showcases the pinnacle of human creativity. Marvel at renowned masterpieces, including Leonardo da Vinci's Mona Lisa, the ancient Greek sculpture Venus de Milo, and the striking Winged Victory of Samothrace. The Louvre's architectural grandeur, from the medieval fortress to the glass pyramid entrance, adds to the allure of this cultural gem. Dive into a rich tapestry of history and artistry as you explore the Louvre's vast collections, making it an essential destination for any art and history enthusiast.",
            transport = null, hours="09:00 - 18:00", category="Museum"),
            Activity(name="Parc des Buttes-Chaumont", address="1 Rue Botzaris, 75019 Paris",
            description = "A beautiful public park with hills, bridges, and a lake.",
            transport = null, hours="07:00 - 22:00", category="Garden")
        )

        for (element in activities) {
            activityList.add(element)
        }

        activityRecyclerView.adapter = ActivityRecyclerAdapter(activityList)
    }

    private fun getCategoryData() {
        val categories = listOf(
            Category(resources.getStringArray(R.array.categories)[0], R.drawable.category_aesthetics),
            Category(resources.getStringArray(R.array.categories)[1], R.drawable.category_event),
            Category(resources.getStringArray(R.array.categories)[2], R.drawable.category_garden),
            Category(resources.getStringArray(R.array.categories)[3], R.drawable.category_museum)
        )

        for (element in categories) {
            categoryList.add(element)
        }

        categoryRecyclerView.adapter = CategoryAdapter(categoryList)
    }
}