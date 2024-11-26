package com.sabal.terramasterhub.data.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Expert
import com.sabal.terramasterhub.data.model.Request

class RequestAdapter(
    private val context: Context,
    private var requests: List<Request>,
    private val onRequestConsultation: (Request) -> Unit,
    private val onDeleteRequest: (Request) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {


    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare view elements
        val messageTextView: TextView = itemView.findViewById(R.id.textViewMessage)
        val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        val timeTextView: TextView = itemView.findViewById(R.id.textViewTime)
        val locationTextView: TextView = itemView.findViewById(R.id.textViewLocation)
        val rateTextView: TextView = itemView.findViewById(R.id.textViewRate)
        val expertNameTextView: TextView = itemView.findViewById(R.id.textViewExpertName)
        val surveyorNameTextView: TextView = itemView.findViewById(R.id.textViewSurveyorName)
        val updatedAtTextView: TextView = itemView.findViewById(R.id.textViewUpdatedAt)
        val createdAtTextView: TextView = itemView.findViewById(R.id.textViewCreatedAt)



        // Button elements
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return requests.size
    }
    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]

        // Bind data to views
        holder.messageTextView.text = request.message
        holder.statusTextView.text = request.status
        holder.dateTextView.text = request.date
        holder.timeTextView.text = request.time
        holder.locationTextView.text = request.location
        holder.rateTextView.text = request.rate.toString()
        holder.expertNameTextView.visibility = View.GONE
        holder.surveyorNameTextView.visibility = View.GONE
        holder.updatedAtTextView.visibility = View.GONE
        holder.createdAtTextView.visibility  = View.GONE

        // Set click listeners for buttons
        holder.updateButton.setOnClickListener {
            onRequestConsultation(request) // Invoke callback for "Update"
        }

        holder.deleteButton.setOnClickListener {
            onDeleteRequest(request)
        }
    }

    // Function to update the requests list
    fun updateRequests(newRequests: List<Request>) {
        requests = newRequests
        notifyDataSetChanged()  // Notify the adapter to refresh the list
    }

}