package com.example.graduationproject.chef
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderChef(
    var familiar_name: String?,
    var price: String?,
    var image: String?,
   // var description: String?,
    var foodId: String?,
    var chefEmail: String?,
    var quantity: String?,
    var rate: String?="5",
) : Parcelable
