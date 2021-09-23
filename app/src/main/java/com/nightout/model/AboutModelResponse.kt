package com.nightout.model

data class AboutModelResponse(
    val `data`: List<Data>,
    val message: String,
    val response: String,
    val status_code: Int
){

data class Data(
    val content: String,
    val created_at: String,
    val id: String,
    val slug: String,
    val status: String,
    val subject: String,
    val title: String,
    val updated_at: String
)}