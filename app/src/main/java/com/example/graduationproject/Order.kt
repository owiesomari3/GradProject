package com.example.graduationproject
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    var familiar_name: String?,
    var price: String?,
    var image: String?,
    var description: String?,
    var foodId: String?,
    var chefEmail: String?,
    var quantity: String?,
    var rate: String?="5",
    var status: String?
) : Parcelable
