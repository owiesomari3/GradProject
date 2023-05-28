package com.example.graduationproject

data class Order(
    var familiar_name: String,
    var price: String,
    var image: String,
    var rate: Double,
    var description: String,
    var foodId:String,
    var chefEmail:String,
    var quantity:Int
)
