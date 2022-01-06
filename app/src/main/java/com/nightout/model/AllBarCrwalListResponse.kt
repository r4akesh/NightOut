package com.nightout.model

import java.io.Serializable

data class AllBarCrwalListResponse(
    val `data`: ArrayList<Data>,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
) : Serializable{

    data class Data(
        var viewType : Int,
        var isSelected : Boolean,
        val age_limit: String,
        val alcohol_license_image: String,
        val alcohol_license_number: String,
        val barcrawl: String,
        val by_default: String,
        val city: String,
        val close_time: String,
        val created_at: String,
        val dress_code: String,
        val event_date: String,
        val event_end_time: String,
        val event_start_time: String,
        val favrouite: String,
        val food_certificate_image: String,
        val food_certificate_number: String,
        val food_registration_image: String,
        val food_registration_number: String,
        val free_end_time: String,
        val free_start_time: String,
        val id: String,
        val open_close: String,
        val open_time: String,
        val party_theme: String,
        val premises_license_image: String,
        val premises_license_number: String,
        val price: String,
        val price_couple: String,
        val price_hour: String,
        val rating: Rating,
        val reject_reason: String,
        val sale_price: String,
        val slug: String,
        val status: String,
        val store_address: String,
        val store_description: String,
        val store_email: String,
        val store_lattitude: String,
        val store_logo: String,
        val store_longitude: String,
        val store_name: String,
        val store_number: String,
        val store_type: String,
        val tax_reference_image: String,
        val tax_reference_number: String,
        val ticket_qty: String,
        val updated_at: String,
        val user_id: String,
        val venue_address: String,
        val venue_gallery: ArrayList<VenueGallery>
    ): Serializable

    data class Rating(
        val avg_rating: String,
        val percent: String,
        val review_title: String,
        val total_rating: String
    ): Serializable

    data class VenueGallery(
        val created_at: String,
        val id: String,
        val image: String,
        val status: String,
        val thumbnail: String,
        val type: String,
        val updated_at: String,
        val venue_id: String
    ): Serializable
}