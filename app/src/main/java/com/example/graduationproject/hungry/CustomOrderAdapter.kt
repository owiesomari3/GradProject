package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.*
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

            familiarName.text = data.familiar_name
            price.text = Util.currencyFormat(data.price.toString())
            description.text = data.description
            quantity.text = data.quantity
            chefEmail.text = data.chefEmail
            Phone.text = data.chefPhone
            order_status.text=data.status

            if (data.status == OrderStatus.PENDING.name) continer.visibility = View.VISIBLE

            if (data.status == OrderStatus.COMPLETED.name) {
                continerfinish.visibility = View.VISIBLE
                continer.visibility = View.GONE
                chef_email.text = data.chefEmail
                chef_phone.text = data.chefPhone
            }

            if (data.status == OrderStatus.CANCELED.name) {
                continer.visibility = View.GONE
            }

            if (data.status == OrderStatus.COOKING.name) {
                continercooking.visibility = View.VISIBLE
                continer.visibility = View.GONE
                chef_email_cooking.text = data.chefEmail
                chef_phone_cooking.text = data.chefPhone
            }

            if (data.status == OrderStatus.DONE.name && data.isOrderRated == "false") {
                continerDone.visibility = View.VISIBLE
                continer.visibility = View.GONE
                submitBtn.setOnClickListener {
                    rating_layout.visibility = View.GONE
                    thx.visibility = View.VISIBLE
                    Handler().postDelayed({ thx.visibility = View.GONE }, 5000)
                    changeIsRating(data.orderId, it.context)
                }
            }

            if (data.isOrderRated == "false") rating_layout.visibility = View.VISIBLE
        }
    }

    private fun changeIsRating(orderId: String?, context: Context) {
        val jsonArray = Storage.allOrder(context)
        jsonArray?.let {
            for (i in 0 until it.length()) {
                val jsonObject = it.getJSONObject(i)
                if (jsonObject.getString(Constants.ORDER_ID) == orderId)
                    jsonObject.put(Constants.IS_ORDER_RATED, "true")
            }
            Storage.saveAllOrder(context, jsonArray)
        }
    }

    override fun getItemCount() = orderList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val familiarName: TextView
        var price: TextView
        var image: ImageView
        var description: TextView
        var chefEmail: TextView
        var quantity: TextView
        var continerfinish: CardView
        var continer: CardView
        var continercooking: CardView
        var continerDone: CardView
        var ratingBar: RatingBar
        var submitBtn: Button
        val rating_layout: LinearLayout
        val thx: LinearLayout
        val chef_email: TextView
        val chef_phone: TextView
        val chef_email_cooking: TextView
        val chef_phone_cooking: TextView
        val Phone: TextView
        val order_status:TextView

        init {
            familiarName = itemView.findViewById(R.id.order_familiar_name)
            chefEmail = itemView.findViewById(R.id.order_chefEmail)
            price = itemView.findViewById(R.id.order_price)
            quantity = itemView.findViewById(R.id.order_quantity)
            image = itemView.findViewById(R.id.order_image_food)
            description = itemView.findViewById(R.id.order_description)
            continerfinish = itemView.findViewById(R.id.finish)
            continer = itemView.findViewById(R.id.order)
            continercooking = itemView.findViewById(R.id.continercooking)
            continerDone = itemView.findViewById(R.id.continerDone)
            ratingBar = itemView.findViewById(R.id.ratingBar)
            submitBtn = itemView.findViewById(R.id.submitBtn)
            rating_layout = itemView.findViewById(R.id.rating_layout)
            thx = itemView.findViewById(R.id.thx)
            chef_email = itemView.findViewById(R.id.chef_email)
            chef_phone = itemView.findViewById(R.id.chef_phone)
            chef_email_cooking = itemView.findViewById(R.id.chef_email_cooking)
            chef_phone_cooking = itemView.findViewById(R.id.chef_phone_cooking)
            Phone = itemView.findViewById(R.id.order_chefphone)
            order_status = itemView.findViewById(R.id.order_status)
        }
    }


}