package com.sabal.terramasterhub.data.model

data class ConsultationLog( val consultation_request_id: String,
                            val user_id: String,
                            val status: String,
                            val response_message: String?,
                            val message: String,
                            val date: String,
                            val time: String,
                            val location: String,
                            val rate: Int,
                            val updated_at: String,
                            val created_at: String,
                            val id: String,
                            val consultation_request: ConsultationRequest?)
