package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.Order
import com.example.graduationproject.R
import com.example.graduationproject.enums.OrderStatus

class CustomOrderAdapter(private val orderList: ArrayList<Order>) :
    RecyclerView.Adapter<CustomOrderAdapter.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycle_order, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Order = orderList[position]
        holder.apply {
            try {
                val decodedBytes = Base64.decode(data.image.toString(), Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                image.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            rate.text = data.rate
            familiarName.text = data.familiar_name
            price.text = data.price
            description.text = data.description
            quantity.text = data.quantity
            chefEmail.text = data.chefEmail

            if (data.status == OrderStatus.COMPLETED.name) {
                continerfinish.visibility = View.VISIBLE
                continer.visibility = View.GONE
            }
           if (data.status == OrderStatus.CANCELED.name) {
                continercancel.visibility = View.VISIBLE
                continer.visibility = View.GONE
            }
            if (data.status == OrderStatus.COOKING.name) {
                continercooking.visibility = View.VISIBLE
                continer.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = orderList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val familiarName: TextView
        var price: TextView
        var rate: TextView
        var image: ImageView
        var description: TextView
        var chefEmail: TextView
        var quantity: TextView
        var continerfinish: CardView
        var continer: CardView
        var continercancel: CardView
        var continercooking: CardView

        init {
            familiarName = itemView.findViewById(R.id.order_familiar_name)
            rate = itemView.findViewById(R.id.order_rate)
            chefEmail = itemView.findViewById(R.id.order_chefEmail)
            price = itemView.findViewById(R.id.order_price)
            quantity = itemView.findViewById(R.id.order_quantity)
            image = itemView.findViewById(R.id.order_image_food)
            description = itemView.findViewById(R.id.order_description)
            continerfinish = itemView.findViewById(R.id.finish)
            continer = itemView.findViewById(R.id.order)
            continercancel = itemView.findViewById(R.id.continercancel)
            continercooking = itemView.findViewById(R.id.continercooking)
        }
    }
}