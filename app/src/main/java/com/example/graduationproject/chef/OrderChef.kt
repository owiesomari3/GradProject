package com.example.graduationproject.chef
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class OrderChef(
    var familiar_name: String?,
    var price: String?,
    var image: String?,
    var foodId: String?,
    var chefEmail: String?,
    var quantity: String?,
    var rate: String? = "5",
    var orderId: String?,
    var orderStatus: String?,
    var hungryPhone:String?,
    var lat: String? = null,
    var long: String? = null
    ) : Parcelable
