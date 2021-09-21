package com.nightout.model

data class ContactFillterModel(
    val `data`: ArrayList<Data>,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
){

data class Data(
    val name: String,
    val phonenumber: String,
    val profile: String,
    val uid: String,
    var isChk: Boolean
)
}