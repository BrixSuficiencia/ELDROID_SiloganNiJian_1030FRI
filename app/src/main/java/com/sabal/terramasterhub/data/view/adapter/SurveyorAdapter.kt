package com.sabal.terramasterhub.data.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Surveyor

class SurveyorAdapter(
    private val context: Context,
    private var surveyors: List<Surveyor>,
    private val onRequestConsultation: (Surveyor) -> Unit
) : RecyclerView.Adapter<SurveyorAdapter.SurveyorViewHolder>() {

    class SurveyorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textName)
        val emailTextView: TextView = itemView.findViewById(R.id.textEmail)
        val certificationTextView: TextView = itemView.findViewById(R.id.textCertification)
        val licenseTextView: TextView = itemView.findViewById(R.id.textLicense)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_expert, parent, false)
        return SurveyorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SurveyorViewHolder, position: Int) {
        val surveyor = surveyors[position]
        holder.nameTextView.text = surveyor.name
        holder.emailTextView.text = surveyor.email
        holder.certificationTextView.text = surveyor.certification_id
        holder.licenseTextView.text = surveyor.license_number

        // Set OnClickListener for each item to show dialog
        holder.itemView.setOnClickListener {
            showSurveyorDialog(surveyor)
        }
    }

    override fun getItemCount(): Int {
        return surveyors.size
    }

    fun updateSurveyors(newSurveyors: List<Surveyor>) {
        surveyors = newSurveyors
        notifyDataSetChanged()
    }

    private fun showSurveyorDialog(surveyor: Surveyor) {
        val dialogBuilder = AlertDialog.Builder(context, R.style.ExpertsAlertDialogTheme)
        dialogBuilder.setTitle("Surveyor Details")
        dialogBuilder.setMessage(
            """
            Name: ${surveyor.name}
            Email: ${surveyor.email}
            Certification ID: ${surveyor.certification_id}
            License Number: ${surveyor.license_number}
            """.trimIndent()
        )

        dialogBuilder.setPositiveButton("Request") { dialog, _ ->
            // Trigger the callback for consultation request
            onRequestConsultation(surveyor)
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // Close dialog on Cancel
            dialog.dismiss()
        }

        dialogBuilder.create().show()
    }
}
