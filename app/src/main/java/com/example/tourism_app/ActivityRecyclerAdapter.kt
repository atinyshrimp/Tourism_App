package com.example.tourism_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActivityRecyclerAdapter(private val activityList: ArrayList<Activity>) : RecyclerView.Adapter<ActivityRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent,
                                                                    false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = activityList[position]

        holder.name.text = currentItem.name
        holder.category.text = currentItem.category
        holder.location.text = "Paris ${getArrondissement(currentItem.address)}"
        holder.visitCount.text = "(51)"
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    private fun getArrondissement(address: String?): String? {
        val pattern = Regex("\\b750(\\d{2})\\b")
        val matchResult = pattern.find(address.toString())

        val arrondissementNumber = matchResult?.groupValues?.get(1)?.toIntOrNull()

        return when {
            arrondissementNumber != null && arrondissementNumber in 1..20 -> {
                "$arrondissementNumber${getOrdinalSuffix(arrondissementNumber)}"
            }
            else -> null
        }
    }

    private fun getOrdinalSuffix(number: Int): String {
        return when {
            number in 11..13 -> "th"
            number % 10 == 1 -> "st"
            number % 10 == 2 -> "nd"
            number % 10 == 3 -> "rd"
            else -> "th"
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.tvName)
        val location : TextView = itemView.findViewById(R.id.tvLocation)
        val visitCount : TextView = itemView.findViewById(R.id.tvVisitCount)
        val category : TextView = itemView.findViewById(R.id.tvCategory)
        val picture : ImageView = itemView.findViewById(R.id.ivPicture)
    }
}