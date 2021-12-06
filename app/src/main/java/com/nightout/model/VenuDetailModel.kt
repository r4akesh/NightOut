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
        val barcrawl: String,
        val by_default: String,
        val close_time: String,
        val created_at: String,
        val dayInformations: ArrayList<DayInformation>,
        val dress_code: String,
        val drinkProducts: DrinkProducts,
        val event_date: String,
        val event_end_time: String,
        val event_start_time: String,
        val facilities: ArrayList<Facility>,
        val favrouite: String,
        val foodProducts: FoodProducts,
        val food_certificate_image: String,
        val food_certificate_number: String,
        val food_registration_image: String,
        val food_registration_number: String,
        val free_end_time: String,
        val free_start_time: String,
        val id: String,
        val open_close: String,
        val open_time: String,
        val packageProducts: PackageProducts,
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
        val snackProducts: SnackProducts,
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
        val venue_gallery: ArrayList<VenueGallery>
    ):Serializable

    data class DayInformation(
        val day: String,
        val info: ArrayList<Info>
    ):Serializable

    data class DrinkProducts(
        val categories: ArrayList<Category>
    ):Serializable

    data class Facility(
        val created_at: String,
        val id: String,
        val slug: String,
        val status: String,
        val title: String,
        val updated_at: String
    ):Serializable

    data class FoodProducts(
        val categories: ArrayList<CategoryX>
    ):Serializable

    data class PackageProducts(
        val products: ArrayList<PkgModel>
    ):Serializable

    data class Rating(
        val avg_rating: String,
        val percent: String,
        val review_title: String,
        val total_rating: String
    ):Serializable

    data class SnackProducts(
        val categories: ArrayList<CategoryXX>
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

    data class Category(
        val category: String,
        val category_type: String,
        val created_at: String,
        val description: String,
        val id: String,
        val image: String,
        val products: ArrayList<Product>,
        val slug: String,
        val status: String,
        val updated_at: String
    ):Serializable

    data class Product(
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

    data class CategoryX(
        val category: String,
        val category_type: String,
        val created_at: String,
        val description: String,
        val id: String,
        val image: String,
        val products: ArrayList<ProductX>,
        val slug: String,
        val status: String,
        val updated_at: String
    ):Serializable

    data class ProductX(
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

    data class PkgModel(
        var isChekd: Boolean,
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

    data class CategoryXX(
        val category: String,
        val category_type: String,
        val created_at: String,
        val description: String,
        val id: String,
        val image: String,
        val products: ArrayList<ProductXXX>,
        val slug: String,
        val status: String,
        val updated_at: String
    ):Serializable

    data class ProductXXX(
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