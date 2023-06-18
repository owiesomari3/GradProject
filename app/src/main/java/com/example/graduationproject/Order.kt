package com.example.graduationproject
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    var familiar_name: String?,
    var price: Double?,
    var image: String?,
    var description: String?,
    var foodId: String?,
    var chefEmail: String?,
    var quantity: String?,
    var status: String?,
    var chefPhone: String?,
    var orderId: String? = null,
    var isOrderRated: String? = null,
) : Parcelable
