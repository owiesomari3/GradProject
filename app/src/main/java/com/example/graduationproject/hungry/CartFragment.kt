package com.example.graduationproject.hungry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.Order
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.FragmentCartBinding
import org.json.JSONArray

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var orderAdapter: CustomOrderAdapter
    private var dataOrder = ArrayList<Order>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
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
                            val familiarName = jsonObjectDataFood?.getString(Constants.FAMILIAR_NAME)
                            val price = jsonObjectDataFood?.getString(Constants.PRICE)
                            val image = jsonObjectDataFood?.getString(Constants.IMAGE)
                            val description = jsonObjectDataFood?.getString(Constants.DESCRIPTION)
                            val chefEmail = jsonObjectDataFood?.getString(Constants.CURRENT_CHEF)
                            val status = jsonObjectOrder?.getString(Constants.ORDER_STATUS)
                            dataOrder.add(
                                Order(
                                    familiarName,
                                    price,
                                    image,
                                    description,
                                    foodOrderId,
                                    chefEmail,
                                    quantity,
                                    "5",
                                    status
                                )
                            )
                        }
                    }
                }
            }
        }
        orderAdapter = CustomOrderAdapter(dataOrder)
        binding.recyclerOrderHungry.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }
}