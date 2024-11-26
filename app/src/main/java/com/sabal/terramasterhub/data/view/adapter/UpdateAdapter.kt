package com.sabal.terramasterhub.data.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Update

class UpdateAdapter(
    private val context: Context,
    private var updates: List<Update>
) : RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder>(){

    class UpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textTitle)
        val contentTextView: TextView = itemView.findViewById(R.id.textContent)
        val updatedAtTextView: TextView = itemView.findViewById(R.id.textUpdatedAt)
        val createdAtTextView: TextView = itemView.findViewById(R.id.textCreatedAt)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_update, parent, false)
        return UpdateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        val update = updates[position]
        holder.titleTextView.text = update.title
        holder.contentTextView.text = update.content
        holder.updatedAtTextView.text = "Updated at: ${update.updated_at}"
        holder.createdAtTextView.text = "Created at: ${update.created_at}"

    }

    override fun getItemCount(): Int = updates.size

    fun updateUpdates(newUpdates: List<Update>) {
        updates = newUpdates
        notifyDataSetChanged()
    }
}
