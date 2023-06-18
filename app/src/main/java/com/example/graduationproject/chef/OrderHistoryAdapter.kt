package com.example.graduationproject.chef


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.*

class OrderHistoryAdapter(
    private val orderList: ArrayList<OrderChef>,
    private val activity: AppCompatActivity? = null
) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_order_chfe, parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: OrderChef = orderList[position]
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
            quantity.text = data.quantity
            hungryEmail.text = data.chefEmail
            phoneNumber.text = data.hungryPhone
            status.text = data.orderStatus
            totalPrice.text = Util.currencyFormat(
                (data.price.toString().toDouble() * data.quantity.toString().toDouble()).toString()
            )
            btnAccept.visibility = View.GONE
            btnCancel.visibility = View.GONE
            cookingBtn.visibility = View.GONE
            open_location.visibility=View.GONE
            done.visibility = View.GONE

        }
    }

    override fun getItemCount() = orderList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val familiarName: TextView
        var price: TextView
        var image: ImageView
        var hungryEmail: TextView
        var quantity: TextView
        var status: TextView
        var totalPrice: TextView
        val btnAccept: Button
        val btnCancel: Button
        val phoneNumber: TextView
        val cookingBtn: Button
        val done: Button
        val open_location: Button

        init {
            familiarName = itemView.findViewById(R.id.order_familiar_name)
            status = itemView.findViewById(R.id.order_status)
            hungryEmail = itemView.findViewById(R.id.order_HungryEmail)
            price = itemView.findViewById(R.id.order_price)
            quantity = itemView.findViewById(R.id.order_quantity)
            image = itemView.findViewById(R.id.order_image_food)
            totalPrice = itemView.findViewById(R.id.order_total)
            btnAccept = itemView.findViewById(R.id.Order_Accept)
            btnCancel = itemView.findViewById(R.id.Order_Cancel)
            phoneNumber = itemView.findViewById(R.id.phone_number)
            cookingBtn = itemView.findViewById(R.id.cooking_btn)
            done = itemView.findViewById(R.id.done)
            open_location = itemView.findViewById(R.id.open_location)
        }
    }
}