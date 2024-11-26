package com.sabal.terramasterhub.data.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Expert

class ExpertAdapter(
    private val context: Context,
    private var experts: List<Expert>,
    private val onRequestConsultation: (Expert) -> Unit
) : RecyclerView.Adapter<ExpertAdapter.ExpertViewHolder>() {

    class ExpertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textName)
        val emailTextView: TextView = itemView.findViewById(R.id.textEmail)
        val certificationTextView: TextView = itemView.findViewById(R.id.textCertification)
        val licenseTextView: TextView = itemView.findViewById(R.id.textLicense)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_expert, parent, false)
        return ExpertViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpertViewHolder, position: Int) {
        val expert = experts[position]
        holder.nameTextView.text = expert.name
        holder.emailTextView.text = expert.email
        holder.certificationTextView.text = expert.certification_id
        holder.licenseTextView.text = expert.license_number

        // Set OnClickListener for each item to show dialog
        holder.itemView.setOnClickListener {
            showExpertDialog(expert)
        }
    }

    override fun getItemCount(): Int {
        return experts.size
    }

    fun updateExperts(newExperts: List<Expert>) {
        experts = newExperts
        notifyDataSetChanged()
    }

    private fun showExpertDialog(expert: Expert) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Expert Details")
        dialogBuilder.setMessage(
            """
            Name: ${expert.name}
            Email: ${expert.email}
            Certification ID: ${expert.certification_id}
            License Number: ${expert.license_number}
   
            """.trimIndent()
        )
        dialogBuilder.setPositiveButton("Request") { dialog, _ ->
            // Trigger the callback for consultation request
            onRequestConsultation(expert)
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // Close dialog on Cancel
            dialog.dismiss()
        }

        dialogBuilder.create().show()
    }
}
