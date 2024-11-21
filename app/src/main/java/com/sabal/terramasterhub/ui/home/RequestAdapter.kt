package com.sabal.terramasterhub.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R

class RequestAdapter(
    private var requests: List<Request>,  // Make requests mutable
    private val onRequestClicked: (Request) -> Unit,
    private val onRequestDeleteClicked: (Request) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = requests.size

    // Update the requests list and notify adapter about the data change
    fun updateRequests(newRequests: List<Request>) {
        requests = newRequests
        notifyDataSetChanged()
    }

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(request: Request) {
            itemView.findViewById<TextView>(R.id.textViewMessage).text = request.message
            itemView.findViewById<TextView>(R.id.textViewStatus).text = request.status
            itemView.findViewById<TextView>(R.id.textViewUpdatedAt).text = request.updated_at

            // Optionally, set the expert/surveyor details if available
            itemView.findViewById<TextView>(R.id.textViewExpertName).text = request.expert?.name ?: "empty"
            itemView.findViewById<TextView>(R.id.textViewSurveyorName).text = request.surveyor?.name ?: "empty"

            // Handle item click
            itemView.setOnClickListener {
                onRequestClicked(request)
            }

            // Inside RequestViewHolder class in RequestAdapter
            itemView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
                // Call the ViewModel method to delete the request
                onRequestDeleteClicked(request)
            }
        }
    }
}
