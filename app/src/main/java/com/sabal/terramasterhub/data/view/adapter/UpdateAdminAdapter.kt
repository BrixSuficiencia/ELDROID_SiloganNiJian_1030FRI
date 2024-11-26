package com.sabal.terramasterhub.data.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Update

class UpdateAdminAdapter(
    private val context: Context,
    private var updates: List<Update>,
    private val onUpdateClick: (Update) -> Unit,
    private val onDeleteClick: (Update) -> Unit
) : RecyclerView.Adapter<UpdateAdminAdapter.UpdateViewHolder>() {

    // ViewHolder for holding each item view
    inner class UpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val contentTextView: TextView = itemView.findViewById(R.id.textViewContent)
        val createdAtTextView: TextView = itemView.findViewById(R.id.textViewCreatedAt)
        val updatedAtTextView: TextView = itemView.findViewById(R.id.textViewUpdatedAt)
        val updateButton: Button = itemView.findViewById(R.id.buttonUpdate)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)

        // Setting click listeners for buttons
        init {
            updateButton.setOnClickListener {
                onUpdateClick(updates[adapterPosition])
            }
            deleteButton.setOnClickListener {
                onDeleteClick(updates[adapterPosition])
            }
        }
    }

    // Inflating the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_admin_update, parent, false)
        return UpdateViewHolder(view)
    }

    // Binding data to each view
    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        val update = updates[position]
        holder.titleTextView.text = update.title
        holder.contentTextView.text = update.content
        holder.createdAtTextView.text = "Created at: ${update.created_at}"
        holder.updatedAtTextView.text = "Updated at: ${update.updated_at}"
    }

    // Return the size of the updates list
    override fun getItemCount(): Int = updates.size

    // Method to update the updates list in the adapter
    fun updateUpdates(newUpdates: List<Update>) {
        updates = newUpdates
        notifyDataSetChanged()  // Notify that the data has changed
    }

}