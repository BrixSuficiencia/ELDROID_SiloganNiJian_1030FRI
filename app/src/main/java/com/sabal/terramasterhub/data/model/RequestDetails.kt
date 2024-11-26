package com.sabal.terramasterhub.data.model

data class RequestDetails( val finder_id: String,
                           val expert_id: String,
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
