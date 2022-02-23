package com.nightout.model

import java.io.Serializable


data class DashboardModel(
    val `data`: Data,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
) : Serializable {

    data class Data(
        var all_records: ArrayList<AllRecord>,
        var event_tickets: ArrayList<EventTicket>,
        var service_charge: ArrayList<ServiceCharge>,
        var stories: ArrayList<Story>,
        val noti_count : String,
        val venue_review_remaning   : String
    ) : Serializable

    data class AllRecord(
        val sub_records: ArrayList<SubRecord>,
        val title: String,
        val type: String
    ) : Serializable

    data class ServiceCharge(
        val id:String,
        val user_id:String,
        val charge:String,
        val created_at:String,
        val updated_at:String,
    ):Serializable

    data class EventTicket(
        val amount: String,
        val created_at: String,
        val id: String,
        val payment_mode: String,
        val qty: String,
        val status: String,
        val ticket_download: String,
        val ticket_number: String,
        val updated_at: String,
        val user_id: String,
        val vendor_id: String,
        val venue_detail: VenueDetail,
        val venue_id: String
    ) : Serializable

    data class Story(
        val created_at: String,
        val id: String,
        val storydetail: ArrayList<Storydetail>,
        val updated_at: String,
        val vendor_detail: VendorDetail,
        val vendor_id: String
    ) : Serializable

    data class SubRecord(
        val is_prime : String,
        val age_limit: String,
        var isChked: Boolean,
        val alcohol_license_image: String,
        val alcohol_license_number: String,
        val barcrawl: String,
        val by_default: String,
        val close_time: String,
        val created_at: String,
        val dress_code: String,
        val event_date: String,
        val event_end_time: String,
        val event_start_time: String,
        var favrouite: String,
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
    ) : Serializable

    data class Rating(
        val avg_rating: String,
        val percent: String,
        val review_title: String,
        val total_rating: String
    ) : Serializable

    data class VenueGallery(
        val created_at: String,
        val id: String,
        val image: String,
        val status: String,
        val thumbnail: String,
        val type: String,
        val updated_at: String,
        val venue_id: String
    ) : Serializable

    data class VenueDetail(
        val age_limit: String,
        val alcohol_license_image: String,
        val alcohol_license_number: String,
        val by_default: String,
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
        val id: String,
        val open_close: String,
        val open_time: String,
        val party_theme: String,
        val premises_license_image: String,
        val premises_license_number: String,
        val price: String,
        val price_couple: String,
        val price_hour: String,
        val reject_reason: String,
        val review: String,
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
        val venue_address: String
    ) : Serializable

    data class Storydetail(
        val created_at: String,
        val expired_time: String,
        val id: String,
        val image: String,
        val story_id: String,
        val thumbnail: String,
        val type: String,
        val updated_at: String
    ) : Serializable

    data class VendorDetail(
        val address: String,
        val created_at: String,
        val deleted_at: String,
        val device_id: String,
        val device_type: String,
        val email: String,
        val email_verified_at: String,
        val first_name: String,
        val id: String,
        val last_name: String,
        val name: String,
        val otp: String,
        val otp_expire: String,
        val phonenumber: String,
        val profile: String,
        val slug: String,
        val status: String,
        val updated_at: String,
        val userID: String
    ) : Serializable
}