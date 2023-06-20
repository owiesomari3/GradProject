package com.example.graduationproject.hungry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.Order
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.FragmentHistoryOrderBinding
import com.example.graduationproject.enums.OrderStatus
import org.json.JSONArray

class HistoryOrderFragment : Fragment() {

    private lateinit var binding: FragmentHistoryOrderBinding
    private lateinit var orderAdapter: HistoryAdapter
    private var dataOrder = ArrayList<Order>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allOrders = Storage.allOrder(requireContext())
        val allFoods = Storage.getAllFoods(requireContext()) ?: JSONArray()
        allOrders?.let {
            for (position in 0 until allOrders.length()) {
                val jsonObjectOrder = allOrders.getJSONObject(position)
                val foodOrderId = jsonObjectOrder.getString(Constants.FOOD_ID)
                val userEmail = jsonObjectOrder.getString(Constants.User)
                val quantity = jsonObjectOrder?.getString(Constants.QUANTITY)
                if (CacheManager.getCurrentUser() == userEmail) {
                    for (i in 0 until allFoods.length()) {
                        val jsonObjectDataFood = allFoods.getJSONObject(i)
                        val foodId = jsonObjectDataFood?.getString(Constants.FOOD_ID)
                        if (foodOrderId == foodId) {
                            val familiarName =
                                jsonObjectDataFood?.getString(Constants.FAMILIAR_NAME)
                            val price = jsonObjectDataFood?.getString(Constants.PRICE)
                            val offerPrice = jsonObjectDataFood?.getString(Constants.OFFER_PRICE)
                            val image = jsonObjectDataFood?.getString(Constants.IMAGE)
                            val description = jsonObjectDataFood?.getString(Constants.DESCRIPTION)
                            val chefEmail = jsonObjectDataFood?.getString(Constants.CURRENT_CHEF)
                            val hungryPhone = getChefPhone(chefEmail)
                            val status = jsonObjectOrder?.getString(Constants.ORDER_STATUS)
                            if (price != null) {
                                dataOrder.add(
                                    Order(
                                        familiarName,
                                        if (offerPrice == "0") price.toDouble() else offerPrice?.toDouble(),
                                        image,
                                        description,
                                        foodOrderId,
                                        chefEmail,
                                        quantity,
                                        status,
                                        hungryPhone
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        val canceledOrders =
            dataOrder.filter { it.status == OrderStatus.CANCELED.name || it.status == OrderStatus.DONE.name }
        if (canceledOrders.isEmpty()) {
            binding.noFoodsLayout.visibility = View.VISIBLE
            binding.mainLayout.visibility = View.GONE
        }

        orderAdapter = HistoryAdapter(
            canceledOrders as ArrayList<Order>,
            activity as AppCompatActivity?
        )

        binding.recyclerOrderHungry.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun getChefPhone(email: String?): String {
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
