package com.example.graduationproject.chef

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.*
import com.example.graduationproject.enums.OrderStatus
import org.json.JSONArray

class CustomOrderAdapterChef(
    private val orderList: ArrayList<OrderChef>,
    private val activity: AppCompatActivity? = null
) :
    RecyclerView.Adapter<CustomOrderAdapterChef.ViewHolder>() {
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
            Order_Type_payment.text = data.typePayment

            if (data.orderStatus == OrderStatus.Pending.name) {
                btnOnTheWay.visibility = View.VISIBLE
                btnCancel.visibility = View.VISIBLE
                cookingBtn.visibility = View.VISIBLE
                btnCompleted.visibility = View.VISIBLE
                openLocation.visibility = View.VISIBLE
            }

            if (data.orderStatus == OrderStatus.Canceled.name) {
                orderContainer.visibility = View.GONE
            }

            if (data.orderStatus == OrderStatus.Cooking.name) {
                btnCancel.visibility = View.GONE
                cookingBtn.visibility = View.GONE
            }

            if (data.orderStatus == OrderStatus.OnTheWay.name) {
                cookingBtn.visibility = View.GONE
                btnOnTheWay.visibility = View.GONE
                btnCancel.visibility = View.GONE
            }

            if (data.orderStatus == OrderStatus.CompletedCash.name) {
                orderContainer.visibility = View.GONE
            }

            if (data.orderStatus == OrderStatus.CompletedVisa.name) {
                orderContainer.visibility = View.GONE
            }

            totalPrice.text = Util.currencyFormat(
                (data.price.toString().toDouble() * data.quantity.toString().toDouble()).toString()
            )

            btnOnTheWay.setOnClickListener {
                changeOrderStatus(
                    OrderStatus.OnTheWay,
                    it.context,
                    orderList[position].orderId
                )
                replaceFragment(OrdersChefsFragment(), null, activity)
            }

            btnCancel.setOnClickListener {
                changeOrderStatus(
                    OrderStatus.Canceled,
                    it.context,
                    orderList[position].orderId
                )
                replaceFragment(OrdersChefsFragment(), null, activity)
            }

            openLocation.setOnClickListener {
                replaceFragment(MapsFragment(), data, activity)
            }

            cookingBtn.setOnClickListener {
                changeOrderStatus(
                    OrderStatus.Cooking,
                    it.context,
                    orderList[position].orderId
                )
                replaceFragment(OrdersChefsFragment(), null, activity)
            }

            btnCompleted.setOnClickListener {
                changeOrderStatus(
                    OrderStatus.Completed,
                    it.context,
                    orderList[position].orderId
                )
                replaceFragment(OrdersChefsFragment(), null, activity)
            }
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
        val btnOnTheWay: Button
        val btnCancel: Button
        val phoneNumber: TextView
        val Order_Type_payment: TextView
        val openLocation: Button
        val cookingBtn: Button
        val btnCompleted: Button
        val orderContainer: CardView

        init {
            familiarName = itemView.findViewById(R.id.order_familiar_name)
            status = itemView.findViewById(R.id.order_status)
            hungryEmail = itemView.findViewById(R.id.order_HungryEmail)
            price = itemView.findViewById(R.id.order_price)
            quantity = itemView.findViewById(R.id.order_quantity)
            image = itemView.findViewById(R.id.order_image_food)
            totalPrice = itemView.findViewById(R.id.order_total)
            btnOnTheWay = itemView.findViewById(R.id.Order_Accept)
            Order_Type_payment = itemView.findViewById(R.id.Order_Type_payment)
            btnCancel = itemView.findViewById(R.id.Order_Cancel)
            phoneNumber = itemView.findViewById(R.id.phone_number)
            openLocation = itemView.findViewById(R.id.open_location)
            cookingBtn = itemView.findViewById(R.id.cooking_btn)
            btnCompleted = itemView.findViewById(R.id.completedBTN)
            orderContainer = itemView.findViewById(R.id.order_contener)
        }
    }

    private fun changeOrderStatus(
        s: OrderStatus,
        context: Context,
        orderId: String?,
    ) {
        val allOrders = Storage.allOrder(context)
        allOrders?.let {
            for (i in 0 until it.length()) {
                val jsonObject = allOrders.getJSONObject(i)
                val id = jsonObject.getString(Constants.ORDER_ID)
                if (id == orderId) {
                    jsonObject.put(Constants.ORDER_STATUS, s.name)
                }
            }
            saveAllOrder(allOrders, context)
        }

        val statusMessage: String = when (s) {
            OrderStatus.Canceled -> "Order has been cancelled successfully"
            OrderStatus.Cooking -> "Order is being cooked"
            OrderStatus.OnTheWay -> "Order is on the way to the hungry"
            else -> "Order completed successfully"
        }

        Util.showToastMsg(context, statusMessage)
    }

    private fun saveAllOrder(jsonArray: JSONArray, context: Context) {
        val sharedPreferences = context.getSharedPreferences(Constants.ORDER, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(Constants.ORDER_LIST, jsonArray.toString())
        editor?.apply()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun replaceFragment(fragment: Fragment, data: OrderChef?, activity: AppCompatActivity?) {
        data?.let {
            val data1 = Bundle()
            data1.putString(Constants.LATITUDE, data.lat)
            data1.putString(Constants.LONGITUDE, data.long)
            fragment.arguments = data1
        }

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_layout_chef, fragment)
        fragmentTransaction?.commit()
    }
}
