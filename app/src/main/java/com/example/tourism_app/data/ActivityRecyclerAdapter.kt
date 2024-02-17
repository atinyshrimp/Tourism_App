package com.example.tourism_app.data

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R

class ActivityRecyclerAdapter(
    private val activityList: ArrayList<Activity>,
    private val listener: ActivityRecyclerEvent
) : RecyclerView.Adapter<ActivityRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_card, parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = activityList[position]

        holder.name.text = currentItem.name
        holder.category.text = currentItem.category
        holder.location.text = "Paris ${currentItem.getArrondissement()}"
        holder.visitCount.text = "(51)"
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name : TextView = itemView.findViewById(R.id.tvName)
        val location : TextView = itemView.findViewById(R.id.tvLocation)
        val visitCount : TextView = itemView.findViewById(R.id.tvVisitCount)
        val category : TextView = itemView.findViewById(R.id.tvCategory)
        private val cardClickable : ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        val picture : ImageView = itemView.findViewById(R.id.ivPicture)

        init {
            cardClickable.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface ActivityRecyclerEvent {
        fun onItemClick(position: Int)
    }
}