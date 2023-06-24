package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.*

class HistoryAdapter(
    private val orderList: ArrayList<Order>,
    private val activity: AppCompatActivity? = null
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
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
            bug.visibility=View.GONE
            continer.visibility = View.VISIBLE
            statusLayout.visibility = View.VISIBLE
            familiarName.text = data.familiar_name
            price.text = Util.currencyFormat(data.price.toString())
            description.text = data.description
            quantity.text = data.quantity
            chefEmail.text = data.chefEmail
            Phone.text = data.chefPhone
            statusValue.text = data.status
            order_payment_method.text=data.payment_method
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
        val bug: LinearLayout
        val chef_email: TextView
        val chef_phone: TextView
        val chef_email_cooking: TextView
        val chef_phone_cooking: TextView
        val Phone: TextView
        val statusLayout: LinearLayout
        val statusValue: TextView
        val order_payment_method: TextView

        init {
            bug = itemView.findViewById(R.id.bug)
            familiarName = itemView.findViewById(R.id.order_familiar_name)
            chefEmail = itemView.findViewById(R.id.order_chefEmail)
            price = itemView.findViewById(R.id.order_price)
            quantity = itemView.findViewById(R.id.order_quantity)
            image = itemView.findViewById(R.id.order_image_food)
            description = itemView.findViewById(R.id.order_description)
            continerfinish = itemView.findViewById(R.id.delevr)
            continer = itemView.findViewById(R.id.order)
            continercooking = itemView.findViewById(R.id.continercooking)
            continerDone = itemView.findViewById(R.id.continerCompleted)
            ratingBar = itemView.findViewById(R.id.ratingBar)
            submitBtn = itemView.findViewById(R.id.submitBtn)
            rating_layout = itemView.findViewById(R.id.rating_layout)
            thx = itemView.findViewById(R.id.thx)
            chef_email = itemView.findViewById(R.id.chef_email)
            order_payment_method = itemView.findViewById(R.id.order_payment_method)
            chef_phone = itemView.findViewById(R.id.chef_phone)
            chef_email_cooking = itemView.findViewById(R.id.chef_email_cooking)
            chef_phone_cooking = itemView.findViewById(R.id.chef_phone_cooking)
            Phone = itemView.findViewById(R.id.order_chefphone)
            statusLayout = itemView.findViewById(R.id.status_layout)
            statusValue = itemView.findViewById(R.id.status_value)
        }
    }
}