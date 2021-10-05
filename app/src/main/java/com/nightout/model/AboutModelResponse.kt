package com.nightout.model

import java.io.Serializable

data class AboutModelResponse(
    val `data`: Data,
    val message: String,
    val response: String,
    val status_code: Int
) : Serializable {
    data class Data(
        val about_us: ArrayList<AboutU>,
        val faq: ArrayList<Faq>,
        val term: ArrayList<Term>
    ) : Serializable

    data class AboutU(
        val content: String,
        val created_at: String,
        val id: String,
        val slug: String,
        val status: String,
        val subject: String,
        val title: String,
        val type: String,
        val updated_at: String
    ) : Serializable

    data class Faq(
        val content: String,
        val created_at: String,
        val id: String,
        val slug: String,
        val status: String,
        val subject: String,
        val title: String,
        val type: String,
        val updated_at: String
    ) : Serializable

    data class Term(
        val content: String,
        val created_at: String,
        val id: String,
        val slug: String,
        val status: String,
        val subject: String,
        val title: String,
        val type: String,
        val updated_at: String
    ) : Serializable
}