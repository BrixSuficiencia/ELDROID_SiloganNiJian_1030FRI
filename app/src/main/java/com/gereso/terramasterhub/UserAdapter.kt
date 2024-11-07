package com.gereso.terramasterhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class UserAdapter(
    private val users: List<User>,
    private val userClickListener: UserClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface UserClickListener {
        fun onUserClicked(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewuserlist, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position], userClickListener)
    }

    override fun getItemCount() = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImageView: ShapeableImageView = itemView.findViewById(R.id.profileImage)
        private val profileNameTextView: TextView = itemView.findViewById(R.id.profileName)

        fun bind(user: User, userClickListener: UserClickListener) {
            profileNameTextView.text = user.name
            itemView.setOnClickListener { userClickListener.onUserClicked(user) }

            // Load profile image using Glide
            if (user.profileImageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.noprofile) // Placeholder image
                    .error(R.drawable.noprofile) // Error image
                    .into(profileImageView)
            } else {
                profileImageView.setImageResource(R.drawable.noprofile)
            }
        }
    }
}
