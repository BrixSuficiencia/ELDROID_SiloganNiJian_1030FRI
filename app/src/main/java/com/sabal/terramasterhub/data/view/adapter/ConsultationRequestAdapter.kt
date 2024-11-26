package com.sabal.terramasterhub.data.view.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.ConsultationRequest

class ConsultationRequestAdapter(
    private var requests: List<ConsultationRequest>,
    private val onAccept: (ConsultationRequest) -> Unit,
    private val onDecline: (ConsultationRequest) -> Unit) :
    RecyclerView.Adapter<ConsultationRequestAdapter.ConsultationRequestViewHolder>() {

    inner class ConsultationRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(request: ConsultationRequest) {
            // Existing data binding
            itemView.findViewById<TextView>(R.id.textViewFinderName).text = request.finder_name
            itemView.findViewById<TextView>(R.id.textViewMessage).text = request.message
            itemView.findViewById<TextView>(R.id.textViewDate).text = request.date
            itemView.findViewById<TextView>(R.id.textViewTime).text = request.time
            itemView.findViewById<TextView>(R.id.textViewLocation).text = request.location
            itemView.findViewById<TextView>(R.id.textViewStatus).text = request.status
            itemView.findViewById<TextView>(R.id.textViewRate).text = "Rate: ${request.rate}"

            // Handle Accept Button Click
            itemView.findViewById<Button>(R.id.buttonAccept).setOnClickListener {
                showConfirmationDialog("Accept", request)
            }

            // Handle Decline Button Click
            itemView.findViewById<Button>(R.id.buttonDecline).setOnClickListener {
                showConfirmationDialog("Decline", request)
            }
        }

        private fun showConfirmationDialog(action: String, request: ConsultationRequest) {
            AlertDialog.Builder(itemView.context).apply {
                setTitle("$action Request")
                setMessage("Are you sure you want to $action this request?")
                setPositiveButton("Yes") { _, _ ->
                    if (action == "Accept") {
                        onAccept(request)  // Call the accept callback
                    } else {
                        onDecline(request)  // Call the decline callback
                    }
                }
                setNegativeButton("No", null)  // Dismiss dialog on "No"
                show()
            }
        }
    }



    // Create new view for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultationRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_consultation_request, parent, false)
        return ConsultationRequestViewHolder(view)
    }

    // Bind data to each item
    override fun onBindViewHolder(holder: ConsultationRequestViewHolder, position: Int) {
        holder.bind(requests[position])
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return requests.size
    }

    // Update data in the adapter
    fun updateData(newRequests: List<ConsultationRequest>) {
        requests = newRequests
        notifyDataSetChanged()
    }
}