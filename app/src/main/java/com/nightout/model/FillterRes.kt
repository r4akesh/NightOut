package com.nightout.model

data class FillterRes(
    val `data`: Data,
    val message: String,
    val response: String,
    val status_code: Int
) {
    data class Data(
        val filter_name: ArrayList<FilterName>
    )

    data class FilterName(
        var isSelected: Boolean,
        val created_at: String,
        val filter_options: ArrayList<FilterOption>,
        val id: String,
        val slug: String,
        val status: String,
        val title: String,
        val updated_at: String
    )

    data class FilterOption(
        var isChekd: Boolean,
        val created_at: String,
        val filter_name_id: String,
        val id: String,
        val slug: String,
        val status: String,
        val title: String,
        val updated_at: String
    )
}