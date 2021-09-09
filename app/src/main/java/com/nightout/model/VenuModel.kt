package com.nightout.model

import java.io.Serializable

data class VenuModel(var id : Int,var title:String,var isSelected : Boolean,var isApiCall : Boolean) : Serializable