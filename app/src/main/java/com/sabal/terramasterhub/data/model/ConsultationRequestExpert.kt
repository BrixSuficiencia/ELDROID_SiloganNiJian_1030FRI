package com.sabal.terramasterhub.data.model

data class ConsultationRequestExpert(  val expert_id: String,
                                       val message: String,
                                       val date: String,
                                       val time: String,
                                       val location: String,
                                       val rate: Double)
