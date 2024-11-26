package com.sabal.terramasterhub.data.model

data class ConsultationRequest(val finder_id: String,
                               val expert_id: String?,  // Nullable to accommodate surveyor requests if the field might differ
                               val surveyor_id: String?,  // Nullable field for surveyors
                               val finder_name: String,
                               val message: String,
                               val status: String,
                               val date: String,
                               val time: String,
                               val location: String,
                               val rate: Double,
                               val updated_at: String,
                               val created_at: String,
                               val id: String)
