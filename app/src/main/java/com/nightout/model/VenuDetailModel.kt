package com.nightout.model

import java.io.Serializable

data class VenuDetailModel(
    val `data`: Data,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
):Serializable {
    data class Data(
        val age_limit: String,
        val alcohol_license_image: String,
        val alcohol_license_number: String,
        val all_products: MutableList<AllProduct>,
        val barcrawl: String,
        val by_default: String,
        val city: String,
        val close_time: String,
        val created_at: String,
        val dayInformations: MutableList<DayInformation>,
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
        val vendor_detail: VendorDetail,
        val venue_address: String,
        val venue_facility: MutableList<VenueFacility>,
        val venue_gallery: MutableList<VenueGallery>
    ):Serializable

    data class AllProduct(
        val records: MutableList<Record>,
        val type: String,
        var isSelected:Boolean
    ):Serializable

    data class DayInformation(
        val day: String,
        val info: MutableList<Info>
    ):Serializable

    data class Rating(
        val avg_rating: String,
        val percent: String,
        val review_title: String,
        val total_rating: String
    ):Serializable

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
    ):Serializable

    data class VenueFacility(
        val created_at: String,
        val facility_detail: FacilityDetail,
        val facility_id: String,
        val id: String,
        val status: String,
        val updated_at: String,
        val venue_id: String
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

    data class Record(
        var isSelected: Boolean,
        var quantityLocal: Int=0,
        var totPriceLocal: Double = 0.0,
        val category: String,
        val category_id: String,
        val category_type: String,
        val created_at: String,
        val description: String,
        val discount: String,
        val free: String,
        val id: String,
        val image: String,
        val ml: String,
        val price: String,
        val product_type: String,
        val products: ArrayList<Product>,
        val qty: String,
        val sale_price: String,
        val slug: String,
        val status: String,
        val title: String,
        val updated_at: String,
        val vendor_id: String,
        val venue_id: String
    ):Serializable

    data class Product(
      //  var isChekd: Boolean,
        var quantityLocal: Int=0,
        var totPriceLocal: Double = 0.0,
        var category_id: String,
        var discount: String,
        var title: String,
        var price: String,

        var description: String,
        var id: String,
        var free: String,
        var image: String,
        var ml: String,
        var product_type: String,
        var qty: String,
        var created_at: String,
        var sale_price: String,
        var slug: String,
        var status: String,
        var updated_at: String,
        var vendor_id: String,
        var venue_id: String
    ):Serializable

    data class Info(
        val created_at: String,
        val day: String,
        val id: String,
        val slug: String,
        val status: String,
        val title: String,
        val updated_at: String,
        val vendor_id: String,
        val venue_id: String
    ):Serializable

    data class FacilityDetail(
        val created_at: String,
        val id: String,
        val slug: String,
        val status: String,
        val title: String,
        val updated_at: String
    ):Serializable
}