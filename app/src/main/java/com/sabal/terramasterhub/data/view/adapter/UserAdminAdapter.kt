package com.sabal.terramasterhub.data.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Update
import com.sabal.terramasterhub.data.model.User

class UserAdminAdapter(
    private val context: Context,
    private var users: List<User>,
    private val onDeleteClick: (User) -> Unit) :
    RecyclerView.Adapter<UserAdminAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = itemView.findViewById(R.id.textName)
        val emailTextView: TextView = itemView.findViewById(R.id.textEmail)
        val userTypeTextView: TextView = itemView.findViewById(R.id.textUserType)
        val userIdTextView: TextView = itemView.findViewById(R.id.textUserId)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)

        // Setting click listeners for buttons
        init {
            deleteButton.setOnClickListener {
                onDeleteClick(users[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        // Set the data from the user object to the respective TextViews
        holder.nameTextView.text = user.name
        holder.emailTextView.text = user.email
        holder.userTypeTextView.text = user.user_type
        holder.userIdTextView.text = user.id


    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()  // Notify that the data has changed
    }
}
