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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.*
import com.example.graduationproject.enums.OrderStatus
import com.example.graduationproject.enums.PaymentMethods
import org.json.JSONArray
import org.json.JSONObject

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

            rate.text = data.rate
            familiarName.text = data.familiar_name
            price.text = data.price
            quantity.text = data.quantity
            hungryEmail.text = data.chefEmail
            phoneNumber.text = data.hungryPhone
            status.text = data.orderStatus

            if (data.orderStatus != OrderStatus.PENDING.name) {
                btnAccept.visibility = View.GONE
                btnCancel.visibility = View.GONE
            }
            totalPrice.text =
                (price.text.toString().toDouble() * quantity.text.toString().toDouble()).toString()
            btnAccept.setOnClickListener {
                changeOrderStatus(
                    OrderStatus.COMPLETED,
                    it.context,
                    orderList[position].orderId,
                    (price.text.toString().toDouble() * quantity.text.toString()
                        .toDouble()).toString()
                )
            }
            btnCancel.setOnClickListener {
                changeOrderStatus(
                    OrderStatus.CANCELED,
                    it.context,
                    orderList[position].orderId, ""
                )
            }

            openLocation.setOnClickListener {
                replaceFragment(MapsFragment(), data, activity)
            }
        }
    }

    override fun getItemCount() = orderList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val familiarName: TextView
        var price: TextView
        var rate: TextView
        var image: ImageView
        var hungryEmail: TextView
        var quantity: TextView
        var status: TextView
        var totalPrice: TextView
        val btnAccept: Button
        val btnCancel: Button
        val phoneNumber: TextView
        val openLocation: Button

        init {
            familiarName = itemView.findViewById(R.id.order_familiar_name)
            rate = itemView.findViewById(R.id.order_rate)
            status = itemView.findViewById(R.id.order_status)
            hungryEmail = itemView.findViewById(R.id.order_HungryEmail)
            price = itemView.findViewById(R.id.order_price)
            quantity = itemView.findViewById(R.id.order_quantity)
            image = itemView.findViewById(R.id.order_image_food)
            totalPrice = itemView.findViewById(R.id.order_total)
            btnAccept = itemView.findViewById(R.id.Order_Accept)
            btnCancel = itemView.findViewById(R.id.Order_Cancel)
            phoneNumber = itemView.findViewById(R.id.phone_number)
            openLocation = itemView.findViewById(R.id.open_location)


        }
    }

    private fun changeOrderStatus(
        s: OrderStatus,
        context: Context,
        orderId: String?,
        totalPrice: String?
    ) {
        val allOrders = Storage.allOrder(context)
        var isVisa = false
        allOrders?.let {
            for (i in 0 until it.length()) {
                val jsonObject = allOrders.getJSONObject(i)
                val id = jsonObject.getString(Constants.ORDER_ID)
                if (id == orderId) {
                    jsonObject.put(Constants.ORDER_STATUS, s.name)
                    val paymentMethod = jsonObject.get(Constants.PAYMENT_METHOD)
                    isVisa = paymentMethod == PaymentMethods.VISA.name
                }
            }
            saveAllOrder(allOrders, context)
        }

        if (s == OrderStatus.COMPLETED && isVisa) {
            val allWallets = Storage.getAllWallet(context) ?: JSONArray()
            if (allWallets.length() == 0) {
                val json = JSONObject()
                json.put(Constants.CURRENT_CHEF, CacheManager.getCurrentUser())
                json.put(Constants.BALANCE, totalPrice)
                allWallets.put(json)
            } else {
                var isUserFound = false
                for (i in 0 until allWallets.length()) {
                    val json = allWallets.getJSONObject(i)
                    val email = json.get(Constants.CURRENT_CHEF)
                    if (email == CacheManager.getCurrentUser()) {
                        isUserFound = true
                        var oldBalance = json.get(Constants.BALANCE).toString().toDouble()
                        oldBalance += totalPrice?.toDouble()!!
                        json.put(Constants.CURRENT_CHEF, CacheManager.getCurrentUser())
                        json.put(Constants.BALANCE, oldBalance.toString())
                        allWallets.remove(i)
                        allWallets.put(json)
                    }
                }

                if (!isUserFound) {
                    val json = JSONObject()
                    json.put(Constants.CURRENT_CHEF, CacheManager.getCurrentUser())
                    json.put(Constants.BALANCE, totalPrice)
                    allWallets.put(json)
                }
            }
            saveAllWallets(context, allWallets)
        }

        if (s == OrderStatus.CANCELED) Util.showToastMsg(
            context,
            "Order has been cancelled successfully"
        )
        else Util.showToastMsg(context, "Order Accepted successfully")
    }

    private fun saveAllOrder(jsonArray: JSONArray, context: Context) {
        val sharedPreferences = context.getSharedPreferences(Constants.ORDER, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(Constants.ORDER_LIST, jsonArray.toString())
        editor?.apply()
    }

    private fun saveAllWallets(context: Context, jsonArray: JSONArray) {
        val jsonString = jsonArray.toString()
        val sharedPreferences =
            context.getSharedPreferences(Constants.WALLET, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.WALLET_List, jsonString)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun replaceFragment(fragment: Fragment, data: OrderChef, activity: AppCompatActivity?) {
        val data1 = Bundle()
        data1.putString(Constants.LATITUDE, data.lat)
        data1.putString(Constants.LONGITUDE, data.long)
        fragment.arguments = data1
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_layout_chef, fragment)
        fragmentTransaction?.commit()
    }
}