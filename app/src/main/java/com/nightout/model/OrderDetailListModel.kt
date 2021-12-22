package com.nightout.model

import java.io.Serializable

class OrderDetailListModel(var strTitle: String,var subListMenu: ArrayList<Product>) {
    data class Product(
        var isChekd: Boolean,
        var quantityLocal: Int = 0,
        var totPriceLocal: Double = 0.0,
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
    ) : Serializable
}