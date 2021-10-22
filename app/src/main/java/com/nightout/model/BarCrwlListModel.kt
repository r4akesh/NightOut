package com.nightout.model

data class BarCrwlListModel(
    val `data`: Data,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
) {

    data class Data(
        val barcrawl: ArrayList<Barcrawl>,
        val barcrawl_options: ArrayList<BarcrawlOption>,
        val favrtvenues: ArrayList<Favrtvenue>
    )

    data class Barcrawl(
        val created_at: String,
        val id: Int,
        val status: Int,
        val store_type: Int,
        val updated_at: String,
        val user_id: Int,
        val vendor_id: Int,
        val venue_detail: VenueDetail,
        val venue_id: Int
    )

    data class BarcrawlOption(
        val created_at: String,
        val id: Int,
        val slug: String,
        val status: Int,
        val title: String,
        val updated_at: String
    )

    data class Favrtvenue(
        val created_at: String,
        val id: Int,
        val status: Int,
        val updated_at: String,
        val user_id: Int,
        val vendor_id: Int,
        val venue_detail: VenueDetailX,
        val venue_id: Int
    )

    data class VenueDetail(
        val age_limit: String,
        val alcohol_license_image: String,
        val alcohol_license_number: String,
        val by_default: Int,
        val close_time: String,
        val created_at: String,
        val dress_code: String,
        val event_date: String,
        val event_end_time: String,
        val event_start_time: String,
        val food_certificate_image: String,
        val food_certificate_number: String,
        val food_registration_image: String,
        val food_registration_number: String,
        val free_end_time: String,
        val free_start_time: String,
        val id: Int,
        val open_close: Int,
        val open_time: String,
        val party_theme: String,
        val premises_license_image: String,
        val premises_license_number: String,
        val price: String,
        val price_couple: String,
        val price_hour: String,
        val reject_reason: Any,
        val sale_price: String,
        val slug: String,
        val status: Int,
        val store_address: String,
        val store_description: String,
        val store_email: String,
        val store_lattitude: String,
        val store_logo: String,
        val store_longitude: String,
        val store_name: String,
        val store_number: String,
        val store_type: Int,
        val tax_reference_image: String,
        val tax_reference_number: String,
        val ticket_qty: String,
        val updated_at: String,
        val user_id: Int,
        val venue_address: String
    )

    data class VenueDetailX(
        val age_limit: String,
        val alcohol_license_image: String,
        val alcohol_license_number: String,
        val by_default: Int,
        val close_time: Any,
        val created_at: String,
        val dress_code: String,
        val event_date: String,
        val event_end_time: Any,
        val event_start_time: Any,
        val food_certificate_image: String,
        val food_certificate_number: String,
        val food_registration_image: String,
        val food_registration_number: String,
        val free_end_time: String,
        val free_start_time: String,
        val id: Int,
        val open_close: Int,
        val open_time: Any,
        val party_theme: String,
        val premises_license_image: String,
        val premises_license_number: String,
        val price: String,
        val price_couple: String,
        val price_hour: Any,
        val reject_reason: Any,
        val sale_price: Any,
        val slug: String,
        val status: Int,
        val store_address: String,
        val store_description: Any,
        val store_email: String,
        val store_lattitude: String,
        val store_logo: String,
        val store_longitude: String,
        val store_name: String,
        val store_number: String,
        val store_type: Int,
        val tax_reference_image: String,
        val tax_reference_number: String,
        val ticket_qty: String,
        val updated_at: String,
        val user_id: Int,
        val venue_address: Any
    )
}