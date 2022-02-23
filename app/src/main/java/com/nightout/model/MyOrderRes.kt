package com.nightout.model

import java.io.Serializable

data class MyOrderRes(
    val `data`: ArrayList<Data>,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
):Serializable{
    data class Data(
        val amount: String,
        val created_at: String,
        val id: String,
        val order_id: String,
        val payment_mode: String,
        val status: String,
        val transaList: ArrayList<Transa>,
        val transaction_id: String,
        val type: String,
        val updated_at: String,
        val user_id: String,
        val vendor_id: String,
        val venue_id: String
    ):Serializable

    data class Transa(
        val amount: String,
        val created_at: String,
        val id: String,
        val order_id: String,
        val order_list: ArrayList<Order>,
        val payment_mode: String,
        val service_charge: String,
        val status: String,
        val transaction_id: String,
        val type: String,
        val updated_at: String,
        val user_id: String,
        val vendor_id: String,
        val venue_detail: VenueDetail,
        val venue_id: String
    ):Serializable

    data class Order(
        val amount: String,
        val created_at: String,
        val grand_total: String,
        val id: String,
        val order_id: String,
        val payment_mode: String,
        val product_detail: ProductDetail,
        val product_id: String,
        val qty: String,
        val status: String,
        val updated_at: String,
        val user_id: String,
        val vendor_id: String,
        val venue_id: String
    ):Serializable

    data class VenueDetail(
        val age_limit: String,
        val alcohol_license_image: String,
        val alcohol_license_number: String,
        val by_default: String,
        val city: String,
        val close_time: String,
        val created_at: String,
        val dress_code: String,
        val end_time: String,
        val event_date: String,
        val event_date1: String,
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
        val sale_price: String,
        val slug: String,
        val start_time: String,
        val status: String,
        val std_code: String,
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
    ):Serializable

    data class ProductDetail(
        val category_id: String,
        val created_at: String,
        val description: String,
        val discount: String,
        val free: String,
        val id: String,
        val image: String,
        val ml: String,
        val price: String,
        val product_type: String,
        val qty: String,
        val sale_price: String,
        val slug: String,
        val status: String,
        val title: String,
        val updated_at: String,
        val vendor_id: String,
        val venue_id: String
    ):Serializable
}