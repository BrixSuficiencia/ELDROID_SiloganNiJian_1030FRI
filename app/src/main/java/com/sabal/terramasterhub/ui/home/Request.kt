package com.sabal.terramasterhub.ui.home


data class Request(
    val finder_id: String,
    val expert_id: String?,
    val surveyor_id: String?,
    val message: String,
    val status: String,
    val updated_at: String,
    val created_at: String,
    val id: String,
    val expert: Expert?,
    val surveyor: Surveyor?
)