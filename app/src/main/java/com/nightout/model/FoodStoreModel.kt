package com.nightout.model

import java.io.Serializable

class FoodStoreModel(var title : String, var isSelected : Boolean, var list : ArrayList<SubFoodModel>) : Serializable