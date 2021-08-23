package com.nightout.model

import java.io.Serializable


data class DashboardModel(
    val `data`: Data,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
): Serializable

data class Data(
    val bars: ArrayList<Bar>,
    val clubs: ArrayList<Club>,
    val events: ArrayList<Any>,
    val foods: ArrayList<Food>,
    val pubs: ArrayList<Pub>,
    val stories: ArrayList<Story>
): Serializable

data class Bar(
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
    val favrouite: String,
    val food_certificate_image: String,
    val food_certificate_number: String,
    val food_registration_image: String,
    val food_registration_number: String,
    val free_end_time: String,
    val free_start_time: String,
    val id: String,
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
    val ticker_qty: String,
    val updated_at: String,
    val user_id: String,
    val venue_gallery: ArrayList<VenueGallery>
): Serializable

data class Club(
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
    val favrouite: String,
    val food_certificate_image: String,
    val food_certificate_number: String,
    val food_registration_image: String,
    val food_registration_number: String,
    val free_end_time: String,
    val free_start_time: String,
    val id: String,
    val open_time: String,
    val party_theme: String,
    val premises_license_image: String,
    val premises_license_number: String,
    val price: String,
    val price_couple: String,
    val price_hour: String,
    val rating: RatingX,
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
    val ticker_qty: String,
    val updated_at: String,
    val user_id: String,
    val venue_gallery: ArrayList<Any>
): Serializable

data class Food(
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
    val favrouite: String,
    val food_certificate_image: String,
    val food_certificate_number: String,
    val food_registration_image: String,
    val food_registration_number: String,
    val free_end_time: String,
    val free_start_time: String,
    val id: String,
    val open_time: String,
    val party_theme: String,
    val premises_license_image: String,
    val premises_license_number: String,
    val price: String,
    val price_couple: String,
    val price_hour: String,
    val rating: RatingXX,
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
    val ticker_qty: String,
    val updated_at: String,
    val user_id: String,
    val venue_gallery: ArrayList<Any>
):Serializable

data class Pub(
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
    val favrouite: String,
    val food_certificate_image: String,
    val food_certificate_number: String,
    val food_registration_image: String,
    val food_registration_number: String,
    val free_end_time: String,
    val free_start_time: String,
    val id: String,
    val open_time: String,
    val party_theme: String,
    val premises_license_image: String,
    val premises_license_number: String,
    val price: String,
    val price_couple: String,
    val price_hour: String,
    val rating: RatingXXX,
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
    val ticker_qty: String,
    val updated_at: String,
    val user_id: String,
    val venue_gallery: ArrayList<Any>
):Serializable

data class Story(
    val created_at: String,
    val id: String,
    val storydetail: ArrayList<Storydetail>,
    val updated_at: String,
    val vendor_detail: VendorDetail,
    val vendor_id: String
):Serializable

data class Rating(
    val avg_rating: String,
    val total_rating: String
):Serializable

data class VenueGallery(
    val created_at: String,
    val id: String,
    val image: String,
    val status: String,
    val thumbnail: String,
    val type: String,
    val updated_at: String,
    val venue_id: String
):Serializable

data class RatingX(
    val avg_rating: String,
    val total_rating: String
):Serializable

data class RatingXX(
    val avg_rating: String,
    val total_rating: String
):Serializable

data class RatingXXX(
    val avg_rating: String,
    val total_rating: String
):Serializable

data class Storydetail(
    val created_at: String,
    val expired_time: String,
    val id: String,
    val image: String,
    val story_id: String,
    val thumbnail: String,
    val type: String,
    val updated_at: String
):Serializable

data class VendorDetail(
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
    val office_number: String,
    val otp: String,
    val otp_expire: String,
    val password: String,
    val phonenumber: String,
    val profile: String,
    val remember_token: String,
    val slug: String,
    val status: String,
    val updated_at: String,
    val userID: String
):Serializable