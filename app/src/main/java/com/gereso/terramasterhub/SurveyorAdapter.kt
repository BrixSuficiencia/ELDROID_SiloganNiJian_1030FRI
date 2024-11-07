package com.gereso.terramasterhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SurveyorAdapter(private val itemList: MutableList<Surveyor>, private val clickListener: OnItemClickListener) : RecyclerView.Adapter<SurveyorAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Surveyor)
        fun onMessageClick(item: Surveyor) // Add this function to handle message button clicks
    }

    fun submitList(newList: List<Surveyor>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewexperts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, clickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val specialtyTextView: TextView = itemView.findViewById(R.id.specialtyTextView)
        private val introductionTextView: TextView = itemView.findViewById(R.id.introductionTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val proposalsTextView: TextView = itemView.findViewById(R.id.proposalsText)
        private val locationTextView: TextView = itemView.findViewById(R.id.LocationText)
        private val expertImageView: ImageView = itemView.findViewById(R.id.profileImage)
        private val messageButton: ImageButton = itemView.findViewById(R.id.Message) // Add this line

        fun bind(item: Surveyor, clickListener: OnItemClickListener) {
            nameTextView.text = item.name
            specialtyTextView.text = item.specialty
            introductionTextView.text = item.introduction
            priceTextView.text = item.price
            proposalsTextView.text = item.proposals
            locationTextView.text = item.location
            itemView.setOnClickListener {
                clickListener.onItemClick(item)
            }
            messageButton.setOnClickListener {
                clickListener.onMessageClick(item)
            }

            // Load image using Picasso if imageUrl is not null
            if (!item.imageUrl.isNullOrEmpty()) {
                Picasso.get().load(item.imageUrl).into(expertImageView)
            } else {
                // Set a placeholder image if imageUrl is null or empty
                expertImageView.setImageResource(R.drawable.noprofile)
            }
        }
    }
}
