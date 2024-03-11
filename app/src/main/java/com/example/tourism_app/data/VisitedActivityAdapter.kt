package com.example.tourism_app.data

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.DatabaseManager
import com.example.tourism_app.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class VisitedActivityAdapter(
    private val activityList: ArrayList<Activity>,
    private val listener: ActivityRecyclerEvent,
    private val user: String,
    private val likeButtonClickListener: LikeButtonClickListener
) : RecyclerView.Adapter<VisitedActivityAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card_visited_activity, parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = activityList[position]

        holder.name.text = currentItem.name
        holder.category.text = currentItem.category
        holder.location.text = "Paris ${currentItem.getArrondissement()}"
        holder.visitCount.text = "(${currentItem.nbVisit.toString()})"

        var imageName = currentItem.name
        if(imageName != null) {
            imageName = imageName.replace(" ", "")
            val storageRef = FirebaseStorage.getInstance().reference.child("LieuImage/$imageName.jpg")
            val localFile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                holder.picture.setImageBitmap(bitmap)
            }
        }

        DatabaseManager.getVisitDate(user, currentItem.name!!) {visitDate ->
            if (visitDate != null) {
                holder.visitDate.text = "Visited on $visitDate"
            } else {
                holder.visitDate.visibility = View.GONE
            }
        }

        setupLikeButton(holder, currentItem)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    private fun setupLikeButton(holder: MyViewHolder, currentItem: Activity) {
        changeLikeButtonIcon(holder, currentItem)
        holder.like_button.setOnClickListener {
            likeButtonClickListener.onLikeButtonClicked(holder.adapterPosition, currentItem)
        }
    }
    private fun changeLikeButtonIcon(holder: MyViewHolder, currentItem: Activity) {
        val likeButton: ImageButton = holder.like_button
        DatabaseManager.isActivityLiked(user, currentItem.name!!) { isLiked ->
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_heart_filled_black_24dp)
            } else {
                likeButton.setImageResource(R.drawable.ic_heart_black_24dp)
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name : TextView = itemView.findViewById(R.id.tvName)
        val location : TextView = itemView.findViewById(R.id.tvLocation)
        val visitCount : TextView = itemView.findViewById(R.id.tvVisitCount)
        val visitDate : TextView = itemView.findViewById(R.id.tvVisitDate)
        val category : TextView = itemView.findViewById(R.id.tvCategory)
        private val cardClickable : ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        val picture : ImageView = itemView.findViewById(R.id.ivPicture)
        val like_button : ImageButton = itemView.findViewById(R.id.ibLike)

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

    interface LikeButtonClickListener {
        fun onLikeButtonClicked(position: Int, currentItem: Activity)
    }
}