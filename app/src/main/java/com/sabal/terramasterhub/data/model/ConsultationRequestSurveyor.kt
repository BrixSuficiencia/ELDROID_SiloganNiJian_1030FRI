package com.sabal.terramasterhub.data.model

data class ConsultationRequestSurveyor(val surveyor_id: String,
                                       val message: String,
                                       val date: String,
                                       val time: String,
                                       val location: String,
                                       val rate: Double)
