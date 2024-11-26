package com.sabal.terramasterhub.data.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.ConsultationLog

class ConsultationLogsAdminAdapter(
    private val consultationLogs: List<ConsultationLog>) :
    RecyclerView.Adapter<ConsultationLogsAdminAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val locationTextView: TextView = view.findViewById(R.id.locationTextView)
        val rateTextView: TextView = view.findViewById(R.id.rateTextView)
        val statusTextView: TextView = view.findViewById(R.id.statusTextView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consultation_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val consultationLog = consultationLogs[position]

        holder.messageTextView.text = consultationLog.message ?: "No message"
        holder.dateTextView.text = consultationLog.date
        holder.locationTextView.text = consultationLog.location
        holder.rateTextView.text = "Rate: ${consultationLog.rate}"
        holder.statusTextView.text = consultationLog.status
    }

    override fun getItemCount(): Int {
        return consultationLogs.size
    }
}