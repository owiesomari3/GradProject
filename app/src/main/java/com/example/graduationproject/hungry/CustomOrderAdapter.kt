package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.*
import com.example.graduationproject.chef.OrderHistoryFragment
import com.example.graduationproject.chef.OrdersChefsFragment
import com.example.graduationproject.enums.OrderStatus
import com.example.graduationproject.enums.PaymentMethods
import org.json.JSONArray
import org.json.JSONObject

class CustomOrderAdapter(
    private val orderList: ArrayList<Order>,
    private val activity: AppCompatActivity? = null
) :
    RecyclerView.Adapter<CustomOrderAdapter.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycle_order, parent, false)
        return ViewHolder(v)
    }


    @SuppressLint("InflateParams")
    private fun showDialogForVisaPayment(context: Context, orderId: String, price: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.paymentdialog, null)
        val alertDialogBuilder = AlertDialog.Builder(context).apply {
            setView(dialogView)
            setCancelable(false)
        }

        val alertDialog = alertDialogBuilder.create()
        dialogView.apply {
            val payBtn = findViewById<Button>(R.id.paymentButton)
            alertDialog.apply {
                payBtn?.setOnClickListener {
                    changeOrderStatus(
                        OrderStatus.CompletedVisa,
                        context,
                        orderId,
                        price
                    )
                    dismiss() // Dismiss the dialog
                }

            }.show()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            order_payment_method.text = data.payment_method
            price.text = Util.currencyFormat(data.price.toString())
            description.text = data.description
            quantity.text = data.quantity
            chefEmail.text = data.chefEmail
            Phone.text = data.chefPhone
            order_status.text = data.status


            if (data.status == OrderStatus.Pending.name) continer.visibility = View.VISIBLE

            if (data.status == OrderStatus.OnTheWay.name) {
                chef_email.text = data.chefEmail
                chef_phone.text = data.chefPhone
                familir_n_drive.text = data.familiar_name
                continer.visibility = View.GONE
                delevr.visibility = View.VISIBLE
            }

            if (data.status == OrderStatus.Canceled.name) {
                continer.visibility = View.GONE
            }


            if (data.status == OrderStatus.Cooking.name) {
                chef_email_cooking.text = data.chefEmail
                chef_phone_cooking.text = data.chefPhone
                fami_name_cooking.text = data.familiar_name
                continercooking.visibility = View.VISIBLE
                continer.visibility = View.GONE

            }
            if (data.status == OrderStatus.Completed.name && data.payment_method == PaymentMethods.Visa.name) {
                continer.visibility = View.GONE
                continerCompleted.visibility = View.VISIBLE
                rating_layout.visibility = View.GONE
                cash_layout.visibility = View.GONE
                com_pay.visibility = View.VISIBLE
                pay_method.text = data.payment_method
                totalPrice.text = Util.currencyFormat(
                    (data.price.toString().toDouble() * data.quantity.toString()
                        .toDouble()).toString()
                )
                DepositBTN.setOnClickListener {
                    showDialogForVisaPayment(
                        it.context,
                        orderList[position].orderId!!,
                        (data.price.toString().toDouble() * data.quantity.toString()
                            .toDouble()).toString()
                    )
                    payment(data.orderId, it.context)
                    com_pay.visibility = View.GONE
                    rating_layout.visibility = View.VISIBLE
                    submitBtn.setOnClickListener {
                        rating_layout.visibility = View.GONE
                        thx.visibility = View.VISIBLE
                        Handler().postDelayed({
                            thx.visibility = View.GONE
                            replaceFragment(OrderHistoryFragment(), null, activity)
                        }, 5700)

                    }
                }

            }
            if (data.status == OrderStatus.Completed.name && data.payment_method == PaymentMethods.Cash.name) {
                continer.visibility = View.GONE
                continerCompleted.visibility = View.VISIBLE
                rating_layout.visibility = View.GONE
                com_pay.visibility = View.GONE
                thx.visibility = View.GONE
                cash_layout.visibility = View.VISIBLE
                pay_method_cash.text = data.payment_method
                total_cash.text = Util.currencyFormat(
                    (data.price.toString().toDouble() * data.quantity.toString()
                        .toDouble()).toString()
                )
                ofCoursBtn.setOnClickListener {
                    vchangeOrderStatus(
                        OrderStatus.CompletedCash,
                        it.context,
                        orderList[position].orderId
                    )

                    cash_layout.visibility = View.GONE
                    rating_layout.visibility = View.VISIBLE
                }

                submitBtn.setOnClickListener {
                    rating_layout.visibility = View.GONE
                    thx.visibility = View.VISIBLE
                    Handler().postDelayed({
                        replaceFragment(OrderHistoryFragment(), null, activity)
                        thx.visibility = View.GONE
                    }, 5700)
                }
            }
        }
    }

    private fun vchangeOrderStatus(
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
    }


    @SuppressLint("SuspiciousIndentation")
    private fun payment(orderId: String?, context: Context) {
        val jsonArray = Storage.allOrder(context)
        val allFoods = Storage.getAllFoods(context)
        jsonArray?.let {
            for (i in 0 until it.length()) {
                val jsonObject = it.getJSONObject(i)
                if (jsonObject.getString(Constants.ORDER_ID) == orderId)
                    jsonObject.put(Constants.ORDER_STATUS, OrderStatus.CompletedVisa)
            }
            Storage.saveAllOrder(context, jsonArray)
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
        var checfEmail = "";
        allOrders?.let {
            for (i in 0 until it.length()) {
                val jsonObject = allOrders.getJSONObject(i)
                val id = jsonObject.getString(Constants.ORDER_ID)
                if (id == orderId) {
                    jsonObject.put(Constants.ORDER_STATUS, s.name)
                    val paymentMethod = jsonObject.get(Constants.PAYMENT_METHOD)
                    isVisa = paymentMethod == PaymentMethods.Visa.name
                    checfEmail = jsonObject.getString(Constants.CURRENT_CHEF)

                }
            }
            saveAllOrder(allOrders, context)
        }

        if (s == OrderStatus.CompletedVisa && isVisa) {
            val allWallets = Storage.getAllWallet(context) ?: JSONArray()
            if (allWallets.length() == 0) {
                val json = JSONObject()
                json.put(Constants.CURRENT_CHEF, checfEmail)
                json.put(Constants.BALANCE, totalPrice)
                allWallets.put(json)
            } else {
                var isUserFound = false
                for (i in 0 until allWallets.length()) {
                    val json = allWallets.getJSONObject(i)
                    val email = json.get(Constants.CURRENT_CHEF)
                    if (email == checfEmail) {
                        isUserFound = true
                        var oldBalance = json.get(Constants.BALANCE).toString().toDouble()
                        oldBalance += totalPrice?.toDouble()!!
                        json.put(Constants.CURRENT_CHEF, checfEmail)
                        json.put(Constants.BALANCE, oldBalance.toString())
                        allWallets.remove(i)
                        allWallets.put(json)
                    }
                }


                if (!isUserFound) {
                    val json = JSONObject()
                    json.put(Constants.CURRENT_CHEF, checfEmail)
                    json.put(Constants.BALANCE, totalPrice)
                    allWallets.put(json)
                }
            }

            saveAllWallets(context, allWallets)

        }
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


    override fun getItemCount() = orderList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val familiarName: TextView
        val order_payment_method: TextView
        var price: TextView
        var image: ImageView
        var description: TextView
        var chefEmail: TextView
        var quantity: TextView
        var continer: CardView
        var delevr: CardView
        var continercooking: CardView
        var continerCompleted: CardView
        var ratingBar: RatingBar
        var submitBtn: Button
        var ofCoursBtn: Button
        var DepositBTN: Button
        val rating_layout: LinearLayout
        val cash_layout: LinearLayout
        val com_pay: LinearLayout
        val thx: LinearLayout
        val chef_email: TextView
        val chef_phone: TextView
        val pay_method: TextView
        val chef_email_cooking: TextView
        val chef_phone_cooking: TextView
        val Phone: TextView
        val order_status: TextView
        val totalPrice: TextView
        val pay_method_cash: TextView
        val total_cash: TextView
        val familir_n_drive: TextView
        val fami_name_cooking: TextView

        init {
            familiarName = itemView.findViewById(R.id.order_familiar_name)
            fami_name_cooking = itemView.findViewById(R.id.fami_name_cooking)
            familir_n_drive = itemView.findViewById(R.id.familir_n_drive)
            total_cash = itemView.findViewById(R.id.total_cash)
            ofCoursBtn = itemView.findViewById(R.id.ofCoursBtn)
            pay_method_cash = itemView.findViewById(R.id.pay_method_cash)
            order_payment_method = itemView.findViewById(R.id.order_payment_method)
            pay_method = itemView.findViewById(R.id.pay_method)
            chefEmail = itemView.findViewById(R.id.order_chefEmail)
            com_pay = itemView.findViewById(R.id.com_pay)
            price = itemView.findViewById(R.id.order_price)
            cash_layout = itemView.findViewById(R.id.cash_layout)
            totalPrice = itemView.findViewById(R.id.total)
            quantity = itemView.findViewById(R.id.order_quantity)
            image = itemView.findViewById(R.id.order_image_food)
            description = itemView.findViewById(R.id.order_description)
            DepositBTN = itemView.findViewById(R.id.DepositBTN)
            delevr = itemView.findViewById(R.id.delevr)
            continerCompleted = itemView.findViewById(R.id.continerCompleted)
            continer = itemView.findViewById(R.id.order)
            continercooking = itemView.findViewById(R.id.continercooking)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun replaceFragment(fragment: Fragment, data: Order?, activity: AppCompatActivity?) {
        data?.let {
            val data1 = Bundle()
            fragment.arguments = data1
        }

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_layout_hungry, fragment)
        fragmentTransaction?.commit()
    }

}