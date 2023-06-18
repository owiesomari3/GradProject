package com.example.graduationproject.chef

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.FragmentOrderHistoryBinding
import com.example.graduationproject.enums.OrderStatus
import org.json.JSONArray

class OrderHistoryFragment : Fragment() {

    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var orderAdapter: OrderHistoryAdapter
    private var dataOrder = ArrayList<OrderChef>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val allOrders = Storage.allOrder(requireContext())
        val allFoods = Storage.getAllFoods(requireContext()) ?: JSONArray()
        allOrders?.let {
            for (position in allOrders.length() - 1 downTo 0) {
                val jsonObjectOrder = allOrders.getJSONObject(position)
                val orderId = jsonObjectOrder.getString(Constants.ORDER_ID)
                val foodOrderId = jsonObjectOrder.getString(Constants.FOOD_ID)
                val chefEmail = jsonObjectOrder?.getString(Constants.CURRENT_CHEF)
                val quantity = jsonObjectOrder?.getString(Constants.QUANTITY)
                val userEmail = jsonObjectOrder?.getString(Constants.User)
                val hungryPhone = getHungryPhone(userEmail)
                val orderStatus = jsonObjectOrder?.getString(Constants.ORDER_STATUS)
                val lat = jsonObjectOrder?.getString(Constants.LATITUDE)
                val long = jsonObjectOrder?.getString(Constants.LONGITUDE)
                if (CacheManager.getCurrentUser() == chefEmail) {
                    for (i in 0 until allFoods.length()) {
                        val jsonObjectDataFood = allFoods.getJSONObject(i)
                        val foodId = jsonObjectDataFood?.getString(Constants.FOOD_ID)
                        if (foodOrderId == foodId) {
                            val familiarName =
                                jsonObjectDataFood?.getString(Constants.FAMILIAR_NAME)
                            val price = jsonObjectDataFood?.getString(Constants.PRICE)
                            val offerPrice = jsonObjectDataFood?.getString(Constants.OFFER_PRICE)

                            val image = jsonObjectDataFood?.getString(Constants.IMAGE)
                            dataOrder.add(
                                OrderChef(
                                    familiarName,
                                    if (offerPrice == "0") price else offerPrice,
                                    image,
                                    foodOrderId,
                                    userEmail,
                                    quantity,
                                    orderId,
                                    orderStatus,
                                    hungryPhone,
                                    lat,
                                    long
                                )
                            )
                        }
                    }
                }
            }
        }

        val canceledOrders = dataOrder.filter { it.orderStatus == OrderStatus.CANCELED.name || it.orderStatus == OrderStatus.DONE.name }

        if (canceledOrders.isEmpty()) {
            binding.noFoodsLayout.visibility = View.VISIBLE
            binding.mainLayout.visibility = View.GONE
        }

        orderAdapter = OrderHistoryAdapter(
            canceledOrders as ArrayList<OrderChef>,
            activity as AppCompatActivity?
        )

        binding.recyclerOrderChef.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun getHungryPhone(email: String?): String {
        var phoneNumber = ""
        val allUsers = Storage.getAllUsers(requireContext()) ?: JSONArray()
        for (i in 0 until allUsers.length()) {
            val json = allUsers.getJSONObject(i)
            if (json.getString(Constants.Email) == email)
                phoneNumber = json.getString(Constants.Phone)
        }
        return phoneNumber
    }
}