package com.example.graduationproject.hungry

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataFood(
    var familiar_name: String,
    var price: String,
    var image: String,
    var description: String,
    var foodId: String,
    var chefEmail: String,
    var offerPrice: String?
) : Parcelable