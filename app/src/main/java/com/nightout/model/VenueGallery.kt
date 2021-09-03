package com.nightout.model

import java.io.Serializable

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